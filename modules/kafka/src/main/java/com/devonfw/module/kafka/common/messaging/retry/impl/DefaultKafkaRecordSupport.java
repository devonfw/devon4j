package com.devonfw.module.kafka.common.messaging.retry.impl;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.devonfw.module.kafka.common.messaging.retry.api.client.KafkaRecordSupport;

/**
 * This is an implementation class for {@link KafkaRecordSupport}. Sets retry topic for enqueue process of message.
 */
public class DefaultKafkaRecordSupport implements KafkaRecordSupport {

  private final String RETRY_TOPIC_SUFFIX = "-retry";

  private final String DEFAULT_RETRY_TOPIC = "default-message" + this.RETRY_TOPIC_SUFFIX;

  /**
   * The constructor.
   */
  public DefaultKafkaRecordSupport() {

    super();
  }

  @Override
  public ProducerRecord<Object, Object> createRecordForRetry(ConsumerRecord<Object, Object> record) {

    String retryTopic = Optional.ofNullable(record.topic()).map(this::setRetryTopic).orElse(this.DEFAULT_RETRY_TOPIC);

    return new ProducerRecord<>(retryTopic, record.partition(), record.key(), record.value(), record.headers());
  }

  private String setRetryTopic(String topic) {

    return topic + this.RETRY_TOPIC_SUFFIX;
  }

}
