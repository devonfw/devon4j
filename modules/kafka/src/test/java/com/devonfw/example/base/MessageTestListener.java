package com.devonfw.example.base;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import com.devonfw.example.module.AbstractKafkaBaseTest;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageProcessor;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageRetryOperations;

/**
 * A Listener class with {@link KafkaListener} listens the message for the given topic and group name.
 *
 */
@Named
public class MessageTestListener {

  @Inject
  private MessageRetryOperations<String, String> messageRetryOperations;

  @Inject
  private MessageProcessor<String, String> messageProcessor;

  /**
   * This method is used to listen the message in kafka broker for the given topic and group name in
   * {@link KafkaListener} and also to process the consumed message.
   *
   * @param consumerRecord the consumed {@link ConsumerRecord}
   * @param acknowledgment the {@link Acknowledgment} to acknowledge the listener that message has been processed.
   * @throws Exception exception
   */
  @KafkaListener(topics = AbstractKafkaBaseTest.RETRY_TEST_TOPIC, groupId = AbstractKafkaBaseTest.TEST_GROUP)
  public void consumer(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) throws Exception {

    processMessageAndAcknowledgeListener(consumerRecord, acknowledgment);
  }

  private void processMessageAndAcknowledgeListener(ConsumerRecord<String, String> consumerRecord,
      Acknowledgment acknowledgment) {

    this.messageRetryOperations.processMessageWithRetry(consumerRecord, this.messageProcessor);

    // Acknowledge the listener.
    acknowledgment.acknowledge();
  }

}
