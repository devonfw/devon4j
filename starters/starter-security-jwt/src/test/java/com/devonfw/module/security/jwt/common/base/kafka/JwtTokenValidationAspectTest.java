package com.devonfw.module.security.jwt.common.base.kafka;

import static org.hamcrest.Matchers.equalTo;

import java.nio.charset.Charset;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.aspectj.lang.ProceedingJoinPoint;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devonfw.module.security.jwt.common.api.JwtComponentTest;
import com.devonfw.module.security.jwt.common.base.JwtConstants;

/**
 * This class is to test the functionality of {@link JwtTokenValidationAspect} as an integrated test.
 */
@ExtendWith(value = { MockitoExtension.class, SpringExtension.class })
@EmbeddedKafka(topics = { JwtTokenValidationAspectTest.TEST_TOPIC, JwtTokenValidationAspectTest.TEST_TOPIC_2 })
@SpringBootTest(classes = TestApplication.class, webEnvironment = WebEnvironment.NONE)
@DirtiesContext
public class JwtTokenValidationAspectTest extends JwtComponentTest {

  /**
   * The test topic name.
   */
  public static final String TEST_TOPIC = "jwt-test";

  /**
   * The test topic name.
   */
  public static final String TEST_TOPIC_2 = "jwt-test-2";

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  private KafkaMessageListener messageListener;

  @Autowired
  private JwtTokenValidationAspect jwtTokenValidationAspect;

  @Mock
  private ProceedingJoinPoint call;

  @Mock
  private JwtAuthentication jwtAuthentication;

  /**
   * This method is used to clear the messages in the list.
   */
  @AfterEach
  public void removeMessages() {

    this.messageListener.getReceivedMessages().clear();
  }

  /**
   * This method is used to test
   * {@link JwtTokenValidationAspect#authenticateToken(ProceedingJoinPoint, JwtAuthentication, ConsumerRecord)} when the
   * valid token is passed and {@link JwtAuthentication#failOnMissingToken()} is true.
   *
   * @throws Exception if an error occurs.
   */
  @Test
  public void shouldValidate_whenInValidateTokenIsPassed() throws Exception {

    // Arrange
    ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TEST_TOPIC, "Hello World!");
    Headers headers = producerRecord.headers();
    headers.add(JwtConstants.HEADER_AUTHORIZATION, TEST_JWT.getBytes(Charset.forName("UTF-8")));

    adjustClock();

    // Act
    this.kafkaTemplate.send(producerRecord);

    // Assert
    Awaitility.await().until(() -> this.messageListener.getReceivedMessages().size(), equalTo(1));
  }

  /**
   * This method is used to test
   * {@link JwtTokenValidationAspect#authenticateToken(ProceedingJoinPoint, JwtAuthentication, ConsumerRecord)} when the
   * inValid token is passed and {@link JwtAuthentication#failOnMissingToken()} is true.
   *
   * @throws Exception if an error occurs.
   */
  @Test
  public void shouldValidate_whenInvalidTokenIsPassed() throws Exception {

    // Arrange
    ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TEST_TOPIC, "Hello World!");
    Headers headers = producerRecord.headers();

    // a inValid token but got expired.
    headers.add(JwtConstants.HEADER_AUTHORIZATION, INVALID_TEST_JWT.getBytes(Charset.forName("UTF-8")));

    adjustClock();

    // Act
    this.kafkaTemplate.send(producerRecord);

    // Assert
    Awaitility.await().until(() -> this.messageListener.getReceivedMessages().size(), equalTo(0));
  }

  /**
   * This method is used to test
   * {@link JwtTokenValidationAspect#authenticateToken(ProceedingJoinPoint, JwtAuthentication, ConsumerRecord)} when the
   * token is null or empty and {@link JwtAuthentication#failOnMissingToken()} is true.
   *
   * @throws Exception if an error occurs.
   */
  @Test
  public void shouldValidateAndThrowMissingTokenException_whenTokenIsNotPassed() throws Exception {

    // Arrange
    ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TEST_TOPIC, "Hello World!");

    ConsumerRecord<String, String> kafkaRecord = new ConsumerRecord<>(TEST_TOPIC, 0, 0, null, "Hello World!");

    // mock
    Mockito.when(this.jwtAuthentication.failOnMissingToken()).thenReturn(true);

    // Act
    this.kafkaTemplate.send(producerRecord);

    // Assert
    Awaitility.await().untilAsserted(() -> Assertions.assertThrows(MissingTokenException.class,
        () -> this.jwtTokenValidationAspect.authenticateToken(this.call, this.jwtAuthentication, kafkaRecord)));

  }

  /**
   * This method is used to test
   * {@link JwtTokenValidationAspect#authenticateToken(ProceedingJoinPoint, JwtAuthentication, ConsumerRecord)} when the
   * token is null or empty and {@link JwtAuthentication#failOnMissingToken()} is false.
   *
   * @throws Exception if an error occurs.
   */
  @Test
  public void shouldNotValidate_whenTheFailOnMissingTokenIsFalse() throws Exception {

    // Arrange
    ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TEST_TOPIC_2, "Hello World!");

    adjustClock();

    // Act
    this.kafkaTemplate.send(producerRecord);

    // Assert
    Awaitility.await().until(() -> this.messageListener.getReceivedMessages().size(), equalTo(1));
  }
}
