package com.devonfw.example.base.config;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.devonfw.module.logging.common.api.DiagnosticContextFacade;
import com.devonfw.module.logging.common.api.LoggingConstants;
import com.devonfw.module.logging.common.impl.DiagnosticContextFacadeImpl;

import brave.Tracer;
import brave.Tracing;
import kafka.server.KafkaConfig;

/**
 * @author ravicm
 *
 */
@Configuration
public class TestMessageSenderConfig {

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
  public KafkaCommonProperties testMessageKafkaCommonProperties() throws IOException {

    KafkaCommonProperties kafkaCommonProperties = new KafkaCommonProperties();
    kafkaCommonProperties.setBootstrapServers(broker().getBrokersAsString());
    kafkaCommonProperties.setClientId("test-clientId");
    return kafkaCommonProperties;
  }

  /**
   * @return
   */
  public MessageLoggingSupport testMessageLoggingSupport() {

    return new MessageLoggingSupport();
  }

  /**
   * @return
   */
  public KafkaHealthIndicatorProperties testMessageKafkaHealthIndicatorProperties() {

    return new KafkaHealthIndicatorProperties();
  }

  /**
   * @param testMessageKafkaHealthIndicatorProperties
   * @param testMessageKafkaCommonProperties
   * @param testKafkaConsumerProperty
   * @return
   * @throws IOException
   */
  @Bean
  public KafkaHealthIndicator kafkaHealthIndicator() throws IOException {

    return new KafkaHealthIndicator(createConsumerFactory(), testMessageKafkaHealthIndicatorProperties());
  }

  /**
   * @return
   */
  public KafkaConsumerProperties testKafkaConsumerProperty() {

    KafkaConsumerProperties kafkaConsumerProperties = new KafkaConsumerProperties();
    kafkaConsumerProperties.setGroupId("test-group");
    kafkaConsumerProperties.setKeyDeserializer("org.apache.kafka.common.serialization.StringDeserializer");
    kafkaConsumerProperties.setValueDeserializer("org.apache.kafka.common.serialization.StringDeserializer");
    kafkaConsumerProperties.setMaxPollIntervalMs(Integer.MAX_VALUE);
    return kafkaConsumerProperties;

  }

  /**
   * @param testMessageKafkaCommonProperties
   * @param testKafkaConsumerProperty
   * @return
   * @throws IOException
   */
  public ConsumerFactory<Object, Object> createConsumerFactory() throws IOException {

    KafkaPropertyMapper mapper = new KafkaPropertyMapper();
    return new DefaultKafkaConsumerFactory<>(
        mapper.consumerProperties(testMessageKafkaCommonProperties(), testKafkaConsumerProperty()));
  }

  /**
   * @return
   */
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
  public MessageSenderProperties testMessageSenderProperties() {

    return new MessageSenderProperties();
  }

  /**
   * @param testMessageKafkaCommonProperties
   * @param testMessageKafkaProducerProperties
   * @return
   * @throws IOException
   */
  @Bean
  public ProducerFactory<Object, Object> testMessageKafkaProducerFactory() throws IOException {

    return createProducerFactory();
  }

  /**
   * @param testMessageKafkaCommonProperties
   * @param testMessageKafkaProducerProperties
   * @return
   * @throws IOException
   */
  public ProducerFactory<Object, Object> createProducerFactory() throws IOException {

    KafkaPropertyMapper mapper = new KafkaPropertyMapper();
    return new DefaultKafkaProducerFactory<>(
        mapper.producerProperties(testMessageKafkaCommonProperties(), testMessageKafkaProducerProperties()));
  }

  /**
   * @param tracer
   * @return
   * @throws IOException
   */
  @Bean
  public KafkaTemplate<Object, Object> testMessageKafkaTemplate(@Autowired(required = false) Tracer tracer)
      throws IOException {

    return createKafkaTemplate(testMessageProducerLoggingListener(testMessageLoggingSupport(), tracer),
        testMessageKafkaProducerFactory());
  }

  /**
   * @param producerLogListener
   * @param producerFactory
   * @return
   * @throws IOException
   */
  public KafkaTemplate<Object, Object> createKafkaTemplate(ProducerLoggingListener<Object, Object> producerLogListener,
      ProducerFactory<Object, Object> producerFactory) throws IOException {

    KafkaTemplate<Object, Object> template = new KafkaTemplate<>(createProducerFactory());
    template.setProducerListener(producerLogListener);
    return template;
  }

  /**
   * @param tracer
   * @return
   * @throws IOException
   */
  @Bean
  public MessageSenderImpl testMessageSender(/* @Autowired(required = true) Tracer tracer */) throws IOException {

    MessageSenderImpl bean = new MessageSenderImpl();
    bean.setKafkaTemplate(testMessageKafkaTemplate(tracer()));
    bean.setLoggingSupport(testMessageLoggingSupport());
    bean.setSenderProperties(testMessageSenderProperties());
    bean.setSpanInjector(messageSpanInjector(testDiagnosticContextFacade()));
    bean.setDiagnosticContextFacade(testDiagnosticContextFacade());
    bean.setTracer(tracer());
    return bean;
  }

  public Tracer tracer() {

    return Tracing.currentTracer();
  }

  /**
   * @param diagnosticContextFacade
   * @return
   */
  public MessageSpanInjector messageSpanInjector(DiagnosticContextFacade diagnosticContextFacade) {

    MessageSpanInjector messageSpanInjector = new MessageSpanInjector();
    messageSpanInjector.setDiagnosticContextFacade(diagnosticContextFacade);
    return messageSpanInjector;
  }

  /**
   * @param testMessageLoggingSupport
   * @param tracer
   * @return
   */
  public ProducerLoggingListener<Object, Object> testMessageProducerLoggingListener(
      MessageLoggingSupport testMessageLoggingSupport, @Autowired(required = false) Tracer tracer) {

    return new ProducerLoggingListener<>(testMessageLoggingSupport(), tracer);
  }

  /**
   * Creates instantiation for {@link DiagnosticContextFacadeImpl} and used for setting and retrieving correlation-id
   * from {@link LoggingConstants}.
   *
   * @return DiagnosticContextFacadeImpl.
   */
  public DiagnosticContextFacade testDiagnosticContextFacade() {

    return new DiagnosticContextFacadeImpl();
  }
}
