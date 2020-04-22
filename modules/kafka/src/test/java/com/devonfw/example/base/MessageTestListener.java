package com.devonfw.example.base;

import javax.inject.Named;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import com.devonfw.example.base.config.TestMessageKafkaConfig;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageProcessor;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageRetryOperations;

/**
 * A Listener class with {@link KafkaListener} listens the message for the given topic and group name. This class uses
 * the configuration of {@link TestMessageKafkaConfig} and also retry pattern of devon kafka to process the consumed
 * message.
 *
 */
@Named
public class MessageTestListener {

  private MessageRetryOperations<String, String> messageRetryOperations;

  private MessageProcessor<String, String> messageProcessor;

  /**
   * This method is used to listen the message in kafka broker for the given topic and group name in
   * {@link KafkaListener} and also to process the consumed message.
   *
   * @param consumerRecord the consumed {@link ConsumerRecord}
   * @param acknowledgment the {@link Acknowledgment} to acknowledge the listener that message has been processed.
   * @throws Exception exception
   */
  @KafkaListener(topics = "retry-test", groupId = "test-group", containerFactory = "kafkaListenerContainerFactory")
  public void consumer(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) throws Exception {

    processMessageAndAcknowledgeListener(consumerRecord, acknowledgment);
    // throws exception to test the retry.
    throw new Exception();
  }

  private void processMessageAndAcknowledgeListener(ConsumerRecord<String, String> consumerRecord,
      Acknowledgment acknowledgment) {

    this.messageRetryOperations.processMessageWithRetry(consumerRecord, this.messageProcessor);

    // Acknowledge the listener.
    acknowledgment.acknowledge();
  }

}
