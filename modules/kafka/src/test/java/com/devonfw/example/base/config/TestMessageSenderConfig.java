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

  /**
   *
   */
  // private String bootstrapServers = System.getProperty("test.messaging.kafka.common.bootstrapServers");

  // // @Autowired
  // private EmbeddedKafkaBroker embeddedKafkaBroker;
  //
  // /**
  // * @return embeddedKafkaBroker
  // */
  // public EmbeddedKafkaBroker getEmbeddedKafkaBroker() {
  //
  // return this.embeddedKafkaBroker;
  // }

  // /**
  // * @param embeddedKafkaBroker
  // */
  // public void setEmbeddedKafkaBroker(EmbeddedKafkaBroker embeddedKafkaBroker) {
  //
  // this.embeddedKafkaBroker = embeddedKafkaBroker;
  // }

  public static int port;

  /**
   * @return
   * @throws IOException
   */
  // @Bean
  // public static EmbeddedKafkaBroker broker() throws IOException {
  //
  // ServerSocket ss = ServerSocketFactory.getDefault().createServerSocket(0);
  // port = ss.getLocalPort();
  // ss.close();
  //
  // return new EmbeddedKafkaBroker(1, true, MessageTestSender.testTopicNames[0]).kafkaPorts(port);
  // }

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
  public KafkaCommonProperties xxxMessageKafkaCommonProperties() throws IOException {

    KafkaCommonProperties kafkaCommonProperties = new KafkaCommonProperties();
    kafkaCommonProperties.setBootstrapServers(broker().getBrokersAsString());
    kafkaCommonProperties.setClientId("test-clientId");
    return kafkaCommonProperties;
  }

  /**
   * @return
   */
  @Bean
  public MessageLoggingSupport xxxMessageLoggingSupport() {

    return new MessageLoggingSupport();
  }

  /**
   * @return
   */
  @Bean
  public KafkaHealthIndicatorProperties xxxMessageKafkaHealthIndicatorProperties() {

    return new KafkaHealthIndicatorProperties();
  }

  /**
   * @param xxxMessageKafkaHealthIndicatorProperties
   * @param xxxMessageKafkaCommonProperties
   * @param xxxKafkaConsumerProperty
   * @return
   */
  @Bean
  public KafkaHealthIndicator kafkaHealthIndicator(
      @Qualifier("xxxMessageKafkaHealthIndicatorProperties") KafkaHealthIndicatorProperties xxxMessageKafkaHealthIndicatorProperties,
      @Qualifier("xxxMessageKafkaCommonProperties") KafkaCommonProperties xxxMessageKafkaCommonProperties,
      @Qualifier("xxxKafkaConsumerProperty") KafkaConsumerProperties xxxKafkaConsumerProperty) {

    return new KafkaHealthIndicator(createConsumerFactory(xxxMessageKafkaCommonProperties, xxxKafkaConsumerProperty),
        xxxMessageKafkaHealthIndicatorProperties);
  }

  /**
   * @return
   */
  @Bean
  public KafkaConsumerProperties xxxKafkaConsumerProperty() {

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
  public KafkaProducerProperties xxxMessageKafkaProducerProperties() {

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
  public MessageSenderProperties xxxMessageSenderProperties() {

    return new MessageSenderProperties();
  }

  /**
   * @param xxxMessageKafkaCommonProperties
   * @param xxxMessageKafkaProducerProperties
   * @return
   */
  @Bean
  public ProducerFactory<String, String> xxxMessageKafkaProducerFactory(
      @Qualifier("xxxMessageKafkaCommonProperties") KafkaCommonProperties xxxMessageKafkaCommonProperties,
      @Qualifier("xxxMessageKafkaProducerProperties") KafkaProducerProperties xxxMessageKafkaProducerProperties) {

    return createProducerFactory(xxxMessageKafkaCommonProperties, xxxMessageKafkaProducerProperties);
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

  /**
   * @param xxxMessageProducerLoggingListener
   * @param xxxMessageKafkaProducerFactory
   * @return
   */
  @Bean
  public KafkaTemplate<String, String> xxxMessageKafkaTemplate(
      @Qualifier("xxxMessageProducerLoggingListener") ProducerLoggingListener xxxMessageProducerLoggingListener,
      @Qualifier("xxxMessageKafkaProducerFactory") ProducerFactory<String, String> xxxMessageKafkaProducerFactory) {

    return createKafkaTemplate(xxxMessageProducerLoggingListener, xxxMessageKafkaProducerFactory);
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
  public ObjectMapper xxxObjectMapper() {

    return new ObjectMapper();
  }

  /**
   * @param xxxObjectMapper
   * @param xxxMessageKafkaTemplate
   * @param xxxMessageLoggingSupport
   * @param xxxMessageSenderProperties
   * @param spanInjector
   * @param tracer
   * @return
   */
  @Bean
  public MessageSenderImpl xxxMessageSender(@Qualifier("xxxObjectMapper") ObjectMapper xxxObjectMapper,
      @Qualifier("xxxMessageKafkaTemplate") KafkaTemplate<String, String> xxxMessageKafkaTemplate,
      @Qualifier("xxxMessageLoggingSupport") MessageLoggingSupport xxxMessageLoggingSupport,
      @Qualifier("xxxMessageSenderProperties") MessageSenderProperties xxxMessageSenderProperties,
      @Autowired(required = false) MessageSpanInjector spanInjector, @Autowired(required = false) Tracer tracer) {

    MessageSenderImpl bean = new MessageSenderImpl();
    bean.setJacksonMapper(xxxObjectMapper);
    bean.setKafkaTemplate(xxxMessageKafkaTemplate);
    bean.setLoggingSupport(xxxMessageLoggingSupport);
    bean.setSenderProperties(xxxMessageSenderProperties);
    bean.setSpanInjector(spanInjector);
    bean.setTracer(tracer);
    return bean;
  }

  /**
   * @param xxxMessageLoggingSupport
   * @param tracer
   * @return
   */
  @Bean
  public ProducerLoggingListener xxxMessageProducerLoggingListener(MessageLoggingSupport xxxMessageLoggingSupport,
      @Autowired(required = false) Tracer tracer) {

    return new ProducerLoggingListener(xxxMessageLoggingSupport, tracer);
  }

}
