package com.devonfw.module.security.jwt.common.base.kafka;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

/**
 * A Listener class with {@link KafkaListener} listens the message for the given topic and group name.
 *
 */
@Named
public class KafkaMessageListener {

  List<ConsumerRecord<String, String>> receivedMessages = new ArrayList<>();

  /**
   * @return receivedMessages
   */
  public List<ConsumerRecord<String, String>> getReceivedMessages() {

    return this.receivedMessages;
  }

  /**
   * @param receivedMessages new value of {@link #getReceivedMessages}.
   */
  public void setReceivedMessages(List<ConsumerRecord<String, String>> receivedMessages) {

    this.receivedMessages = receivedMessages;
  }

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
  public void consume(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) throws Exception {

    this.receivedMessages.add(consumerRecord);
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
  @KafkaListener(topics = JwtTokenValidationAspectTest.TEST_TOPIC_2, groupId = "test-group")
  public void consume2(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) throws Exception {

    this.receivedMessages.add(consumerRecord);
  }
}
