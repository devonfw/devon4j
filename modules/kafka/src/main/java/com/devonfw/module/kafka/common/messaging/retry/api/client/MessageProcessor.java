package com.devonfw.module.kafka.common.messaging.retry.api.client;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * This interface is used to process the received.
 *
 * @param <K> the key type.
 * @param <V> the value type.
 *
 */
@FunctionalInterface
public interface MessageProcessor<K, V> {

  /**
   * This method is used to process the given {@link ConsumerRecord}.
   *
   * @param message the message to process.
   */
  public void processMessage(ConsumerRecord<K, V> message);

}
