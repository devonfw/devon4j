package com.devonfw.module.security.jwt.common.base.kafka;

import static org.hamcrest.Matchers.equalTo;

import javax.inject.Inject;

import org.apache.commons.codec.Charsets;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.aspectj.lang.ProceedingJoinPoint;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devonfw.module.kafka.common.messaging.api.client.MessageSender;
import com.devonfw.module.security.jwt.common.api.JwtComponentTest;
import com.devonfw.module.security.jwt.common.base.JwtConstants;

/**
 *
 */
@ExtendWith(value = { MockitoExtension.class, SpringExtension.class })
@EmbeddedKafka(topics = JwtTokenValidationAspectTest.TEST_TOPIC)
public class JwtTokenValidationAspectTest extends JwtComponentTest {

  /**
   *
   */
  public static final String TEST_TOPIC = "jwt-test";

  @Inject
  private MessageSender<String, String> messageSender;

  @Inject
  private MessageTestProcessor messageProcessor;

  @Inject
  private JwtTokenValidationAspect jwtTokenValidationAspect;

  @Mock
  private ProceedingJoinPoint call;

  @Mock
  private JwtAuthentication jwtAuthentication;

  private ConsumerRecord<String, String> kafkaRecord;

  /**
   * Test receiving message with starter setup works
   *
   * @throws Exception if an error occurs.
   */
  @Test
  public void shouldValidate_whenInValidateTokenIsPassed() throws Exception {

    // Arrange
    ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TEST_TOPIC, "Hello World!");
    Headers headers = producerRecord.headers();
    headers.add(JwtConstants.HEADER_AUTHORIZATION, TEST_JWT.getBytes(Charsets.UTF_8));

    adjustClock();

    // Act
    this.messageSender.sendMessageAndWait(producerRecord);

    // Assert
    Awaitility.await().until(() -> this.messageProcessor.getReceivedMessages().size(), equalTo(1));

    resetClock();
  }

  /**
   * @throws Exception
   *
   */
  @Test
  public void shouldValidate_whenInvalidTokenIsPassed() throws Exception {

    // Arrange
    ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TEST_TOPIC, "Hello World!");
    Headers headers = producerRecord.headers();
    headers.add(JwtConstants.HEADER_AUTHORIZATION, INVALID_TEST_JWT.getBytes(Charsets.UTF_8));

    adjustClock();

    // Act
    this.messageSender.sendMessageAndWait(producerRecord);

    // Assert
    Awaitility.await().until(() -> this.messageProcessor.getReceivedMessages().size(), equalTo(0));

    resetClock();
  }

  /**
   * @throws Exception
   */
  @Test
  public void shouldValidateAndThrowMissingTokenException_whenTokenIsNotPassed() throws Exception {

    // Arrange
    ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TEST_TOPIC, "Hello World!");

    Mockito.when(this.jwtAuthentication.failOnMissingToken()).thenReturn(true);

    adjustClock();

    // Act
    this.messageSender.sendMessageAndWait(producerRecord);

    // Assert
    // Awaitility.await().until(() -> this.messageProcessor.getReceivedMessages().size(), equalTo(0));
    Awaitility.await().untilAsserted(() -> Assertions.assertThrows(MissingTokenException.class,
        () -> this.jwtTokenValidationAspect.authenticateToken(this.call, this.jwtAuthentication, this.kafkaRecord)));

    resetClock();

  }
}
