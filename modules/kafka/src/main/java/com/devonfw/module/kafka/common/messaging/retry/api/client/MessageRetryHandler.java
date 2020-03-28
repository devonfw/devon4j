package com.devonfw.module.kafka.common.messaging.retry.api.client;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.devonfw.module.kafka.common.messaging.retry.impl.MessageRetryContext;

/**
 * This interface is used to handle the retry fails. An implementation class is required to handle.
 *
 */
public interface MessageRetryHandler {

  /**
   * This method can handle the timeout happens in the retry pattern with the given {@link ConsumerRecord} and
   * {@link MessageRetryContext}.
   *
   * @param consumerRecord the {@link ConsumerRecord}
   * @param retryContext the {@link MessageRetryContext}.
   */
  void retryTimeout(ConsumerRecord<Object, Object> consumerRecord, MessageRetryContext retryContext);

  /**
   * This method is can be used to indicate the final fail of message retry.
   *
   * @param consumerRecord the {@link ConsumerRecord}
   * @param retryContext the {@link MessageRetryContext}
   * @param ex the {@link Exception}
   * @return the boolean.
   */
  boolean retryFailedFinal(ConsumerRecord<Object, Object> consumerRecord, MessageRetryContext retryContext,
      Exception ex);

}
