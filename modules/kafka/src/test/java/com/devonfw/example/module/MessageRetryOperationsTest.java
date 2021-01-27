package com.devonfw.example.module;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.devonfw.module.kafka.common.messaging.api.client.MessageSender;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageProcessor;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageRetryOperations;
import com.devonfw.module.kafka.common.messaging.retry.impl.MessageRetryContext;

/**
 * A junit class to check the retry pattern in {@link MessageRetryOperations}
 */
public class MessageRetryOperationsTest extends AbstractKafkaBaseTest {

  @Autowired
  private MessageRetryOperations<String, String> messageRetryOperations;

  @Autowired
  private MessageProcessor<String, String> processor;

  @Autowired
  private MessageSender<String, String> messageSender;

  private ConsumerRecord<String, String> consumerRecord;

  private ProducerRecord<String, String> producerRecord;

  @BeforeEach
  @Override
  protected void doSetUp() {

    this.consumerRecord = new ConsumerRecord<>(AbstractKafkaBaseTest.RETRY_TEST_TOPIC, 0, 0,
        AbstractKafkaBaseTest.RETRY_TEST_TOPIC, "message");
    Headers headers = this.consumerRecord.headers();
    headers.add(MessageRetryContext.RETRY_COUNT, "0".getBytes(StandardCharsets.UTF_8));
    headers.add(MessageRetryContext.RETRY_STATE, "Pending".getBytes(StandardCharsets.UTF_8));
    headers.add(MessageRetryContext.RETRY_NEXT, Instant.now().plus(1, ChronoUnit.MINUTES).toString().getBytes());
    headers.add(MessageRetryContext.RETRY_UNTIL, Instant.now().toString().getBytes());

    this.producerRecord = new ProducerRecord<>(this.consumerRecord.topic(), 0, AbstractKafkaBaseTest.RETRY_TEST_TOPIC,
        this.consumerRecord.value(), headers);

  }

  /**
   * This method is used to test the method
   * {@link MessageRetryOperations#processMessageWithRetry(ConsumerRecord, MessageProcessor) } whether it throws the
   * exception caused when the retry final fails.
   */
  @Test
  public void shouldProceedWithRetry_WhenExceptionIsThrownWhileProcessing() {

    // Arrange
    this.messageSender.sendMessage(this.producerRecord);

    // Assert
    Awaitility.await().untilAsserted(() -> Assertions.assertThrows(RuntimeException.class,
        () -> this.messageRetryOperations.processMessageWithRetry(this.consumerRecord, this.processor)));

  }

}
