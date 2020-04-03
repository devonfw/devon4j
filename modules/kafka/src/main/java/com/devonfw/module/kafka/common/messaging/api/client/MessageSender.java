package com.devonfw.module.kafka.common.messaging.api.client;

import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * The interface used to send message to kafka broker with the help of multiple methods. For example, the method
 * {@link #sendMessage(ProducerRecord)}} sends the message by interacting with
 * {@link KafkaTemplate#send(org.apache.kafka.clients.producer.ProducerRecord)}}.
 *
 * @param <K> the key type.
 * @param <V> the value type.
 *
 */
public interface MessageSender<K, V> {

  /**
   * Send the provided {@link ProducerRecord}} to kafka.
   *
   * @param producerRecord the {@link ProducerRecord}
   * @return the {@link ListenableFuture}.
   */
  ListenableFuture<SendResult<K, V>> sendMessage(ProducerRecord<K, V> producerRecord);

  /**
   * Send the provided {@link ProducerRecord}} to kafka and waits for the default timeout seconds by default 60.
   *
   * @param producerRecord {@link ProducerRecord}}
   * @throws Exception generic exception. Throws {@link TimeoutException}} when timeout seconds exceeds.
   */
  void sendMessageAndWait(ProducerRecord<K, V> producerRecord) throws Exception;

  /**
   * Send the provided {@link ProducerRecord}} to kafka and waits for the given timeout seconds.
   *
   * @param producerRecord producerRecord the {@link ProducerRecord}
   * @param timeout the seconds needs to wait for the operation to complete
   * @throws Exception generic exception. Throws {@link TimeoutException}} when timeout seconds exceeds.
   */
  void sendMessageAndWait(ProducerRecord<K, V> producerRecord, int timeout) throws Exception;

}