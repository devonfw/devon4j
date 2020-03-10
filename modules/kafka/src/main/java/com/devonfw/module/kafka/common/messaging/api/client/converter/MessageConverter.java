package com.devonfw.module.kafka.common.messaging.api.client.converter;

/**
 * @author ravicm
 *
 */
@FunctionalInterface
public interface MessageConverter {

  /**
   * @param <T>
   * @param message
   * @return
   */
  public <T> T convertMessage(T message);
}
