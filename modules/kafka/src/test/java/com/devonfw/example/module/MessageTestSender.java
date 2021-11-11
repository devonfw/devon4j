package com.devonfw.example.module;

import java.util.UUID;

import javax.inject.Inject;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import com.devonfw.module.kafka.common.messaging.api.client.MessageSender;

/**
 * A test class to test the methods of {@link MessageSender}.
 */
public class MessageTestSender extends AbstractKafkaBaseTest {

  @Inject
  private MessageSender<String, String> messageSender;

  @Inject
  private ConsumerFactory<String, String> consumerFactory;

  @Inject
  private EmbeddedKafkaBroker embeddedKafka;

  private Consumer<String, String> consumer;

  private ProducerRecord<String, String> producerRecord;

  @Override
  protected void doSetUp() {

    // Arrange
    this.producerRecord = new ProducerRecord<>(AbstractKafkaBaseTest.TEST_TOPIC_1, 0, UUID.randomUUID().toString(),
        "Hello World!");

    this.consumer = this.consumerFactory.createConsumer(AbstractKafkaBaseTest.TEST_GROUP, "test");
    this.embeddedKafka.consumeFromAnEmbeddedTopic(this.consumer, AbstractKafkaBaseTest.TEST_TOPIC_1);
  }

  @Override
  protected void doTearDown() {

    this.consumer.commitSync();
    this.consumer.close();
  }

  /**
   * Test method to test the {@link MessageSender#sendMessage(ProducerRecord)} from {@link MessageSender}.
   */
  @Test
  public void shouldSendMessageToKafKaBroker_whenProducerRecordIsGiven() {

    // Act
    this.messageSender.sendMessage(this.producerRecord);

    // Assert
    ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(this.consumer,
        AbstractKafkaBaseTest.TEST_TOPIC_1);

    assertThat(record).isNotNull();
    assertThat(record.topic()).isEqualTo(this.producerRecord.topic());
    assertThat(record.value()).isEqualTo(this.producerRecord.value());
    assertThat(record.key()).isEqualTo(this.producerRecord.key());

  }

  /**
   * Test method to test the {@link MessageSender#sendMessageAndWait(ProducerRecord)} from {@link MessageSender}.
   *
   * @throws Exception the {@link Exception}.
   */
  @Test
  public void shouldSendMessageToKafkaBrokerAndWaitTillTimeout_whenproducerRecordIsGiven() throws Exception {

    // Act
    this.messageSender.sendMessageAndWait(this.producerRecord);

    // Assert
    ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(this.consumer,
        AbstractKafkaBaseTest.TEST_TOPIC_1);

    assertThat(record).isNotNull();
    assertThat(record.topic()).isEqualTo(this.producerRecord.topic());
    assertThat(record.value()).isEqualTo(this.producerRecord.value());
    assertThat(record.key()).isEqualTo(this.producerRecord.key());

  }

  /**
   * Test method to test the {@link MessageSender#sendMessageAndWait(ProducerRecord, int)} from {@link MessageSender}.
   *
   * @throws Exception the {@link Exception}.
   */
  @Test
  public void shouldSendMessageToKafkaBrokerAndWaitTillTimeout_whenProducerRecordAndTimoutIsGiven() throws Exception {

    // Act
    this.messageSender.sendMessageAndWait(this.producerRecord, 30);

    // Assert
    ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(this.consumer,
        AbstractKafkaBaseTest.TEST_TOPIC_1);

    assertThat(record).isNotNull();
    assertThat(record.topic()).isEqualTo(this.producerRecord.topic());
    assertThat(record.value()).isEqualTo(this.producerRecord.value());
    assertThat(record.key()).isEqualTo(this.producerRecord.key());

  }

}
