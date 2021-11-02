package com.devonfw.module.kafka.common.messaging.retry.impl;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.devonfw.module.kafka.common.messaging.retry.api.client.KafkaRecordSupport;

/**
 * This is an implementation class for {@link KafkaRecordSupport}. Sets retry topic for enqueue process of message.
 *
 * @param <K> the key type.
 * @param <V> the value type.
 * @deprecated The implementation of devon4j-kafka will be abandoned. It is superseeded by Springs Kafka
 *             implementation.
 */
@Deprecated
public class DefaultKafkaRecordSupport<K, V> implements KafkaRecordSupport<K, V> {

  private final String RETRY_TOPIC_SUFFIX = "-retry";

  private final String DEFAULT_RETRY_TOPIC = "default-message" + this.RETRY_TOPIC_SUFFIX;

  @Override
  public ProducerRecord<K, V> createRecordForRetry(ConsumerRecord<K, V> record) {

    String retryTopic = Optional.ofNullable(record.topic()).map(this::setRetryTopic).orElse(this.DEFAULT_RETRY_TOPIC);

    return new ProducerRecord<>(retryTopic, record.partition(), record.key(), record.value(), record.headers());
  }

  /**
   * This method is used to set retry topic by adding suffix '-retry' to the given topic.
   *
   * @param topic the topic where the suffix needs to be added.
   * @return the retry topic.
   */
  protected String setRetryTopic(String topic) {

    return topic + this.RETRY_TOPIC_SUFFIX;
  }

}
