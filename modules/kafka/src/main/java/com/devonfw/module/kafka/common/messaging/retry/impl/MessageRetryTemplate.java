package com.devonfw.module.kafka.common.messaging.retry.impl;

import java.time.Instant;
import java.util.Optional;

import javax.inject.Named;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import com.devonfw.module.kafka.common.messaging.api.client.MessageSender;
import com.devonfw.module.kafka.common.messaging.logging.impl.EventKey;
import com.devonfw.module.kafka.common.messaging.retry.api.MessageRetryProcessingResult;
import com.devonfw.module.kafka.common.messaging.retry.api.RetryState;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageBackOffPolicy;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageProcessor;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageRetryHandler;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageRetryOperations;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageRetryPolicy;

/**
 * @author ravicm
 *
 */
@Named
public class MessageRetryTemplate implements MessageRetryOperations {

  private static final Logger LOG = LoggerFactory.getLogger(MessageRetryTemplate.class);

  private MessageSender messageSender;

  private MessageBackOffPolicy backOffPolicy;

  private MessageRetryPolicy retryPolicy;

  private MessageRetryHandler retryHandler;

  private final String RETRY_TOPIC_SUFFIX = "-retry";

  private final String DEFAULT_RETRY_TOPIC = "default-message" + this.RETRY_TOPIC_SUFFIX;

  /**
   * The constructor.
   */
  public MessageRetryTemplate() {

    super();
  }

  /**
   * The constructor.
   *
   * @param retryPolicy
   * @param backOffPolicy
   */
  public MessageRetryTemplate(MessageRetryPolicy retryPolicy, MessageBackOffPolicy backOffPolicy) {

    if (ObjectUtils.isEmpty(retryPolicy)) {
      throw new IllegalArgumentException("The parameter \" retryPolicy \"must be specified.");
    }

    if (ObjectUtils.isEmpty(backOffPolicy)) {
      throw new IllegalArgumentException("The parameter \" backOffPolicy \"must be specified.");
    }

    this.retryPolicy = retryPolicy;
    this.backOffPolicy = backOffPolicy;
  }

  @Override
  public MessageRetryProcessingResult processMessageWithRetry(ProducerRecord<Object, Object> producerRecord,
      MessageProcessor processor) {

    checkParameters(producerRecord, processor, this.retryPolicy, this.backOffPolicy);

    String retryTopic = Optional.ofNullable(producerRecord.topic()).map(this::setRetryTopic)
        .orElse(this.DEFAULT_RETRY_TOPIC);

    ProducerRecord<Object, Object> updatedProducerRecord = new ProducerRecord<>(retryTopic, producerRecord.partition(),
        producerRecord.key(), producerRecord.value(), producerRecord.headers());

    return processRetry(updatedProducerRecord, processor);
  }

  private String setRetryTopic(String topic) {

    return topic + this.RETRY_TOPIC_SUFFIX;
  }

  private <T> MessageRetryProcessingResult processRetry(ProducerRecord<Object, Object> producerRecord,
      MessageProcessor processor) {

    MessageRetryContext retryContext = MessageRetryContext.from(producerRecord);

    if (retryContext != null) {

      if (retryContext.getRetryState() != RetryState.PENDING) {
        LOG.info(EventKey.RETRY_MESSAGE_ALREADY_PROCESSED.getMessage(), retryContext.getRetryState());
        return MessageRetryProcessingResult.NO_PROCESSING;
      }

      Instant now = Instant.now();

      if (now.compareTo(retryContext.getRetryUntil()) > 0) {
        LOG.info(EventKey.RETRY_PERIOD_EXPIRED.getMessage(), retryContext.getRetryUntil());

        retryContext.setRetryState(RetryState.EXPIRED);
        enqueueRetry(producerRecord, retryContext);

        if (this.retryHandler != null) {
          this.retryHandler.retryTimeout(producerRecord, retryContext);
        }
        return MessageRetryProcessingResult.RETRY_PERIOD_EXPIRED;
      }

      retryContext.incRetryReadCount();

      if (retryContext.getRetryNext() != null && now.compareTo(retryContext.getRetryNext()) < 0) {

        this.backOffPolicy.sleepBeforeReEnqueue();

        LOG.info(EventKey.RETRY_TIME_NOT_REACHED.getMessage(), retryContext.getRetryNext(),
            retryContext.getRetryCount() + 1, producerRecord.topic());

        enqueueRetry(producerRecord, retryContext);

        return MessageRetryProcessingResult.RETRY_WITHOUT_PROCESSING;
      }
      retryContext.incRetryCount();
    }

    try {
      processor.processMessage(producerRecord);

      if (retryContext != null) {
        LOG.info(EventKey.RETRY_SUCCESSFUL.getMessage(), retryContext.getRetryCount(), producerRecord.topic());
        retryContext.setRetryState(RetryState.SUCCESSFUL);

        enqueueRetry(producerRecord, retryContext);
      }
      return MessageRetryProcessingResult.PROCESSING_SUCCESSFUL;
    } catch (Exception e) {
      if (this.retryPolicy.canRetry(producerRecord, retryContext, e)) {
        long retryCount = (retryContext == null ? 1 : retryContext.getRetryCount() + 1);
        LOG.info(EventKey.RETRY_INITIATED.getMessage(), retryCount, producerRecord.topic());

        enqueueRetry(producerRecord,
            updateRetryContextForNextRetry(producerRecord, retryContext, this.retryPolicy, this.backOffPolicy, e));

        return MessageRetryProcessingResult.RETRY_AFTER_PROCESSING_TRIAL;
      }

      if (retryContext != null) {
        LOG.info(EventKey.RETRY_FINALLY_FAILED.getMessage(), retryContext.getRetryCount());

        retryContext.setRetryState(RetryState.FAILED);

        enqueueRetry(producerRecord, retryContext);

        if (this.retryHandler != null && this.retryHandler.retryFailedFinal(producerRecord, retryContext, e)) {
          return MessageRetryProcessingResult.FINAL_FAIL_HANDLER_SUCCESSFUL;
        }
      }
      throw e;
    }
  }

  private <T> void checkParameters(T message, MessageProcessor processor, MessageRetryPolicy pRetryPolicy,
      MessageBackOffPolicy pBackOffPolicy) {

    if (ObjectUtils.isEmpty(message)) {
      throw new IllegalArgumentException("No message was given.");
    }

    if (ObjectUtils.isEmpty(processor)) {
      throw new IllegalArgumentException("No message processor was specified.");
    }

    if (ObjectUtils.isEmpty(pRetryPolicy)) {
      throw new IllegalArgumentException("No retry policy was specified.");
    }

    if (ObjectUtils.isEmpty(pBackOffPolicy)) {
      throw new IllegalArgumentException("No back-off policy was specified.");
    }
  }

  private MessageRetryContext updateRetryContextForNextRetry(ProducerRecord<Object, Object> producerRecord,
      MessageRetryContext retryContext, MessageRetryPolicy pRetryPolicy, MessageBackOffPolicy pBackOffPolicy,
      Exception e) {

    MessageRetryContext result = retryContext;

    if (ObjectUtils.isEmpty(result)) {
      result = new MessageRetryContext();
      result.setRetryUntil(pRetryPolicy.getRetryUntilTimestamp(producerRecord, retryContext));
    }

    result
        .setRetryNext(pBackOffPolicy.getNextRetryTimestamp(result.getRetryCount(), result.getRetryUntil().toString()));
    return result;
  }

  private void enqueueRetry(ProducerRecord<Object, Object> producerRecord, MessageRetryContext retryContext) {

    retryContext.injectInto(producerRecord);
    this.messageSender.sendMessage(producerRecord, null);
  }

  public void setMessageSender(MessageSender messageSender) {

    this.messageSender = messageSender;
  }

  public void setBackOffPolicy(MessageBackOffPolicy backOffPolicy) {

    this.backOffPolicy = backOffPolicy;
  }

  public void setRetryPolicy(MessageRetryPolicy retryPolicy) {

    this.retryPolicy = retryPolicy;
  }

  public void setRetryHandler(MessageRetryHandler retryHandler) {

    this.retryHandler = retryHandler;
  }

}
