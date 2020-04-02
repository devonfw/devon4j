package com.defonfw.starters.kafka.sender;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devonfw.module.kafka.common.messaging.api.client.MessageSender;

/**
 * Tests that kafka sender is working in devon4j-starter-kafka-sender.
 *
 */
@ExtendWith(value = { SpringExtension.class })
@SpringBootTest(classes = TestApplication.class, webEnvironment = WebEnvironment.NONE)
@EmbeddedKafka
public class TestKafkaSenderSetup {

  @Autowired
  private MessageSender<String, String> messageSender;

  @Test
  /**
   * Test sending message with MessageSender does not throw an exception
   *
   * @throws Exception
   */
  public void testUseMessageSender() throws Exception {

    ProducerRecord<String, String> producerRecord = new ProducerRecord<>("test-topic", "Hello World!");
    this.messageSender.sendMessageAndWait(producerRecord);
  }

}
