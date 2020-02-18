package com.devonfw.module.kafka.common.messaging.impl;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;

import com.devonfw.module.kafka.common.messaging.api.Message;
import com.devonfw.module.kafka.common.messaging.api.client.MessageSender;
import com.devonfw.module.kafka.common.messaging.api.config.MessageSenderProperties;
import com.devonfw.module.kafka.common.messaging.logging.impl.MessageLoggingSupport;
import com.devonfw.module.kafka.common.messaging.trace.impl.MessageSpanInjector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import brave.Tracer;

/**
 * @author ravicm
 *
 */
public class MessageSenderImpl implements MessageSender {

  private static final Logger LOG = LoggerFactory.getLogger(MessageSenderImpl.class);

  private MessageSenderProperties senderProperties;

  private KafkaTemplate<String, String> kafkaTemplate;

  private ObjectMapper jacksonMapper;

  private MessageLoggingSupport loggingSupport;

  private Tracer tracer;

  private MessageSpanInjector spanInjector;

  /**
   * The constructor.
   */
  public MessageSenderImpl() {

    super();
  }

  /**
   * @param senderProperties
   */
  public void setSenderProperties(MessageSenderProperties senderProperties) {

    this.senderProperties = senderProperties;
  }

  /**
   * @param kafkaTemplate
   */
  public void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {

    this.kafkaTemplate = kafkaTemplate;
  }

  /**
   * @param jacksonMapper
   */
  public void setJacksonMapper(ObjectMapper jacksonMapper) {

    this.jacksonMapper = jacksonMapper;
  }

  /**
   * @param loggingSupport
   */
  public void setLoggingSupport(MessageLoggingSupport loggingSupport) {

    this.loggingSupport = loggingSupport;
  }

  /**
   * @param spanInjector
   */
  public void setSpanInjector(MessageSpanInjector spanInjector) {

    this.spanInjector = spanInjector;
  }

  /**
   * @param tracer
   */
  public void setTracer(Tracer tracer) {

    this.tracer = tracer;
  }

  @Override
  public <T> ListenableFuture<SendResult<String, String>> sendMessage(String topic, int partition, Message<T> message)
      throws JsonProcessingException {

    return createAndSendRecord(message, topic, partition, null);
  }

  @Override
  public <T> ListenableFuture<SendResult<String, String>> sendMessage(String topic, String key, Message<T> message)
      throws JsonProcessingException {

    return createAndSendRecord(message, topic, null, key);
  }

  @Override
  public <T> ListenableFuture<SendResult<String, String>> sendMessage(String topic, Message<T> message)
      throws JsonProcessingException {

    return createAndSendRecord(message, topic, null, null);
  }

  @Override
  public <T> void sendMessageAndWait(String topic, int partition, Message<T> message) throws Exception {

    createAndSendRecordWaited(message, topic, partition, null, this.senderProperties.getDefaultSendTimeoutSeconds());
  }

  @Override
  public <T> void sendMessageAndWait(String topic, String key, Message<T> message) throws Exception {

    createAndSendRecordWaited(message, topic, null, key, this.senderProperties.getDefaultSendTimeoutSeconds());
  }

  @Override
  public <T> void sendMessageAndWait(String topic, Message<T> message) throws Exception {

    createAndSendRecordWaited(message, topic, null, null, this.senderProperties.getDefaultSendTimeoutSeconds());
  }

  @Override
  public <T> void sendMessageAndWait(String topic, int partition, Message<T> message, int timeout) throws Exception {

    createAndSendRecordWaited(message, topic, partition, null, timeout);
  }

  @Override
  public <T> void sendMessageAndWait(String topic, String key, Message<T> message, int timeout) throws Exception {

    createAndSendRecordWaited(message, topic, null, key, timeout);
  }

  @Override
  public <T> void sendMessageAndWait(String topic, Message<T> message, int timeout) throws Exception {

    createAndSendRecordWaited(message, topic, null, null, timeout);
  }

  /**
   * @param <T>
   * @param message
   * @param topic
   * @param partition
   * @param key
   * @return
   * @throws JsonProcessingException
   */
  protected <T> ListenableFuture<SendResult<String, String>> createAndSendRecord(Message<T> message, String topic,
      Integer partition, String key) throws JsonProcessingException {

    ProducerRecord<String, String> record = buildPayload(message, topic, partition, key);

    try {
      return this.kafkaTemplate.send(record);

    } catch (Exception e) {
      this.loggingSupport.logMessageNotSent(LOG, message.getMessageId(), record.topic(), record.partition(),
          e.getLocalizedMessage());
      throw e;
    }
  }

  /**
   * @param <T>
   * @param message
   * @param topic
   * @param partition
   * @param key
   * @param timeout
   * @throws Exception
   */
  protected <T> void createAndSendRecordWaited(Message<T> message, String topic, Integer partition, String key,
      int timeout) throws Exception {

    try {
      ListenableFuture<SendResult<String, String>> sendResultFuture = createAndSendRecord(message, topic, partition,
          key);

      sendResultFuture.get(timeout, TimeUnit.SECONDS);

    } catch (Exception e) {
      throw handleSendWaitedException(e, timeout);
    }
  }

  /**
   * @param e
   * @param timeout
   * @return
   */
  protected Exception handleSendWaitedException(Exception e, int timeout) {

    if (e instanceof TimeoutException) {
      return new TimeoutException(String.valueOf(timeout));
    }

    return e;
  }

  /**
   * @param <T>
   * @param message
   * @param topic
   * @param partition
   * @param key
   * @return
   * @throws JsonProcessingException
   */
  protected <T> ProducerRecord<String, String> buildPayload(Message<T> message, String topic, Integer partition,
      String key) throws JsonProcessingException {

    Assert.notNull(message, "The Parameter 'message' cannot be null.");
    Assert.notNull(message.getPayload(), "The Parameter 'payload' cannot be null.");

    MessageKafkaPayloadBuilder<T> builder = MessageKafkaPayloadBuilder.with(this.jacksonMapper);
    builder.from(message);

    Optional.ofNullable(topic).ifPresent(value -> builder.topic(value));

    builder.partition(partition);

    Optional.ofNullable(key).ifPresentOrElse(k -> builder.key(k), () -> builder.key(message.getMessageId()));

    builder.headers(message.getHeaders());

    checkTracerCurrentSpanAndInject(builder);
    return builder.build();
  }

  /**
   * @param <T>
   * @param builder
   */
  private <T> void checkTracerCurrentSpanAndInject(MessageKafkaPayloadBuilder<T> builder) {

    if (this.tracer != null && this.tracer.currentSpan() != null && this.spanInjector != null) {

      this.spanInjector.inject(this.tracer.currentSpan().context(), builder);
    }
  }

  /**
   *
   */
  @PostConstruct
  public void validateBeanProperties() {

    Assert.notNull(this.senderProperties, "The property 'senderProperties' is not set.");
    Assert.notNull(this.kafkaTemplate, "The property 'kafkaTemplate' is not set.");
    Assert.notNull(this.jacksonMapper, "The property 'jacksonMapper' is not set.");
    Assert.notNull(this.loggingSupport, "The property 'loggingSupport' is not set.");
  }

}
