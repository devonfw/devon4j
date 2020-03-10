package com.devonfw.module.kafka.common.messaging.retry.api.client;

import org.apache.kafka.clients.producer.ProducerRecord;

import com.devonfw.module.kafka.common.messaging.retry.impl.MessageRetryContext;

/**
 * @author ravicm
 *
 */
public interface MessageRetryHandler {

  /**
   * @param producerRecord
   * @param retryContext
   */
  void retryTimeout(ProducerRecord<Object, Object> producerRecord, MessageRetryContext retryContext);

  /**
   * @param producerRecord
   * @param retryContext
   * @param ex
   * @return
   */
  boolean retryFailedFinal(ProducerRecord<Object, Object> producerRecord, MessageRetryContext retryContext,
      Exception ex);

}
