package com.devonfw.module.kafka.common.messaging.logging.impl;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListener;

/**
 * This is an implementation class of {@link ProducerListener}.
 *
 * @param <K> The key type
 * @param <V> The value type
 *
 *
 */
public class ProducerLoggingListener<K, V> implements ProducerListener<K, V> {

  private static final Logger LOG = LoggerFactory.getLogger(ProducerLoggingListener.class);

  private MessageLoggingSupport loggingSupport;

  /**
   * The constructor.
   *
   * @param loggingSupport the {@link MessageLoggingSupport}
   */
  public ProducerLoggingListener(MessageLoggingSupport loggingSupport) {

    this.loggingSupport = loggingSupport;
  }

  @Override
  public void onSuccess(String topic, Integer partition, Object key, Object value, RecordMetadata recordMetadata) {

    if (recordMetadata != null) {
      this.loggingSupport.logMessageSent(LOG, value.toString(), recordMetadata.topic(), recordMetadata.partition(),
          recordMetadata.offset());

    } else {
      this.loggingSupport.logMessageSent(LOG, value.toString(), topic, partition, null);
    }
  }

  @Override
  public void onError(String topic, Integer partition, Object key, Object value, Exception exception) {

    this.loggingSupport.logMessageNotSent(LOG, topic, partition,
        (exception != null ? exception.getLocalizedMessage() : "unknown"));
  }
}
