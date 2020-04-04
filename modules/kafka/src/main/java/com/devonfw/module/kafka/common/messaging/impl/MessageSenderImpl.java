package com.devonfw.module.kafka.common.messaging.impl;

import static com.devonfw.module.kafka.common.messaging.util.MessageUtil.addHeaderValue;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;

import com.devonfw.module.kafka.common.messaging.api.client.MessageSender;
import com.devonfw.module.kafka.common.messaging.api.config.MessageSenderProperties;
import com.devonfw.module.kafka.common.messaging.logging.impl.MessageLoggingSupport;
import com.devonfw.module.kafka.common.messaging.trace.impl.MessageSpanInjector;
import com.devonfw.module.kafka.common.messaging.trace.impl.MessageTraceHeaders;
import com.devonfw.module.logging.common.api.DiagnosticContextFacade;
import com.devonfw.module.logging.common.api.LoggingConstants;

import brave.Tracer;

/**
 * This is an implementation class for {@link MessageSender}
 *
 */
public class MessageSenderImpl implements MessageSender<Object, Object> {

  private static final Logger LOG = LoggerFactory.getLogger(MessageSenderImpl.class);

  private MessageSenderProperties senderProperties;

  private KafkaTemplate<Object, Object> kafkaTemplate;

  private MessageLoggingSupport loggingSupport;

  private Tracer tracer;

  private MessageSpanInjector spanInjector;

  private DiagnosticContextFacade diagnosticContextFacade;

  /**
   * The constructor.
   */
  public MessageSenderImpl() {

    super();
  }

  /**
   * Set the {@link MessageSenderProperties}.
   *
   * @param senderProperties {@link MessageSenderProperties}
   */
  public void setSenderProperties(MessageSenderProperties senderProperties) {

    this.senderProperties = senderProperties;
  }

  /**
   * Set the {@link KafkaTemplate}
   *
   * @param kafkaTemplate {@link KafkaTemplate}
   */
  public void setKafkaTemplate(KafkaTemplate<Object, Object> kafkaTemplate) {

    this.kafkaTemplate = kafkaTemplate;
  }

  /**
   * Set {@link MessageLoggingSupport}
   *
   * @param loggingSupport {@link MessageLoggingSupport}.
   */
  public void setLoggingSupport(MessageLoggingSupport loggingSupport) {

    this.loggingSupport = loggingSupport;
  }

  /**
   * Set the {@link MessageSpanInjector}.
   *
   * @param spanInjector {@link MessageSpanInjector}
   */
  public void setSpanInjector(MessageSpanInjector spanInjector) {

    this.spanInjector = spanInjector;
  }

  /**
   * Set the {@link DiagnosticContextFacade}.
   *
   * @param diagnosticContextFacade {@link DiagnosticContextFacade}
   */
  public void setDiagnosticContextFacade(DiagnosticContextFacade diagnosticContextFacade) {

    this.diagnosticContextFacade = diagnosticContextFacade;
  }

  /**
   * Set the {@link Tracer}.
   *
   * @param tracer {@link Tracer}
   */
  public void setTracer(Tracer tracer) {

    this.tracer = tracer;
  }

  @Override
  public ListenableFuture<SendResult<Object, Object>> sendMessage(ProducerRecord<Object, Object> producerRecord) {

    return createAndSendRecord(producerRecord);
  }

  @Override
  public void sendMessageAndWait(ProducerRecord<Object, Object> producerRecord, int timeout) throws Exception {

    createAndSendRecordWaited(producerRecord, timeout);
  }

  @Override
  public void sendMessageAndWait(ProducerRecord<Object, Object> producerRecord) throws Exception {

    createAndSendRecordWaited(producerRecord, this.senderProperties.getDefaultSendTimeoutSeconds());
  }

  private <T> ListenableFuture<SendResult<Object, Object>> createAndSendRecord(
      ProducerRecord<Object, Object> producerRecord) {

    ProducerRecord<Object, Object> updatedRecord = validateProducerRecordAndUpdate(producerRecord);

    try {
      return this.kafkaTemplate.send(updatedRecord);

    } catch (Exception e) {
      this.loggingSupport.logMessageNotSent(LOG, updatedRecord.topic(), updatedRecord.partition(),
          e.getLocalizedMessage());
      throw e;
    }
  }

  private ProducerRecord<Object, Object> validateProducerRecordAndUpdate(
      ProducerRecord<Object, Object> producerRecord) {

    Optional.ofNullable(producerRecord).ifPresent(this::checkProducerRecord);

    return updateProducerRecord(producerRecord);
  }

  private ProducerRecord<Object, Object> updateProducerRecord(ProducerRecord<Object, Object> producerRecord) {

    Headers headers = producerRecord.headers();
    updateHeadersWithTracers(producerRecord.topic(), producerRecord.key(), headers);

    return new ProducerRecord<>(producerRecord.topic(), producerRecord.partition(), producerRecord.key(),
        producerRecord.value(), headers);
  }

  private void updateHeadersWithTracers(String topic, Object key, Headers headers) {

    if (StringUtils.isEmpty(this.diagnosticContextFacade.getCorrelationId())) {
      this.diagnosticContextFacade.setCorrelationId(UUID.randomUUID().toString());
    }

    headers.add(LoggingConstants.CORRELATION_ID, this.diagnosticContextFacade.getCorrelationId().getBytes());
    Optional.ofNullable(key).ifPresent(k -> addHeaderValue(headers, KafkaHeaders.MESSAGE_KEY, k.toString()));
    Optional.ofNullable(topic).ifPresent(t -> addHeaderValue(headers, KafkaHeaders.TOPIC, topic));

    checkTracerCurrentSpanAndInjectHeaders(headers);
  }

  private void checkProducerRecord(ProducerRecord<Object, Object> producerRecord) {

    if (ObjectUtils.isEmpty(producerRecord)) {
      throw new IllegalArgumentException("The parameter 'producerRecord' cannot be null");
    }

    if (ObjectUtils.isEmpty(producerRecord.value())) {
      throw new IllegalArgumentException("The value in producerRecord cannot be null");
    }

    if (ObjectUtils.isEmpty(producerRecord.topic())) {
      throw new IllegalArgumentException("The topic in producerRecord cannot be null");
    }
  }

  private void createAndSendRecordWaited(ProducerRecord<Object, Object> producerRecord, int timeout) throws Exception {

    try {
      ListenableFuture<SendResult<Object, Object>> sendResultFuture = createAndSendRecord(producerRecord);

      sendResultFuture.get(timeout, TimeUnit.SECONDS);

    } catch (Exception e) {
      throw handleSendWaitedException(e, timeout);
    }
  }

  private Exception handleSendWaitedException(Exception e, int timeout) {

    if (e instanceof TimeoutException) {
      return new TimeoutException(String.valueOf(timeout));
    }

    return e;
  }

  private <T> void checkTracerCurrentSpanAndInjectHeaders(Headers headers) {

    Optional.ofNullable(this.tracer).ifPresent(t -> injectTraceHeaders(headers));
  }

  private void injectTraceHeaders(Headers headers) {

    if (this.tracer.currentSpan() != null && this.spanInjector != null) {
      this.spanInjector.inject(this.tracer.currentSpan().context(), headers);
      return;
    }

    if (this.tracer.currentSpan() == null) {
      this.tracer.nextSpan().name(MessageTraceHeaders.SPAN_NAME.toString()).start();
      this.spanInjector.inject(this.tracer.nextSpan().context(), headers);
    }
  }

  /**
   * This method is used to check the given properties are not null.
   */
  @PostConstruct
  public void validateBeanProperties() {

    if (ObjectUtils.isEmpty(this.senderProperties)) {
      throw new IllegalStateException("The property 'senderProperties' is not set.");
    }

    if (ObjectUtils.isEmpty(this.kafkaTemplate)) {
      throw new IllegalArgumentException("The property 'kafkaTemplate' is not set.");
    }

    if (ObjectUtils.isEmpty(this.loggingSupport)) {
      throw new IllegalArgumentException("The property 'loggingSupport' is not set.");
    }

    if (ObjectUtils.isEmpty(this.diagnosticContextFacade)) {
      throw new IllegalArgumentException("The property 'diagnosticContextFacade' is not set.");
    }
  }

}
