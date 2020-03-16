package com.devonfw.module.kafka.common.messaging.retry.api.client;

import java.time.Instant;

import org.apache.kafka.clients.producer.ProducerRecord;

import com.devonfw.module.kafka.common.messaging.retry.impl.MessageRetryContext;
import com.devonfw.module.kafka.common.messaging.retry.impl.MessageRetryTemplate;

/**
 * This interface is used in {@link MessageRetryTemplate#processMessageWithRetry(ProducerRecord, MessageProcessor)} to
 * indicate the retry to proceed again and to get the retry until timeStamp in {@link Instant} format.
 *
 */
public interface MessageRetryPolicy {

  /**
   * This method is used to indicate to proceed the retry pattern again.
   *
   * @param producerRecord the {@link ProducerRecord}
   * @param retryContext the {@link MessageRetryContext}
   * @param ex the {@link Exception}
   * @return boolean.
   */
  boolean canRetry(ProducerRecord<Object, Object> producerRecord, MessageRetryContext retryContext, Exception ex);

  /**
   * This method is used to return the retry until timeStamp in {@link Instant} format.
   *
   * @param producerRecord
   * @param retryContext
   * @return {@link Instant}
   */
  Instant getRetryUntilTimestamp(ProducerRecord<Object, Object> producerRecord, MessageRetryContext retryContext);

}
