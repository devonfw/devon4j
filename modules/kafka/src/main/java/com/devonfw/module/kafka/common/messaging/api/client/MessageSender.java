package com.devonfw.module.kafka.common.messaging.api.client;

import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import com.devonfw.module.kafka.common.messaging.api.client.converter.MessageConverter;

/**
 * The interface used to send message to kafka broker with the help of multiple methods. For example, the method
 * {@link #sendMessage(ProducerRecord, MessageConverter)}} sends the message by interacting with
 * {@link KafkaTemplate#send(org.apache.kafka.clients.producer.ProducerRecord)}}.
 *
 */
public interface MessageSender {

  /**
   * Send the provided {@link Message}} and other informations.
   *
   * @param topic the topic name for the given message.
   * @param partition no of partitions to replicate
   * @param producerRecord
   * @param messageConverter
   * @return the Future for the {@link ListenableFuture}.
   */
  ListenableFuture<SendResult<Object, Object>> sendMessage(ProducerRecord<Object, Object> producerRecord,
      MessageConverter messageConverter);

  /**
   * Send the provided {@link Message}} and other informations and waits for the default timeout seconds by default 60.
   *
   * @param producerRecord
   * @param messageConverter
   * @throws Exception generic exception. Throws {@link TimeoutException}} when timeout seconds exceeds.
   */
  void sendMessageAndWait(ProducerRecord<Object, Object> producerRecord, MessageConverter messageConverter)
      throws Exception;

  /**
   * Send the provided {@link Message}} and other informations and waits for the given timeout seconds.
   *
   * @param producerRecord
   * @param messageConverter
   * @param timeout the seconds needs to wait for the operation to complete
   * @throws Exception generic exception. Throws {@link TimeoutException}} when timeout seconds exceeds.
   */
  void sendMessageAndWait(ProducerRecord<Object, Object> producerRecord, MessageConverter messageConverter, int timeout)
      throws Exception;

}