package com.devonfw.module.kafka.common.messaging.retry.api.client;

import java.time.Instant;

import org.apache.kafka.clients.producer.ProducerRecord;

import com.devonfw.module.kafka.common.messaging.retry.impl.MessageRetryContext;

/**
 * @author ravicm
 *
 */
public interface MessageRetryPolicy {

  /**
   * @param producerRecord
   * @param retryContext
   * @param ex
   * @return
   */
  boolean canRetry(ProducerRecord<Object, Object> producerRecord, MessageRetryContext retryContext, Exception ex);

  /**
   * @param producerRecord
   * @param retryContext
   * @return
   */
  Instant getRetryUntilTimestamp(ProducerRecord<Object, Object> producerRecord, MessageRetryContext retryContext);

}
