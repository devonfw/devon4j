package com.devonfw.example.base.config;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import com.devonfw.module.kafka.common.messaging.api.config.KafkaCommonProperties;
import com.devonfw.module.kafka.common.messaging.api.config.KafkaConsumerProperties;
import com.devonfw.module.kafka.common.messaging.api.config.KafkaProducerProperties;
import com.devonfw.module.kafka.common.messaging.api.config.MessageSenderProperties;
import com.devonfw.module.kafka.common.messaging.health.api.config.KafkaHealthIndicatorProperties;
import com.devonfw.module.kafka.common.messaging.health.impl.KafkaHealthIndicator;
import com.devonfw.module.kafka.common.messaging.impl.MessageSenderImpl;
import com.devonfw.module.kafka.common.messaging.logging.impl.MessageLoggingSupport;
import com.devonfw.module.kafka.common.messaging.logging.impl.ProducerLoggingListener;
import com.devonfw.module.kafka.common.messaging.trace.impl.MessageSpanInjector;
import com.devonfw.module.kafka.common.messaging.util.KafkaPropertyMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import brave.Tracer;
import kafka.server.KafkaConfig;

/**
 * @author ravicm
 *
 */
@Configuration
public class TestMessageSenderConfig {

  public static int port;

  /**
   * @return
   * @throws IOException
   */
  @Bean
  public EmbeddedKafkaBroker broker() throws IOException {

    return new EmbeddedKafkaBroker(1, false, 2).brokerProperties(
        Collections.singletonMap(KafkaConfig.LogDirProp(), Files.createTempDirectory("kafka-test").toString()));
  }

  /**
   * @return
   * @throws IOException
   */
  @Bean
  public KafkaCommonProperties testMessageKafkaCommonProperties() throws IOException {

    KafkaCommonProperties kafkaCommonProperties = new KafkaCommonProperties();
    kafkaCommonProperties.setBootstrapServers(broker().getBrokersAsString());
    kafkaCommonProperties.setClientId("test-clientId");
    return kafkaCommonProperties;
  }

  /**
   * @return
   */
  @Bean
  public MessageLoggingSupport testMessageLoggingSupport() {

    return new MessageLoggingSupport();
  }

  /**
   * @return
   */
  @Bean
  public KafkaHealthIndicatorProperties testMessageKafkaHealthIndicatorProperties() {

    return new KafkaHealthIndicatorProperties();
  }

  /**
   * @param testMessageKafkaHealthIndicatorProperties
   * @param testMessageKafkaCommonProperties
   * @param testKafkaConsumerProperty
   * @return
   */
  @Bean
  public KafkaHealthIndicator kafkaHealthIndicator(
      @Qualifier("testMessageKafkaHealthIndicatorProperties") KafkaHealthIndicatorProperties testMessageKafkaHealthIndicatorProperties,
      @Qualifier("testMessageKafkaCommonProperties") KafkaCommonProperties testMessageKafkaCommonProperties,
      @Qualifier("testKafkaConsumerProperty") KafkaConsumerProperties testKafkaConsumerProperty) {

    return new KafkaHealthIndicator(createConsumerFactory(testMessageKafkaCommonProperties, testKafkaConsumerProperty),
        testMessageKafkaHealthIndicatorProperties);
  }

  /**
   * @return
   */
  @Bean
  public KafkaConsumerProperties testKafkaConsumerProperty() {

    KafkaConsumerProperties kafkaConsumerProperties = new KafkaConsumerProperties();
    kafkaConsumerProperties.setGroupId("test-group");
    kafkaConsumerProperties.setKeyDeserializer("org.apache.kafka.common.serialization.StringDeserializer");
    kafkaConsumerProperties.setValueDeserializer("org.apache.kafka.common.serialization.StringDeserializer");
    kafkaConsumerProperties.setMaxPollIntervalMs(Integer.MAX_VALUE);
    return kafkaConsumerProperties;

  }

  /**
   * @param kafkaCommonProperties
   * @param kafkaConsumerProperties
   * @return
   */
  public static ConsumerFactory<String, String> createConsumerFactory(KafkaCommonProperties kafkaCommonProperties,
      KafkaConsumerProperties kafkaConsumerProperties) {

    KafkaPropertyMapper mapper = new KafkaPropertyMapper();
    return new DefaultKafkaConsumerFactory<>(mapper.consumerProperties(kafkaCommonProperties, kafkaConsumerProperties));
  }

  /**
   * @return
   */
  @Bean
  public KafkaProducerProperties testMessageKafkaProducerProperties() {

    KafkaProducerProperties kafkaProducerproperties = new KafkaProducerProperties();
    kafkaProducerproperties.setKeySerializer("org.apache.kafka.common.serialization.StringSerializer");
    kafkaProducerproperties.setValueSerializer("org.apache.kafka.common.serialization.StringSerializer");
    kafkaProducerproperties.setRetries(10);
    return kafkaProducerproperties;
  }

  /**
   * @return
   */
  @Bean
  public MessageSenderProperties testMessageSenderProperties() {

    return new MessageSenderProperties();
  }

  /**
   * @param testMessageKafkaCommonProperties
   * @param testMessageKafkaProducerProperties
   * @return
   */
  @Bean
  public ProducerFactory<String, String> testMessageKafkaProducerFactory(
      @Qualifier("testMessageKafkaCommonProperties") KafkaCommonProperties testMessageKafkaCommonProperties,
      @Qualifier("testMessageKafkaProducerProperties") KafkaProducerProperties testMessageKafkaProducerProperties) {

    return createProducerFactory(testMessageKafkaCommonProperties, testMessageKafkaProducerProperties);
  }

  /**
   * @param kafkaCommonProperties common properties for kafka.
   * @param kafkaProducerProperties
   * @return
   */
  public static ProducerFactory<String, String> createProducerFactory(KafkaCommonProperties kafkaCommonProperties,
      KafkaProducerProperties kafkaProducerProperties) {

    KafkaPropertyMapper mapper = new KafkaPropertyMapper();
    return new DefaultKafkaProducerFactory<>(mapper.producerProperties(kafkaCommonProperties, kafkaProducerProperties));
  }

  //
  /**
   * @param testMessageProducerLoggingListener
   * @param testMessageKafkaProducerFactory
   * @return
   */
  @Bean
  public KafkaTemplate<String, String> testMessageKafkaTemplate(
      @Qualifier("testMessageProducerLoggingListener") ProducerLoggingListener testMessageProducerLoggingListener,
      @Qualifier("testMessageKafkaProducerFactory") ProducerFactory<String, String> testMessageKafkaProducerFactory) {

    return createKafkaTemplate(testMessageProducerLoggingListener, testMessageKafkaProducerFactory);
  }

  /**
   * @param producerLogListener
   * @param producerFactory
   * @return
   */
  public static KafkaTemplate<String, String> createKafkaTemplate(ProducerLoggingListener producerLogListener,
      ProducerFactory<String, String> producerFactory) {

    KafkaTemplate<String, String> template = new KafkaTemplate<>(producerFactory);
    template.setProducerListener(producerLogListener);
    return template;
  }

  /**
   * @return
   */
  @Bean
  public ObjectMapper testObjectMapper() {

    return new ObjectMapper();
  }

  /**
   * @param testObjectMapper
   * @param testMessageKafkaTemplate
   * @param testMessageLoggingSupport
   * @param testMessageSenderProperties
   * @param spanInjector
   * @param tracer
   * @return
   */
  @Bean
  public MessageSenderImpl testMessageSender(@Qualifier("testObjectMapper") ObjectMapper testObjectMapper,
      @Qualifier("testMessageKafkaTemplate") KafkaTemplate<String, String> testMessageKafkaTemplate,
      @Qualifier("testMessageLoggingSupport") MessageLoggingSupport testMessageLoggingSupport,
      @Qualifier("testMessageSenderProperties") MessageSenderProperties testMessageSenderProperties,
      @Autowired(required = false) MessageSpanInjector spanInjector, @Autowired(required = false) Tracer tracer) {

    MessageSenderImpl bean = new MessageSenderImpl();
    bean.setJacksonMapper(testObjectMapper);
    bean.setKafkaTemplate(testMessageKafkaTemplate);
    bean.setLoggingSupport(testMessageLoggingSupport);
    bean.setSenderProperties(testMessageSenderProperties);
    bean.setSpanInjector(spanInjector);
    bean.setTracer(tracer);
    return bean;
  }

  /**
   * @param messageLoggingSupport
   * @param tracer
   * @return
   */
  @Bean
  public ProducerLoggingListener testMessageProducerLoggingListener(MessageLoggingSupport messageLoggingSupport,
      @Autowired(required = false) Tracer tracer) {

    return new ProducerLoggingListener(messageLoggingSupport, tracer);
  }

}
