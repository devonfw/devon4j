package com.devonfw.module.kafka.common.messaging.api.client.converter;

import com.devonfw.module.kafka.common.messaging.api.client.MessageSender;

/**
 * This interface is to convert the message in the format required to publish the message to kafka. An implementation
 * class is required to implement the method {@link #convertMessage(Object)}. It is passed as a parameter for
 * {@link MessageSender#sendMessage(org.apache.kafka.clients.producer.ProducerRecord, MessageConverter)}.
 *
 */
@FunctionalInterface
public interface MessageConverter {

  /**
   * This method is used to convert the given message to the required type.
   *
   * @param <T> the type of message.
   * @param message the message which needs to converted
   * @return the converted message.
   */
  public <T> T convertMessage(T message);
}
