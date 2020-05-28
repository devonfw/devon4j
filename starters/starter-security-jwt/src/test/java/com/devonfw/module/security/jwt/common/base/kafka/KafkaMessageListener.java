package com.devonfw.module.security.jwt.common.base.kafka;

import javax.inject.Named;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * A Listener class with {@link KafkaListener} listens the message for the given topic and group name.
 *
 */
@Named
public class KafkaMessageListener {

  /**
   * This method is used for the purpose of testing {@link JwtTokenValidationAspect}
   *
   * @param consumerRecord the {@link ConsumerRecord}
   * @return the {@link ConsumerRecord}
   * @throws Exception the {@link Exception}
   */
  @JwtAuthentication(failOnMissingToken = true)
  @KafkaListener(topics = JwtTokenValidationAspectTest.TEST_TOPIC, groupId = "test-group")
  public ConsumerRecord<String, String> consumer(ConsumerRecord<String, String> consumerRecord) throws Exception {

    return consumerRecord;
  }

}
