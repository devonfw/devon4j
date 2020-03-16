package com.devonfw.module.kafka.common.messaging.logging.impl;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListener;

import brave.Tracer;

/**
 * This is an implementation class of {@link ProducerListener}.
 *
 * @param <K> the key type.
 * @param <V> the value type.
 *
 */
public class ProducerLoggingListener<K, V> implements ProducerListener<K, V> {

  private static final Logger LOG = LoggerFactory.getLogger(ProducerLoggingListener.class);

  private Tracer tracer;

  private MessageLoggingSupport loggingSupport;

  /**
   * The constructor.
   *
   * @param loggingSupport the {@link MessageLoggingSupport}
   * @param tracer the {@link Tracer}
   */
  public ProducerLoggingListener(MessageLoggingSupport loggingSupport, Tracer tracer) {

    this.loggingSupport = loggingSupport;
    this.tracer = tracer;
  }

  @Override
  public void onSuccess(String topic, Integer partition, K key, V value, RecordMetadata recordMetadata) {

    if (recordMetadata != null) {
      this.loggingSupport.logMessageSent(LOG, value.toString(), recordMetadata.topic(), recordMetadata.partition(),
          recordMetadata.offset());

    } else {
      this.loggingSupport.logMessageSent(LOG, value.toString(), topic, partition, null);
    }
  }

  @Override
  public void onError(String topic, Integer partition, K key, V value, Exception exception) {

    this.loggingSupport.logMessageNotSent(LOG, topic, partition,
        (exception != null ? exception.getLocalizedMessage() : "unknown"));
  }
}
