package com.defonfw.starters.kafka.receiver;

import static org.hamcrest.Matchers.equalTo;

import javax.inject.Inject;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.defonfw.starters.kafka.receiver.app.MessageTestProcessor;
import com.defonfw.starters.kafka.receiver.app.TestApplication;
import com.devonfw.module.kafka.common.messaging.api.client.MessageSender;

/**
 * Tests that kafka sender is working in devon4j-starter-kafka-sender.
 *
 */
@ExtendWith(value = { SpringExtension.class })
@SpringBootTest(classes = TestApplication.class, webEnvironment = WebEnvironment.NONE)
@EmbeddedKafka(topics = TestKafkaReceiverSetup.TEST_TOPIC)
public class TestKafkaReceiverSetup {

  public static final String TEST_TOPIC = "test-topic";

  @Inject
  private MessageSender<String, String> messageSender;

  @Inject
  private MessageTestProcessor messageProcessor;

  /**
   * Test receiving message with starter setup works
   *
   * @throws Exception if an error occurs.
   */
  @Test
  public void testUseMessageSender() throws Exception {

    // Arrange
    ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TEST_TOPIC, "Hello World!");

    // Act
    this.messageSender.sendMessageAndWait(producerRecord);

    // Assert
    Awaitility.await().until(() -> this.messageProcessor.getReceivedMessages().size(), equalTo(1));
  }

}
