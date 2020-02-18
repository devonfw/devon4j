package com.devonfw.example.module;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
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

import com.devonfw.example.base.TestTopicNameGenerator;
import com.devonfw.example.base.config.TestMessageSenderConfig;
import com.devonfw.example.base.to.SampleUserTestPojo;
import com.devonfw.module.kafka.common.messaging.api.Message;
import com.devonfw.module.kafka.common.messaging.api.client.MessageSender;
import com.devonfw.module.test.common.base.ComponentTest;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author ravicm
 *
 */
@ExtendWith(value = { SpringExtension.class })
@SpringBootTest(classes = { TestMessageSenderConfig.class }, webEnvironment = WebEnvironment.NONE)
@DirtiesContext
@TestInstance(Lifecycle.PER_CLASS)
public class MessageTestSender extends ComponentTest {

  private static final String GROUP = "test-group";

  @Autowired
  private MessageSender messageSender;

  /**
   *
   */
  public static String[] testTopicNames = TestTopicNameGenerator.generateTopicNames(1);

  private Message<SampleUserTestPojo> message;

  @Autowired
  private EmbeddedKafkaBroker embeddedKafka;

  private Consumer<String, String> consumer;

  @Override
  @BeforeAll
  protected void doSetUp() {

    SampleUserTestPojo payload = new SampleUserTestPojo("ashwin", "9876543210", "India");

    this.message = new Message<>();
    this.message.setPayload(payload);
    this.message.setTopic(testTopicNames[0]);

    Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps(GROUP, "true", this.embeddedKafka);
    consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.embeddedKafka.getBrokersAsString());
    consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    // create a Kafka consumer factory
    DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProperties,
        new StringDeserializer(), new StringDeserializer());

    this.consumer = consumerFactory.createConsumer();
    this.consumer.subscribe(List.of(testTopicNames[0]));

  }

  @Override
  @AfterAll
  protected void doTearDown() {

    this.consumer.close();
  }

  /**
   * @throws JsonProcessingException exception thrown while parsing message.
   *
   */
  @Test
  public void shouldSendMessageToKafKaBroker_whenTopicPartitionAndMessageIsGiven() throws JsonProcessingException {

    // Act
    this.messageSender.sendMessage(this.message.getTopic(), 0, this.message);

    // Assert

    ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(this.consumer);

    assertThat(records).isNotEmpty();

    Iterable<ConsumerRecord<String, String>> messageOfGivenTopic = records.records(testTopicNames[0]);
    assertThat(messageOfGivenTopic).isNotEmpty();

    messageOfGivenTopic.forEach(record -> {
      assertThat(record).isNotNull();
      assertThat(record.topic()).isEqualTo(this.message.getTopic());
    });

  }

  /**
   * @throws JsonProcessingException
   */
  @Test
  public void shouldSendMessageToKafkaBroker_whenMessageAndTopicIsGiven() throws JsonProcessingException {

    // Act
    this.messageSender.sendMessage(this.message.getTopic(), this.message);

    // Assert
    ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(this.consumer);

    assertThat(records).isNotEmpty();

    Iterable<ConsumerRecord<String, String>> messageOfGivenTopic = records.records(testTopicNames[0]);
    assertThat(messageOfGivenTopic).isNotEmpty();

    messageOfGivenTopic.forEach(record -> {
      assertThat(record).isNotNull();
      assertThat(record.topic()).isEqualTo(this.message.getTopic());
    });

  }

  /**
   * @throws JsonProcessingException
   */
  @Test
  public void shouldSendMessageToKafkaBroker_whenTopicAndKeyAndMessageIsGiven() throws JsonProcessingException {

    // Act
    this.messageSender.sendMessage(this.message.getTopic(), "key", this.message);

    // Assert
    ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(this.consumer);

    assertThat(records).isNotEmpty();

    Iterable<ConsumerRecord<String, String>> messageOfGivenTopic = records.records(testTopicNames[0]);
    assertThat(messageOfGivenTopic).isNotEmpty();

    messageOfGivenTopic.forEach(record -> {
      assertThat(record).isNotNull();
      assertThat(record.topic()).isEqualTo(this.message.getTopic());
    });

  }

  /**
   * @throws Exception
   */
  @Test
  public void shouldSendMessageToKafkaBrokerAndWaitTillTimeout_whenMessageAndTopicIsGiven() throws Exception {

    // Act
    this.messageSender.sendMessageAndWait(this.message.getTopic(), this.message);

    // Assert
    ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(this.consumer);

    assertThat(records).isNotEmpty();

    Iterable<ConsumerRecord<String, String>> messageOfGivenTopic = records.records(testTopicNames[0]);
    assertThat(messageOfGivenTopic).isNotEmpty();

    messageOfGivenTopic.forEach(record -> {
      assertThat(record).isNotNull();
      assertThat(record.topic()).isEqualTo(this.message.getTopic());
    });

  }

  /**
   * @throws Exception
   */
  @Test
  public void shouldSendMessageToKafkaBrokerAndWaitTillTimeout_whenMessageAndTopicAndPartitionIsGiven()
      throws Exception {

    // Act
    this.messageSender.sendMessageAndWait(this.message.getTopic(), 0, this.message);

    // Assert
    ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(this.consumer);

    assertThat(records).isNotEmpty();

    Iterable<ConsumerRecord<String, String>> messageOfGivenTopic = records.records(testTopicNames[0]);
    assertThat(messageOfGivenTopic).isNotEmpty();

    messageOfGivenTopic.forEach(record -> {
      assertThat(record).isNotNull();
      assertThat(record.topic()).isEqualTo(this.message.getTopic());
    });

  }

  /**
   * @throws Exception
   */
  @Test
  public void shouldSendMessageToKafkaBrokerAndWaitTillTimeout_whenMessageAndTopicAndTimeoutIsGiven() throws Exception {

    // Act
    this.messageSender.sendMessageAndWait(this.message.getTopic(), this.message, 100);

    // Assert
    ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(this.consumer);

    assertThat(records).isNotEmpty();

    Iterable<ConsumerRecord<String, String>> messageOfGivenTopic = records.records(testTopicNames[0]);
    assertThat(messageOfGivenTopic).isNotEmpty();

    messageOfGivenTopic.forEach(record -> {
      assertThat(record).isNotNull();
      assertThat(record.topic()).isEqualTo(this.message.getTopic());
    });

  }

  /**
   * @throws Exception
   */
  @Test
  public void shouldSendMessageToKafkaBrokerAndWaitTillTimeout_whenMessageAndTopicAndKeyIsGiven() throws Exception {

    // Act
    this.messageSender.sendMessageAndWait(this.message.getTopic(), "key", this.message);

    // Assert
    ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(this.consumer);

    assertThat(records).isNotEmpty();

    Iterable<ConsumerRecord<String, String>> messageOfGivenTopic = records.records(testTopicNames[0]);
    assertThat(messageOfGivenTopic).isNotEmpty();

    messageOfGivenTopic.forEach(record -> {
      assertThat(record).isNotNull();
      assertThat(record.topic()).isEqualTo(this.message.getTopic());
    });

  }

  /**
   * @throws Exception
   */
  @Test
  public void shouldSendMessageToKafkaBrokerAndWaitTillTimeout_whenMessageAndTopicAndPartionAndTimeoutIsGiven()
      throws Exception {

    // Act
    this.messageSender.sendMessageAndWait(this.message.getTopic(), 0, this.message, 100);

    // Assert
    ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(this.consumer);

    assertThat(records).isNotEmpty();

    Iterable<ConsumerRecord<String, String>> messageOfGivenTopic = records.records(testTopicNames[0]);
    assertThat(messageOfGivenTopic).isNotEmpty();

    messageOfGivenTopic.forEach(record -> {
      assertThat(record).isNotNull();
      assertThat(record.topic()).isEqualTo(this.message.getTopic());
    });

  }

  /**
   * @throws Exception
   */
  @Test
  public void shouldSendMessageToKafkaBrokerAndWaitTillTimeout_whenMessageAndTopicAndKeyAndTimeoutIsGiven()
      throws Exception {

    // Act
    this.messageSender.sendMessageAndWait(this.message.getTopic(), "key", this.message, 100);

    // Assert
    ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(this.consumer);

    assertThat(records).isNotEmpty();

    Iterable<ConsumerRecord<String, String>> messageOfGivenTopic = records.records(testTopicNames[0]);
    assertThat(messageOfGivenTopic).isNotEmpty();

    messageOfGivenTopic.forEach(record -> {
      assertThat(record).isNotNull();
      assertThat(record.topic()).isEqualTo(this.message.getTopic());
    });

  }

}
