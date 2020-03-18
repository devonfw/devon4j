package com.devonfw.module.kafka.common.messaging.retry.api.client;

import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * This interface is used to process the received.
 */
@FunctionalInterface
public interface MessageProcessor {

  /**
   * This method is used to process the given message.
   *
   * @param message the message to process.
   */
  public void processMessage(ProducerRecord<Object, Object> message);

}
