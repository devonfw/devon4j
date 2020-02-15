package com.devonfw.module.kafka.common.messaging.retry.api.client;

import com.devonfw.module.kafka.common.messaging.api.Message;

/**
 * @author ravicm
 * @param <T>
 *
 */
@FunctionalInterface
public interface MessageProcessor<T> {

  /**
   * @param message
   */
  public void processMessage(Message<T> message);

}
