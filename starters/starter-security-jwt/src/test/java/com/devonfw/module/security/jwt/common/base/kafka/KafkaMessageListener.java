package com.devonfw.module.security.jwt.common.base.kafka;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageProcessor;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageRetryOperations;
import com.devonfw.module.security.jwt.common.base.kafka.JwtAuthentication;

/**
 * A Listener class with {@link KafkaListener} listens the message for the given topic and group name.
 *
 */
@Named
public class KafkaMessageListener {

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
  @JwtAuthentication(failOnMissingToken = true)
  @KafkaListener(topics = JwtTokenValidationAspectTest.TEST_TOPIC, groupId = "test-group")
  public void consumer(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) throws Exception {

    processMessageAndAcknowledgeListener(consumerRecord, acknowledgment);
  }

  private void processMessageAndAcknowledgeListener(ConsumerRecord<String, String> consumerRecord,
      Acknowledgment acknowledgment) {

    this.messageRetryOperations.processMessageWithRetry(consumerRecord, this.messageProcessor);

    // Acknowledge the listener.
    acknowledgment.acknowledge();
  }

  /**
   * This method is used to listen the message in kafka broker for the given topic and group name in
   * {@link KafkaListener} and also to process the consumed message.
   *
   * @param consumerRecord the consumed {@link ConsumerRecord}
   * @param acknowledgment the {@link Acknowledgment} to acknowledge the listener that message has been processed.
   * @throws Exception exception
   */
  @JwtAuthentication(failOnMissingToken = false)
  @KafkaListener(topics = JwtTokenValidationAspectTest.TEST_TOPIC_2, groupId = "test-group-2")
  public void consumer2(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) throws Exception {

    processMessageAndAcknowledgeListener(consumerRecord, acknowledgment);
  }

}
