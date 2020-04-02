package com.devonfw.module.kafka.common.messaging.retry.api.client;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.devonfw.module.kafka.common.messaging.retry.api.MessageRetryProcessingResult;

/**
 * This interface is used to process the given {@link ConsumerRecord} with the retry pattern. The processor can be
 * implemented by {@link MessageProcessor}.
 *
 * @param <K> the key type.
 * @param <V> the value type.
 *
 */
@FunctionalInterface
public interface MessageRetryOperations<K, V> {

  /**
   * This method is used to process the given {@link ConsumerRecord} using
   * {@link MessageProcessor#processMessage(ConsumerRecord)} with retry pattern.
   *
   * @param consumerRecord the {@link ConsumerRecord}
   * @param processor the {@link MessageProcessor}
   * @return MessageRetryProcessingResult the {@link MessageRetryProcessingResult}
   */
  public MessageRetryProcessingResult processMessageWithRetry(ConsumerRecord<K, V> consumerRecord,
      MessageProcessor<K, V> processor);

}
