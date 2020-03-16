package com.devonfw.module.kafka.common.messaging.retry.api.client;

import org.apache.kafka.clients.producer.ProducerRecord;

import com.devonfw.module.kafka.common.messaging.retry.impl.MessageRetryContext;

/**
 * This interface is used to handle the retry fails. An implementation class is required to handle.
 *
 */
public interface MessageRetryHandler {

  /**
   * This method can handle the timeout happens in the retry pattern with the given {@link ProducerRecord} and
   * {@link MessageRetryContext}.
   *
   * @param producerRecord the {@link ProducerRecord}
   * @param retryContext the {@link MessageRetryContext}.
   */
  void retryTimeout(ProducerRecord<Object, Object> producerRecord, MessageRetryContext retryContext);

  /**
   * This method is can be used to indicate the final fail of message retry.
   *
   * @param producerRecord the {@link ProducerRecord}
   * @param retryContext the {@link MessageRetryContext}
   * @param ex the {@link Exception}
   * @return the boolean.
   */
  boolean retryFailedFinal(ProducerRecord<Object, Object> producerRecord, MessageRetryContext retryContext,
      Exception ex);

}
