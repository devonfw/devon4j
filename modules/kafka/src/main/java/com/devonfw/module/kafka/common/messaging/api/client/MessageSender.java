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
   * Send the provided {@link ProducerRecord}} to kafka.
   *
   * @param producerRecord the {@link ProducerRecord}
   * @param messageConverter the {@link MessageConverter} used to convert the {@link ProducerRecord#value()} to the
   *        required type.
   * @return the {@link ListenableFuture}.
   */
  ListenableFuture<SendResult<Object, Object>> sendMessage(ProducerRecord<Object, Object> producerRecord,
      MessageConverter messageConverter);

  /**
   * Send the provided {@link ProducerRecord}} to kafka and waits for the default timeout seconds by default 60.
   *
   * @param producerRecord {@link ProducerRecord}}
   * @param messageConverter the {@link MessageConverter} used to convert the {@link ProducerRecord#value()} to the
   *        required type.
   * @throws Exception generic exception. Throws {@link TimeoutException}} when timeout seconds exceeds.
   */
  void sendMessageAndWait(ProducerRecord<Object, Object> producerRecord, MessageConverter messageConverter)
      throws Exception;

  /**
   * Send the provided {@link ProducerRecord}} to kafka and waits for the given timeout seconds.
   *
   * @param producerRecord producerRecord the {@link ProducerRecord}
   * @param messageConverter the {@link MessageConverter} used to convert the {@link ProducerRecord#value()} to the
   *        required type.
   * @param timeout the seconds needs to wait for the operation to complete
   * @throws Exception generic exception. Throws {@link TimeoutException}} when timeout seconds exceeds.
   */
  void sendMessageAndWait(ProducerRecord<Object, Object> producerRecord, MessageConverter messageConverter, int timeout)
      throws Exception;

}