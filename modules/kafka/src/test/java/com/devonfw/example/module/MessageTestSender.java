package com.devonfw.example.module;

import java.util.Arrays;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devonfw.example.TestApplication;
import com.devonfw.example.base.TestTopicNameGenerator;
import com.devonfw.example.base.to.SampleUserTestPojo;
import com.devonfw.module.kafka.common.messaging.api.client.MessageSender;
import com.devonfw.module.test.common.base.ComponentTest;

/**
 * A test class to test the methods of {@link MessageSender}.
 */
@ExtendWith(value = { SpringExtension.class })
@SpringBootTest(classes = { TestApplication.class }, webEnvironment = WebEnvironment.NONE)
@DirtiesContext
@TestInstance(Lifecycle.PER_CLASS)
public class MessageTestSender extends ComponentTest {

  private static final String GROUP = "test-group";

  @Autowired
  private MessageSender<Object, Object> messageSender;

  /**
   * The generated topic names.
   */
  public static String[] testTopicNames = TestTopicNameGenerator.generateTopicNames(1);

  @Autowired
  private EmbeddedKafkaBroker embeddedKafka;

  private Consumer<String, String> consumer;

  private ProducerRecord<Object, Object> producerRecord;

  @Override
  @BeforeAll
  protected void doSetUp() {

    // Arrange
    SampleUserTestPojo payload = new SampleUserTestPojo("ashwin", "9876543210", "India");

    this.producerRecord = new ProducerRecord<>(testTopicNames[0], 0, "testKey", payload.toString());

    Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps(GROUP, "true", this.embeddedKafka);
    consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.embeddedKafka.getBrokersAsString());
    consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    // create a Kafka consumer factory
    DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProperties,
        new StringDeserializer(), new StringDeserializer());

    this.consumer = consumerFactory.createConsumer();
    this.consumer.subscribe(Arrays.asList(testTopicNames[0]));
  }

  @Override
  @AfterAll
  protected void doTearDown() {

    this.consumer.close();
  }

  /**
   * Test method to test the
   * {@link MessageSender#sendMessage(ProducerRecord, com.devonfw.module.kafka.common.messaging.api.client.converter.MessageConverter)}
   * from {@link MessageSender}.
   */
  @Test
  public void shouldSendMessageToKafKaBroker_whenProducerRecordIsGiven() {

    // Act
    this.messageSender.sendMessage(this.producerRecord);

    // Assert
    ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(this.consumer);

    assertThat(records).isNotEmpty();

    Iterable<ConsumerRecord<String, String>> messageOfGivenTopic = records.records(testTopicNames[0]);
    assertThat(messageOfGivenTopic).isNotEmpty();

    messageOfGivenTopic.forEach(record -> {
      assertThat(record).isNotNull();
      assertThat(record.topic()).isEqualTo(this.producerRecord.topic());
      assertThat(record.value()).isEqualTo(this.producerRecord.value());
      assertThat(record.key()).isEqualTo(this.producerRecord.key());
    });

  }

  /**
   * Test method to test the
   * {@link MessageSender#sendMessageAndWait(ProducerRecord, com.devonfw.module.kafka.common.messaging.api.client.converter.MessageConverter)}
   * from {@link MessageSender}.
   *
   * @throws Exception the {@link Exception}.
   */
  @Test
  public void shouldSendMessageToKafkaBrokerAndWaitTillTimeout_whenproducerRecordIsGiven() throws Exception {

    // Act
    this.messageSender.sendMessageAndWait(this.producerRecord);

    // Assert
    ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(this.consumer);

    assertThat(records).isNotEmpty();

    Iterable<ConsumerRecord<String, String>> messageOfGivenTopic = records.records(testTopicNames[0]);
    assertThat(messageOfGivenTopic).isNotEmpty();

    messageOfGivenTopic.forEach(record -> {
      assertThat(record).isNotNull();
      assertThat(record.topic()).isEqualTo(this.producerRecord.topic());
      assertThat(record.value()).isEqualTo(this.producerRecord.value());
      assertThat(record.key()).isEqualTo(this.producerRecord.key());
    });
  }

  /**
   * Test method to test the
   * {@link MessageSender#sendMessageAndWait(ProducerRecord, com.devonfw.module.kafka.common.messaging.api.client.converter.MessageConverter,int)}
   * from {@link MessageSender}.
   *
   * @throws Exception the {@link Exception}.
   */
  @Test
  public void shouldSendMessageToKafkaBrokerAndWaitTillTimeout_whenProducerRecordAndTimoutIsGiven() throws Exception {

    // Act
    this.messageSender.sendMessageAndWait(this.producerRecord, 30);

    // Assert
    ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(this.consumer);

    assertThat(records).isNotEmpty();

    Iterable<ConsumerRecord<String, String>> messageOfGivenTopic = records.records(testTopicNames[0]);
    assertThat(messageOfGivenTopic).isNotEmpty();

    messageOfGivenTopic.forEach(record -> {
      assertThat(record).isNotNull();
      assertThat(record.topic()).isEqualTo(this.producerRecord.topic());
      assertThat(record.value()).isEqualTo(this.producerRecord.value());
      assertThat(record.key()).isEqualTo(this.producerRecord.key());
    });

  }

}
