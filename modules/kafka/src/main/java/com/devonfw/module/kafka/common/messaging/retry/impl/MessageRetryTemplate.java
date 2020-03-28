package com.devonfw.module.kafka.common.messaging.retry.impl;

import java.time.Instant;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import com.devonfw.module.kafka.common.messaging.api.client.MessageSender;
import com.devonfw.module.kafka.common.messaging.logging.impl.EventKey;
import com.devonfw.module.kafka.common.messaging.retry.api.MessageRetryProcessingResult;
import com.devonfw.module.kafka.common.messaging.retry.api.RetryState;
import com.devonfw.module.kafka.common.messaging.retry.api.client.KafkaRecordSupport;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageBackOffPolicy;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageProcessor;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageRetryHandler;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageRetryOperations;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageRetryPolicy;

/**
 * This is an implementation class for the {@link MessageRetryOperations}.
 *
 */
public class MessageRetryTemplate implements MessageRetryOperations {

  private static final Logger LOG = LoggerFactory.getLogger(MessageRetryTemplate.class);

  private MessageSender messageSender;

  private MessageBackOffPolicy backOffPolicy;

  private MessageRetryPolicy retryPolicy;

  private MessageRetryHandler retryHandler;

  private KafkaRecordSupport kafkaRecordSupport;

  /**
   * The constructor.
   */
  public MessageRetryTemplate() {

    super();
  }

  /**
   * The constructor.
   *
   * @param retryPolicy the {@link MessageRetryPolicy}
   * @param backOffPolicy the {@link MessageBackOffPolicy}
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
  public MessageRetryProcessingResult processMessageWithRetry(ConsumerRecord<Object, Object> consumerRecord,
      MessageProcessor processor) {

    checkParameters(consumerRecord, processor, this.retryPolicy, this.backOffPolicy);

    return processRetry(consumerRecord, processor);
  }

  private <T> MessageRetryProcessingResult processRetry(ConsumerRecord<Object, Object> consumerRecord,
      MessageProcessor processor) {

    MessageRetryContext retryContext = MessageRetryContext.from(consumerRecord);

    if (retryContext != null) {

      if (retryContext.getRetryState() != RetryState.PENDING) {
        LOG.info(EventKey.RETRY_MESSAGE_ALREADY_PROCESSED.getMessage(), retryContext.getRetryState());
        return MessageRetryProcessingResult.NO_PROCESSING;
      }

      Instant now = Instant.now();

      if (now.compareTo(retryContext.getRetryUntil()) > 0) {
        LOG.info(EventKey.RETRY_PERIOD_EXPIRED.getMessage(), retryContext.getRetryUntil());

        retryContext.setRetryState(RetryState.EXPIRED);
        enqueueRetry(consumerRecord, retryContext);

        if (this.retryHandler != null) {
          this.retryHandler.retryTimeout(consumerRecord, retryContext);
        }
        return MessageRetryProcessingResult.RETRY_PERIOD_EXPIRED;
      }

      retryContext.incRetryReadCount();

      if (retryContext.getRetryNext() != null && now.compareTo(retryContext.getRetryNext()) < 0) {

        this.backOffPolicy.sleepBeforeReEnqueue();

        LOG.info(EventKey.RETRY_TIME_NOT_REACHED.getMessage(), retryContext.getRetryNext(),
            retryContext.getRetryCount() + 1, consumerRecord.topic());

        enqueueRetry(consumerRecord, retryContext);

        return MessageRetryProcessingResult.RETRY_WITHOUT_PROCESSING;
      }
      retryContext.incRetryCount();
    }

    try {
      processor.processMessage(consumerRecord);

      if (retryContext != null) {
        LOG.info(EventKey.RETRY_SUCCESSFUL.getMessage(), retryContext.getRetryCount(), consumerRecord.topic());
        retryContext.setRetryState(RetryState.SUCCESSFUL);

        enqueueRetry(consumerRecord, retryContext);
      }
      return MessageRetryProcessingResult.PROCESSING_SUCCESSFUL;
    } catch (Exception e) {
      if (this.retryPolicy.canRetry(consumerRecord, retryContext, e)) {
        long retryCount = (retryContext == null ? 1 : retryContext.getRetryCount() + 1);
        LOG.info(EventKey.RETRY_INITIATED.getMessage(), retryCount, consumerRecord.topic());

        enqueueRetry(consumerRecord, updateRetryContextForNextRetry(consumerRecord, retryContext));

        return MessageRetryProcessingResult.RETRY_AFTER_PROCESSING_TRIAL;
      }

      if (retryContext != null) {
        LOG.info(EventKey.RETRY_FINALLY_FAILED.getMessage(), retryContext.getRetryCount());

        retryContext.setRetryState(RetryState.FAILED);

        enqueueRetry(consumerRecord, retryContext);

        if (this.retryHandler != null && this.retryHandler.retryFailedFinal(consumerRecord, retryContext, e)) {
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

  private MessageRetryContext updateRetryContextForNextRetry(ConsumerRecord<Object, Object> consumerRecord,
      MessageRetryContext retryContext) {

    MessageRetryContext result = retryContext;

    if (ObjectUtils.isEmpty(result)) {
      result = new MessageRetryContext();
      result.setRetryUntil(this.retryPolicy.getRetryUntilTimestamp(consumerRecord, retryContext));
    }

    result.setRetryNext(
        this.backOffPolicy.getNextRetryTimestamp(result.getRetryCount(), result.getRetryUntil().toString()));
    return result;
  }

  private void enqueueRetry(ConsumerRecord<Object, Object> consumerRecord, MessageRetryContext retryContext) {

    ProducerRecord<Object, Object> producerRecord = this.kafkaRecordSupport.createRecordForRetry(consumerRecord);

    retryContext.injectInto(producerRecord);
    this.messageSender.sendMessage(producerRecord);
  }

  /**
   * Set the {@link MessageSender}
   *
   * @param messageSender the MessageSender.
   */
  public void setMessageSender(MessageSender messageSender) {

    this.messageSender = messageSender;
  }

  /**
   * Set the {@link MessageBackOffPolicy}
   *
   * @param backOffPolicy MessageBackOffPolicy
   */
  public void setBackOffPolicy(MessageBackOffPolicy backOffPolicy) {

    this.backOffPolicy = backOffPolicy;
  }

  /**
   * Set the {@link MessageRetryPolicy}
   *
   * @param retryPolicy MessageRetryPolicy
   */
  public void setRetryPolicy(MessageRetryPolicy retryPolicy) {

    this.retryPolicy = retryPolicy;
  }

  /**
   * Set the {@link MessageRetryHandler}
   *
   * @param retryHandler MessageRetryHandler.
   */
  public void setRetryHandler(MessageRetryHandler retryHandler) {

    this.retryHandler = retryHandler;
  }

  /**
   * Set the {@link KafkaRecordSupport}
   *
   * @param kafkaRecordSupport KafkaRecordSupport.
   */
  public void setKafkaRecordSupport(KafkaRecordSupport kafkaRecordSupport) {

    this.kafkaRecordSupport = kafkaRecordSupport;
  }

}
