package com.devonfw.module.kafka.common.messaging.api.client.parser;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * This interface is used to parse the given message{@link ConsumerRecord consumerRecord}} to the given class
 * type{@link Class payloadClassName} using {@link #parseMessage(ConsumerRecord, Class)}.
 *
 */
@FunctionalInterface
public interface MessageParser {

  /**
   * This method is used to parse message {@link ConsumerRecord consumerRecord}} to the given class type {@link Class
   * payloadClassName}}.
   *
   * @param <T>
   * @param consumerRecord the record consumed from Kafka broker.
   * @param payloadClassName the class type which should be the parsed result.
   * @return the converted message of type T.
   * @throws Exception when invalid class type is given.
   */
  <T> T parseMessage(ConsumerRecord<?, ?> consumerRecord, Class<T> payloadClassName) throws Exception;

}
