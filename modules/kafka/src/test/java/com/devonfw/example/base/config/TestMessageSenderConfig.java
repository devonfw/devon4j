package com.devonfw.example.base.config;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

import javax.inject.Inject;

import org.apache.kafka.common.errors.ProducerFencedException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import com.devonfw.module.kafka.common.messaging.api.client.MessageSender;
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
import kafka.server.KafkaConfig;

/**
 * A test configuration class for the {@link MessageSender}
 *
 */
@Configuration
public class TestMessageSenderConfig {

  @Inject
  private Tracer tracer;

  /**
   * Creates bean for the {@link EmbeddedKafkaBroker}
   *
   * @return the {@link EmbeddedKafkaBroker}
   * @throws IOException the {@link IOException}
   */
  @Bean
  public EmbeddedKafkaBroker broker() throws IOException {

    return new EmbeddedKafkaBroker(1, false, 2).brokerProperties(
        Collections.singletonMap(KafkaConfig.LogDirProp(), Files.createTempDirectory("kafka-test").toString()));
  }

  /**
   * The {@link KafkaCommonProperties}
   *
   * @return the kafkaCommonProperties
   * @throws IOException the {@link IOException}.
   */
  protected KafkaCommonProperties testMessageKafkaCommonProperties() throws IOException {

    KafkaCommonProperties kafkaCommonProperties = new KafkaCommonProperties();
    kafkaCommonProperties.setBootstrapServers(broker().getBrokersAsString());
    kafkaCommonProperties.setClientId("test-clientId");
    return kafkaCommonProperties;
  }

  /**
   * The {@link MessageLoggingSupport}
   *
   * @return MessageLoggingSupport.
   */
  protected MessageLoggingSupport testMessageLoggingSupport() {

    return new MessageLoggingSupport();
  }

  /**
   * The {@link KafkaHealthIndicatorProperties}
   *
   * @return KafkaHealthIndicatorProperties.
   */
  protected KafkaHealthIndicatorProperties testMessageKafkaHealthIndicatorProperties() {

    return new KafkaHealthIndicatorProperties();
  }

  /**
   * Creates bean for {@link KafkaHealthIndicator}.
   *
   * @return KafkaHealthIndicator.
   * @throws IOException the {@link IOException}.
   */
  @Bean
  public KafkaHealthIndicator kafkaHealthIndicator() throws IOException {

    return new KafkaHealthIndicator(createConsumerFactory(), testMessageKafkaHealthIndicatorProperties());
  }

  /**
   * The {@link KafkaConsumerProperties}.
   *
   * @return KafkaConsumerProperties.
   */
  protected KafkaConsumerProperties testKafkaConsumerProperty() {

    KafkaConsumerProperties kafkaConsumerProperties = new KafkaConsumerProperties();
    kafkaConsumerProperties.setGroupId("test-group");
    kafkaConsumerProperties.setKeyDeserializer("org.apache.kafka.common.serialization.StringDeserializer");
    kafkaConsumerProperties.setValueDeserializer("org.apache.kafka.common.serialization.StringDeserializer");
    kafkaConsumerProperties.setMaxPollIntervalMs(Integer.MAX_VALUE);
    return kafkaConsumerProperties;

  }

  /**
   * The {@link ConsumerFactory}
   *
   * @return ConsumerFactory
   * @throws IOException the {@link IOException}
   */
  protected ConsumerFactory<Object, Object> createConsumerFactory() throws IOException {

    KafkaPropertyMapper mapper = new KafkaPropertyMapper();
    return new DefaultKafkaConsumerFactory<>(
        mapper.consumerProperties(testMessageKafkaCommonProperties(), testKafkaConsumerProperty()));
  }

  /**
   * The {@link KafkaProducerProperties}
   *
   * @return KafkaProducerProperties.
   */
  protected KafkaProducerProperties testMessageKafkaProducerProperties() {

    KafkaProducerProperties kafkaProducerproperties = new KafkaProducerProperties();
    kafkaProducerproperties.setKeySerializer("org.apache.kafka.common.serialization.StringSerializer");
    kafkaProducerproperties.setValueSerializer("org.apache.kafka.common.serialization.StringSerializer");
    kafkaProducerproperties.setRetries(10);
    return kafkaProducerproperties;
  }

  /**
   * The {@link MessageSenderProperties}
   *
   * @return MessageSenderProperties.
   */
  protected MessageSenderProperties testMessageSenderProperties() {

    return new MessageSenderProperties();
  }

  /**
   * Creates Bean for the {@link ProducerFactory}.
   *
   * @return ProducerFactory
   * @throws IOException the {@link IOException}.
   */
  @Bean
  public ProducerFactory<Object, Object> testMessageKafkaProducerFactory() throws IOException {

    return createProducerFactory();
  }

  /**
   * The {@link ProducerFactory}
   *
   * @return ProducerFactory.
   * @throws IOException the {@link IOException}.
   */
  protected ProducerFactory<Object, Object> createProducerFactory() throws IOException {

    KafkaPropertyMapper mapper = new KafkaPropertyMapper();
    return new DefaultKafkaProducerFactory<>(
        mapper.producerProperties(testMessageKafkaCommonProperties(), testMessageKafkaProducerProperties()));
  }

  /**
   * Creates Bean for {@link KafkaTemplate}.
   *
   * @return KafkaTemplate.
   * @throws IOException the {@link IOException}.
   */
  @Bean
  public KafkaTemplate<Object, Object> testMessageKafkaTemplate() throws IOException {

    return createKafkaTemplate(testMessageProducerLoggingListener(testMessageLoggingSupport()),
        testMessageKafkaProducerFactory());
  }

  /**
   * The {@link KafkaTemplate}
   *
   * @param producerLogListener the {@link ProducerFencedException}
   * @param producerFactory the {@link ProducerFactory}
   * @return KafkaTemplate
   * @throws IOException the {@link IOException}
   */
  protected KafkaTemplate<Object, Object> createKafkaTemplate(ProducerLoggingListener producerLogListener,
      ProducerFactory<Object, Object> producerFactory) throws IOException {

    KafkaTemplate<Object, Object> template = new KafkaTemplate<>(createProducerFactory());
    template.setProducerListener(producerLogListener);
    return template;
  }

  /**
   * creates bean for the {@link MessageSenderImpl}
   *
   * @return MessageSenderImpl
   * @throws IOException the {@link IOException}.
   */
  @Bean
  public MessageSender<?, ?> testMessageSender() throws IOException {

    MessageSenderImpl<Object, Object> bean = new MessageSenderImpl<>();
    bean.setKafkaTemplate(testMessageKafkaTemplate());
    bean.setLoggingSupport(testMessageLoggingSupport());
    bean.setSenderProperties(testMessageSenderProperties());
    bean.setSpanInjector(messageSpanInjector());
    bean.setDiagnosticContextFacade(testDiagnosticContextFacade());
    bean.setTracer(this.tracer);
    return bean;
  }

  /**
   * The {@link MessageSpanInjector}
   *
   * @return MessageSpanInjector.
   */
  protected MessageSpanInjector messageSpanInjector() {

    return new MessageSpanInjector();
  }

  /**
   * The {@link ProducerLoggingListener}
   *
   * @param testMessageLoggingSupport the {@link MessageLoggingSupport}
   * @return ProducerLoggingListener
   */
  protected ProducerLoggingListener testMessageProducerLoggingListener(
      MessageLoggingSupport testMessageLoggingSupport) {

    return new ProducerLoggingListener(testMessageLoggingSupport());
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
