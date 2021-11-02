package com.devonfw.module.kafka.common.messaging.retry.api.client;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * This interface is used to process the received.
 *
 * @param <K> the key type.
 * @param <V> the value type.
 *
 * @deprecated The implementation of devon4j-kafka will be abandoned. It is superseeded by Springs Kafka
 *             implementation.
 *
 */
@FunctionalInterface
@Deprecated
public interface MessageProcessor<K, V> {

  /**
   * This method is used to process the given {@link ConsumerRecord}.
   *
   * @param message the message to process.
   */
  public void processMessage(ConsumerRecord<K, V> message);

}
