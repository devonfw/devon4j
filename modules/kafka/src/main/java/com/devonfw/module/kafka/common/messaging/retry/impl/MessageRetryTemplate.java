package com.devonfw.module.kafka.common.messaging.retry.impl;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import com.devonfw.module.kafka.common.messaging.api.Message;
import com.devonfw.module.kafka.common.messaging.api.client.MessageSender;
import com.devonfw.module.kafka.common.messaging.logging.impl.EventKey;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageBackOffPolicy;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageProcessor;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageRetryHandler;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageRetryPolicy;
import com.devonfw.module.kafka.common.messaging.retry.impl.MessageRetryContext.RetryState;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author ravicm
 *
 */
public class MessageRetryTemplate {

  private static final Logger LOG = LoggerFactory.getLogger(MessageRetryTemplate.class);

  private MessageSender messageSender;

  private MessageBackOffPolicy backOffPolicy;

  private MessageRetryPolicy retryPolicy;

  private MessageRetryHandler retryHandler;

  /**
   * The constructor.
   */
  public MessageRetryTemplate() {

  }

  /**
   * The constructor.
   *
   * @param retryPolicy
   * @param backOffPolicy
   */
  public MessageRetryTemplate(MessageRetryPolicy retryPolicy, MessageBackOffPolicy backOffPolicy) {

    Assert.notNull(retryPolicy, "The parameter \" retryPolicy \"must be specified.");
    Assert.notNull(backOffPolicy, "The parameter \" backOffPolicy \"must be specified.");

    this.retryPolicy = retryPolicy;
    this.backOffPolicy = backOffPolicy;
  }

  /**
   * @param <T>
   * @param message
   * @param processor
   * @param retryTopic
   * @return
   * @throws JsonProcessingException
   */
  public <T> MessageRetryProcessingResult processMessageWithRetry(Message<T> message, MessageProcessor<T> processor,
      String retryTopic) throws JsonProcessingException {

    return processMessageWithRetry(message, processor, retryTopic, this.retryPolicy, this.backOffPolicy);
  }

  /**
   * @param <T>
   * @param message
   * @param processor
   * @param retryTopic
   * @param pRetryPolicy
   * @param pBackOffPolicy
   * @return
   * @throws JsonProcessingException
   */
  public <T> MessageRetryProcessingResult processMessageWithRetry(Message<T> message, MessageProcessor<T> processor,
      String retryTopic, MessageRetryPolicy pRetryPolicy, MessageBackOffPolicy pBackOffPolicy)
      throws JsonProcessingException {

    Assert.notNull(message, "No message was given.");
    Assert.notNull(processor, "No message processor was specified.");
    Assert.notNull(pRetryPolicy, "No retry policy was specified.");
    Assert.notNull(pBackOffPolicy, "No back-off policy was specified.");

    MessageRetryContext retryContext = MessageRetryContext.from(message);

    if (retryContext != null) {

      if (retryContext.getRetryState() != RetryState.PENDING) {
        LOG.info(EventKey.RETRY_MESSAGE_ALREADY_PROCESSED.getMessage(), message.getMessageId(),
            retryContext.getRetryState());
        return MessageRetryProcessingResult.NO_PROCESSING;
      }

      String now = Instant.now().toString();

      if (now.compareTo(retryContext.getRetryUntil()) > 0) {
        LOG.info(EventKey.RETRY_PERIOD_EXPIRED.getMessage(), retryContext.getRetryUntil(), message.getMessageId());

        retryContext.setRetryState(RetryState.EXPIRED);
        enqueueRetry(message, retryContext, retryTopic);

        if (this.retryHandler != null) {
          this.retryHandler.retryTimeout(message, retryContext);
        }
        return MessageRetryProcessingResult.RETRY_PERIOD_EXPIRED;
      }

      retryContext.incRetryReadCount();

      if (retryContext.getRetryNext() != null && now.compareTo(retryContext.getRetryNext()) < 0) {

        pBackOffPolicy.sleepBeforeReEnqueue();

        LOG.info(EventKey.RETRY_TIME_NOT_REACHED.getMessage(), retryContext.getRetryNext(),
            retryContext.getRetryCount() + 1, message.getMessageId(), retryTopic);

        enqueueRetry(message, retryContext, retryTopic);

        return MessageRetryProcessingResult.RETRY_WITHOUT_PROCESSING;
      }
      retryContext.incRetryCount();
    }

    try {
      processor.processMessage(message);
      if (retryContext != null) {
        LOG.info(EventKey.RETRY_SUCCESSFUL.getMessage(), message.getMessageId(), retryContext.getRetryCount(),
            retryTopic);
        retryContext.setRetryState(RetryState.SUCCESSFUL);
        enqueueRetry(message, retryContext, retryTopic);
      }
      return MessageRetryProcessingResult.PROCESSING_SUCCESSFUL;
    } catch (Exception e) {
      if (pRetryPolicy.canRetry(message, retryContext, e)) {
        long retryCount = (retryContext == null ? 1 : retryContext.getRetryCount() + 1);
        LOG.info(EventKey.RETRY_INITIATED.getMessage(), message.getMessageId(), retryCount, retryTopic);

        enqueueRetry(message, updateRetryContextForNextRetry(message, retryContext, pRetryPolicy, pBackOffPolicy, e),
            retryTopic);

        return MessageRetryProcessingResult.RETRY_AFTER_PROCESSING_TRIAL;
      }

      if (retryContext != null) {
        LOG.info(EventKey.RETRY_FINALLY_FAILED.getMessage(), message.getMessageId(), retryContext.getRetryCount());

        retryContext.setRetryState(RetryState.FAILED);

        enqueueRetry(message, retryContext, retryTopic);

        if (this.retryHandler != null && this.retryHandler.retryFailedFinal(message, retryContext, e)) {
          return MessageRetryProcessingResult.FINAL_FAIL_HANDLER_SUCCESSFUL;
        }
      }
      throw e;
    }
  }

  private MessageRetryContext updateRetryContextForNextRetry(Message<?> message, MessageRetryContext retryContext,
      MessageRetryPolicy pRetryPolicy, MessageBackOffPolicy pBackOffPolicy, Exception e) {

    MessageRetryContext result = retryContext;

    if (ObjectUtils.isEmpty(result)) {
      result = new MessageRetryContext();
      result.setRetryUntil(pRetryPolicy.getRetryUntilTimestamp(message, retryContext));
    }

    result.setRetryNext(pBackOffPolicy.getNextRetryTimestamp(result.getRetryCount(), result.getRetryUntil()));

    // if (e != null && e instanceof MessageRetryableException) {
    // result.setRetryCheckpoint(((MessageRetryableException) e).getCheckpoint());

    // ---------------------- Have a look on this ..
    // if (e != null && e instanceof MessageRetryableException) {
    // result.setRetryCheckpoint(((MessageRetryableException) e).getCheckpoint());
    // } else {
    // result.setRetryCheckpoint(null);
    // }

    return result;
  }

  private void enqueueRetry(Message<?> message, MessageRetryContext retryContext, String retryTopic)
      throws JsonProcessingException {

    retryContext.injectInto(message);
    this.messageSender.sendMessage(retryTopic, message);
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
