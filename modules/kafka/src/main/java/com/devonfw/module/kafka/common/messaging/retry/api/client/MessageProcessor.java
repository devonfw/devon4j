package com.devonfw.module.kafka.common.messaging.retry.api.client;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * This interface is used to process the received.
 */
@FunctionalInterface
public interface MessageProcessor {

  /**
   * This method is used to process the given {@link ConsumerRecord}.
   *
   * @param message the message to process.
   */
  public void processMessage(ConsumerRecord<Object, Object> message);

}
