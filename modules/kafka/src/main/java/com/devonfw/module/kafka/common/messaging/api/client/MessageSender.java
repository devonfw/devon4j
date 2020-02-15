package com.devonfw.module.kafka.common.messaging.api.client;

import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import com.devonfw.module.kafka.common.messaging.api.Message;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * The interface used to send message and the given other data to kafka broker using {@link KafkaTemplate} with the
 * method {@link KafkaTemplate#send(org.apache.kafka.clients.producer.ProducerRecord)}} with the help of multiple
 * methods. For example, the method {@link #sendMessage(String, Message)}} sends the message along with the topic by
 * interacting with {@link KafkaTemplate}.
 */
public interface MessageSender {

  /**
   * Send the provided {@link Message}} and other informations.
   *
   * @param topic the topic name for the given message. If topic is not given a default topic will be set by
   *        {@link KafkaTemplate}}
   * @param partition no of partitions to replicate
   * @param message the message which needs to needs to be sent
   * @param <T> the message type
   * @return the Future for the {@link ListenableFuture}.
   * @throws JsonProcessingException throws when an error occurs while trying to convert the given {@link Message}} to
   *         {@link ProducerRecord}.
   */
  <T> ListenableFuture<SendResult<String, String>> sendMessage(String topic, int partition, Message<T> message)
      throws JsonProcessingException;

  /**
   * Send the provided {@link Message}} and other informations.
   *
   * @param topic the topic name for the given message. If topic is not given a default topic will be set by
   *        {@link KafkaTemplate}}
   * @param message the message which needs to needs to be sent
   * @param <T> the message type
   * @param key the key for the given {@link Message}}
   * @return the Future for the {@link ListenableFuture}.
   * @throws JsonProcessingException throws when an error occurs while trying to convert the given {@link Message}} to
   *         {@link ProducerRecord}.
   */
  <T> ListenableFuture<SendResult<String, String>> sendMessage(String topic, String key, Message<T> message)
      throws JsonProcessingException;

  /**
   * Send the provided {@link Message}} and other informations.
   *
   * @param topic the topic name for the given message. If topic is not given a default topic will be set by
   *        {@link KafkaTemplate}}
   * @param message the message which needs to needs to be sent
   * @param <T> the message type
   * @return the Future for the {@link ListenableFuture}.
   * @throws JsonProcessingException throws when an error occurs while trying to convert the given {@link Message}} to
   *         {@link ProducerRecord}.
   */
  <T> ListenableFuture<SendResult<String, String>> sendMessage(String topic, Message<T> message)
      throws JsonProcessingException;

  /**
   * Send the provided {@link Message}} and other informations and waits for the default timeout seconds.
   *
   * @param topic the topic name for the given message. If topic is not given a default topic will be set by
   *        {@link KafkaTemplate}}
   * @param message the message which needs to needs to be sent
   * @param <T> the message type
   * @param key the key for the given {@link Message}}
   * @throws Exception generic exception. Throws {@link JsonProcessingException} when an error occurs while trying to
   *         convert the given {@link Message}} to {@link ProducerRecord}. Throws {@link TimeoutException}} when timeout
   *         seconds exceeds.
   */
  <T> void sendMessageAndWait(String topic, String key, Message<T> message) throws Exception;

  /**
   * Send the provided {@link Message}} and other informations and waits for the default timeout seconds.
   *
   * @param topic the topic name for the given message. If topic is not given a default topic will be set by
   *        {@link KafkaTemplate}}
   * @param message the message which needs to needs to be sent
   * @param <T> the message type
   * @param partition no. of partition to replicate
   * @throws Exception generic exception. Throws {@link JsonProcessingException} when an error occurs while trying to
   *         convert the given {@link Message}} to {@link ProducerRecord}. Throws {@link TimeoutException}} when timeout
   *         seconds exceeds.
   */
  <T> void sendMessageAndWait(String topic, int partition, Message<T> message) throws Exception;

  /**
   * Send the provided {@link Message}} and other informations and waits for the default timeout seconds.
   *
   * @param topic the topic name for the given message. If topic is not given a default topic will be set by
   *        {@link KafkaTemplate}}
   * @param message the message which needs to needs to be sent
   * @param <T> the message type
   * @throws Exception generic exception. Throws {@link JsonProcessingException} when an error occurs while trying to
   *         convert the given {@link Message}} to {@link ProducerRecord}. Throws {@link TimeoutException}} when timeout
   *         seconds exceeds.
   */
  <T> void sendMessageAndWait(String topic, Message<T> message) throws Exception;

  /**
   * Send the provided {@link Message}} and other informations and waits for the given timeout seconds.
   *
   * @param topic the topic name for the given message. If topic is not given a default topic will be set by
   *        {@link KafkaTemplate}}
   * @param message the message which needs to needs to be sent
   * @param <T> the message type
   * @param partition no. of partition to replicate
   * @param timeout the seconds needs to wait for the operation to complete
   * @throws Exception generic exception. Throws {@link JsonProcessingException} when an error occurs while trying to
   *         convert the given {@link Message}} to {@link ProducerRecord}. Throws {@link TimeoutException}} when timeout
   *         seconds exceeds.
   */
  <T> void sendMessageAndWait(String topic, int partition, Message<T> message, int timeout) throws Exception;

  /**
   * Send the provided {@link Message}} and other informations and waits for the given timeout seconds.
   *
   * @param topic the topic name for the given message. If topic is not given a default topic will be set by
   *        {@link KafkaTemplate}}
   * @param message the message which needs to needs to be sent
   * @param <T> the message type
   * @param key the key for the given {@link Message}
   * @param timeout the seconds needs to wait for the operation to complete
   * @throws Exception generic exception. Throws {@link JsonProcessingException} when an error occurs while trying to
   *         convert the given {@link Message}} to {@link ProducerRecord}. Throws {@link TimeoutException}} when timeout
   *         seconds exceeds.
   */
  <T> void sendMessageAndWait(String topic, String key, Message<T> message, int timeout) throws Exception;

  /**
   * Send the provided {@link Message}} and other informations and waits for the given timeout seconds.
   *
   * @param topic the topic name for the given message. If topic is not given a default topic will be set by
   *        {@link KafkaTemplate}}
   * @param message the message which needs to needs to be sent
   * @param <T> the message type
   * @param timeout the seconds needs to wait for the operation to complete
   * @throws Exception generic exception. Throws {@link JsonProcessingException} when an error occurs while trying to
   *         convert the given {@link Message}} to {@link ProducerRecord}. Throws {@link TimeoutException}} when timeout
   *         seconds exceeds.
   */
  <T> void sendMessageAndWait(String topic, Message<T> message, int timeout) throws Exception;

}
