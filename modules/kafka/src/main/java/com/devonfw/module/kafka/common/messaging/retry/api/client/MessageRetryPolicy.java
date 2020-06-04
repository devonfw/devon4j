package com.devonfw.module.kafka.common.messaging.retry.api.client;

import java.time.Instant;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.devonfw.module.kafka.common.messaging.retry.impl.MessageRetryContext;
import com.devonfw.module.kafka.common.messaging.retry.impl.MessageRetryTemplate;

/**
 * This interface is used in {@link MessageRetryTemplate#processMessageWithRetry(ConsumerRecord, MessageProcessor)} to
 * indicate the retry to proceed again and to get the retry until timeStamp in {@link Instant} format.
 *
 * @param <K> the key type.
 * @param <V> the value type.
 */
public interface MessageRetryPolicy<K, V> {

  /**
   * This method is used to indicate to proceed the retry pattern again.
   *
   * @param consumerRecord the {@link ConsumerRecord}
   * @param retryContext the {@link MessageRetryContext}
   * @param ex the {@link Exception}
   * @return boolean.
   */
  boolean canRetry(ConsumerRecord<K, V> consumerRecord, MessageRetryContext retryContext, Exception ex);

  /**
   * This method is used to return the retry until timeStamp in {@link Instant} format.
   *
   * @param consumerRecord
   * @param retryContext
   * @return {@link Instant}
   */
  Instant getRetryUntilTimestamp(ConsumerRecord<K, V> consumerRecord, MessageRetryContext retryContext);

  /**
   * This method is used to return the number of retry count.
   *
   * @param topic the topic
   *
   * @return the retry count as long.
   */
  long getRetryCount(String topic);

}
