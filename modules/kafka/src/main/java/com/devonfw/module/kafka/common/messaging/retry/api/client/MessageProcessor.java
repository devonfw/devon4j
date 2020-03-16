package com.devonfw.module.kafka.common.messaging.retry.api.client;

/**
 * This interface is used to process the received.
 */
@FunctionalInterface
public interface MessageProcessor {

  /**
   * This method is used to process the given message.
   *
   * @param <T> the type of message to process
   * @param message the message to process.
   */
  public <T> void processMessage(T message);

}
