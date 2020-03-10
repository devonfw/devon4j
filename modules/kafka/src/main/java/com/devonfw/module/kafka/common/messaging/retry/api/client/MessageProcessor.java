package com.devonfw.module.kafka.common.messaging.retry.api.client;

/**
 * @author ravicm
 *
 */
@FunctionalInterface
public interface MessageProcessor {

  /**
   * @param <T>
   * @param message
   */
  public <T> void processMessage(T message);

}
