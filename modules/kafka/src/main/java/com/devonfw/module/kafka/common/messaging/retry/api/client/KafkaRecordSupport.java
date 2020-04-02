package com.devonfw.module.kafka.common.messaging.retry.api.client;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * This interface is used to convert the given {@link ConsumerRecord} to the {@link ProducerRecord} for one of the
 * process of {@link MessageRetryOperations#processMessageWithRetry(ConsumerRecord, MessageProcessor)}.
 *
 * @param <K> the key type.
 * @param <V> the value type.
 */
@FunctionalInterface
public interface KafkaRecordSupport<K, V> {

  /**
   * This method is used to convert the given {@link ConsumerRecord} to the {@link ProducerRecord} .
   *
   * @param record the {@link ConsumerRecord}
   * @return the {@link ProducerRecord}
   */
  ProducerRecord<K, V> createRecordForRetry(ConsumerRecord<K, V> record);

}
