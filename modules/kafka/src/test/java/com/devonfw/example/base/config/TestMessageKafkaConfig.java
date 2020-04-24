package com.devonfw.example.base.config;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.apache.kafka.common.errors.ProducerFencedException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.listener.LoggingErrorHandler;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import com.devonfw.example.base.MessageProcessorImpl;
import com.devonfw.module.kafka.common.messaging.api.client.MessageSender;
import com.devonfw.module.kafka.common.messaging.api.config.KafkaCommonProperties;
import com.devonfw.module.kafka.common.messaging.api.config.KafkaConsumerProperties;
import com.devonfw.module.kafka.common.messaging.api.config.KafkaListenerContainerProperties;
import com.devonfw.module.kafka.common.messaging.api.config.KafkaProducerProperties;
import com.devonfw.module.kafka.common.messaging.api.config.MessageCommonConfig;
import com.devonfw.module.kafka.common.messaging.api.config.MessageSenderProperties;
import com.devonfw.module.kafka.common.messaging.health.api.config.KafkaHealthIndicatorProperties;
import com.devonfw.module.kafka.common.messaging.health.impl.KafkaHealthIndicator;
import com.devonfw.module.kafka.common.messaging.impl.MessageSenderImpl;
import com.devonfw.module.kafka.common.messaging.logging.impl.ConsumerGroupResolver;
import com.devonfw.module.kafka.common.messaging.logging.impl.MessageListenerLoggingAspect;
import com.devonfw.module.kafka.common.messaging.logging.impl.MessageLoggingSupport;
import com.devonfw.module.kafka.common.messaging.logging.impl.ProducerLoggingListener;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageProcessor;
import com.devonfw.module.kafka.common.messaging.retry.api.config.DefaultBackOffPolicyProperties;
import com.devonfw.module.kafka.common.messaging.retry.api.config.DefaultRetryPolicyProperties;
import com.devonfw.module.kafka.common.messaging.retry.impl.DefaultBackOffPolicy;
import com.devonfw.module.kafka.common.messaging.retry.impl.DefaultKafkaRecordSupport;
import com.devonfw.module.kafka.common.messaging.retry.impl.DefaultRetryPolicy;
import com.devonfw.module.kafka.common.messaging.retry.impl.MessageRetryContext;
import com.devonfw.module.kafka.common.messaging.retry.impl.MessageRetryTemplate;
import com.devonfw.module.kafka.common.messaging.trace.impl.MessageSpanExtractor;
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
@EnableKafka
public class TestMessageKafkaConfig {

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
  @Bean
  public MessageLoggingSupport testMessageLoggingSupport() {

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

  private KafkaListenerContainerProperties testkafkaListenerContainerProperties() {

    return new KafkaListenerContainerProperties();
  }

  /**
   * Creates the bean of {@link KafkaListenerContainerFactory}
   *
   * @param <K> the key type
   * @param <V> the value type
   *
   * @return the bean of {@link KafkaListenerContainerFactory}
   * @throws IOException IOEXception
   */
  @Bean
  public <K, V> KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<K, V>> kafkaListenerContainerFactory()
      throws IOException {

    return createKafkaListenerContainerFactory(testMessageKafkaCommonProperties(), testKafkaConsumerProperty(),
        testkafkaListenerContainerProperties(), testMessageLoggingErrorHandler());
  }

  private static <K, V> KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<K, V>> createKafkaListenerContainerFactory(
      KafkaCommonProperties kafkaCommonProperties, KafkaConsumerProperties kafkaConsumerProperties,
      KafkaListenerContainerProperties kafkaListenerContainerProperties,
      LoggingErrorHandler messageLoggingErrorHandler) {

    ConcurrentKafkaListenerContainerFactory<K, V> factory = new ConcurrentKafkaListenerContainerFactory<>();

    factory
        .setConsumerFactory(MessageCommonConfig.createConsumerFactory(kafkaCommonProperties, kafkaConsumerProperties));

    factory.setConcurrency(Optional.ofNullable(kafkaListenerContainerProperties.getConcurrency()).orElse(1));

    Optional.ofNullable(kafkaListenerContainerProperties.getAckMode())
        .ifPresent(ackMode -> setAckModeToListenerFactory(ackMode, factory));

    Optional.ofNullable(kafkaListenerContainerProperties.getAckCount())
        .ifPresent(ackCount -> factory.getContainerProperties().setAckCount(ackCount));

    Optional.ofNullable(kafkaListenerContainerProperties.getAckTime())
        .ifPresent(ackTime -> factory.getContainerProperties().setAckTime(ackTime));

    Optional.ofNullable(kafkaListenerContainerProperties.getPollTimeout())
        .ifPresent(pollTimeout -> factory.getContainerProperties().setPollTimeout(pollTimeout));

    Optional.ofNullable(kafkaListenerContainerProperties.getShutdownTimeout())
        .ifPresent(shutdownTimeout -> factory.getContainerProperties().setShutdownTimeout(shutdownTimeout));

    Optional.ofNullable(kafkaListenerContainerProperties.getSyncCommits())
        .ifPresent(syncCommits -> factory.getContainerProperties().setSyncCommits(syncCommits));

    Optional.ofNullable(kafkaListenerContainerProperties.getIdleEventInterval())
        .ifPresent(idleEventInterval -> factory.getContainerProperties().setIdleEventInterval(idleEventInterval));

    factory.setErrorHandler(messageLoggingErrorHandler);

    return factory;
  }

  private static <K, V> void setAckModeToListenerFactory(String ackModeProperty,
      ConcurrentKafkaListenerContainerFactory<K, V> factory) {

    AckMode ackMode = null;
    try {

      ackMode = AckMode.valueOf(ackModeProperty);

    } catch (IllegalArgumentException e) {

      StringBuilder builder = new StringBuilder();

      boolean isFirst = true;

      for (AckMode mode : AckMode.values()) {

        if (isFirst) {

          isFirst = false;
        } else {

          builder.append(", ");
        }

        builder.append(mode.name());
      }

      throw new IllegalArgumentException("Invalid ack-mode " + builder.toString(), e);
    }

    factory.getContainerProperties().setAckMode(ackMode);
  }

  /**
   * Creates bean for {@link LoggingErrorHandler}
   *
   * @return LoggingErrorHandler.
   */
  @Bean
  public LoggingErrorHandler testMessageLoggingErrorHandler() {

    return new LoggingErrorHandler();
  }

  /**
   * Creates the bean for {@link MessageListenerLoggingAspect}.
   *
   *
   * @return the {@link MessageListenerLoggingAspect}.
   */
  @Bean
  public MessageListenerLoggingAspect testMessageListenerLoggingAspect() {

    MessageListenerLoggingAspect messageListenerLoggingAspect = new MessageListenerLoggingAspect();
    messageListenerLoggingAspect.setTracer(this.tracer);
    messageListenerLoggingAspect.setSpanExtractor(testessageSpanExtractor());
    return messageListenerLoggingAspect;
  }

  private MessageSpanExtractor testessageSpanExtractor() {

    return new MessageSpanExtractor();
  }

  /**
   * Creates the bean for {@link ConsumerGroupResolver}.
   *
   * @return the ConsumerGroupResolver
   */
  @Bean
  public ConsumerGroupResolver testConsumerGroupResolver() {

    return new ConsumerGroupResolver();
  }

  /**
   * The {@link ConsumerFactory}
   *
   * @param <K> the key type
   * @param <V> the value type
   *
   * @return ConsumerFactory
   * @throws IOException the {@link IOException}
   */
  protected <K, V> ConsumerFactory<K, V> createConsumerFactory() throws IOException {

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
   * @param <K> the key type
   * @param <V> the value type
   *
   * @return ProducerFactory
   * @throws IOException the {@link IOException}.
   */
  @Bean
  public <K, V> ProducerFactory<K, V> testMessageKafkaProducerFactory() throws IOException {

    return createProducerFactory();
  }

  /**
   * The {@link ProducerFactory}
   *
   * @param <K> the key type
   * @param <V> the value type
   *
   * @return ProducerFactory.
   * @throws IOException the {@link IOException}.
   */
  protected <K, V> ProducerFactory<K, V> createProducerFactory() throws IOException {

    KafkaPropertyMapper mapper = new KafkaPropertyMapper();
    return new DefaultKafkaProducerFactory<>(
        mapper.producerProperties(testMessageKafkaCommonProperties(), testMessageKafkaProducerProperties()));
  }

  /**
   * Creates Bean for {@link KafkaTemplate}.
   *
   * @param <K> the key type
   * @param <V> the value type
   *
   * @return KafkaTemplate.
   * @throws IOException the {@link IOException}.
   */
  @Bean
  public <K, V> KafkaTemplate<K, V> testMessageKafkaTemplate() throws IOException {

    return createKafkaTemplate(testMessageProducerLoggingListener(testMessageLoggingSupport()),
        testMessageKafkaProducerFactory());
  }

  /**
   * The {@link KafkaTemplate}
   *
   * @param <K> the key type
   * @param <V> the value type
   *
   * @param producerLogListener the {@link ProducerFencedException}
   * @param producerFactory the {@link ProducerFactory}
   * @return KafkaTemplate
   * @throws IOException the {@link IOException}
   */
  protected <K, V> KafkaTemplate<K, V> createKafkaTemplate(ProducerLoggingListener<K, V> producerLogListener,
      ProducerFactory<K, V> producerFactory) throws IOException {

    KafkaTemplate<K, V> template = new KafkaTemplate<>(createProducerFactory());
    template.setProducerListener(producerLogListener);
    return template;
  }

  /**
   * creates bean for the {@link MessageSenderImpl}
   *
   * @param <K> the key type
   * @param <V> the value type
   *
   * @return MessageSenderImpl
   * @throws IOException the {@link IOException}.
   */
  @Bean
  public <K, V> MessageSender<K, V> testMessageSender() throws IOException {

    MessageSenderImpl<K, V> bean = new MessageSenderImpl<>();
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
   * @param <K> the key type
   * @param <V> the value type
   *
   * @param testMessageLoggingSupport the {@link MessageLoggingSupport}
   * @return ProducerLoggingListener
   */
  protected <K, V> ProducerLoggingListener<K, V> testMessageProducerLoggingListener(
      MessageLoggingSupport testMessageLoggingSupport) {

    return new ProducerLoggingListener<>(testMessageLoggingSupport());
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

  private <K, V> DefaultKafkaRecordSupport<K, V> testMessageKafkaRecordSupport() {

    return new DefaultKafkaRecordSupport<>();
  }

  /**
   * Creates bean for the {@link MessageRetryContext}
   *
   * @return the {@link MessageRetryContext}
   */
  @Bean
  public MessageRetryContext testMessageRetryContext() {

    return new MessageRetryContext();
  }

  private <K, V> DefaultRetryPolicy<K, V> testMessageRetryPolicy() {

    return new DefaultRetryPolicy<>(testMessageDefaultRetryPolicyProperties());
  }

  private DefaultBackOffPolicy testMessageBackOffPolicy() {

    return new DefaultBackOffPolicy(testMessageDefaultBackOffPolicyProperties());
  }

  private DefaultBackOffPolicyProperties testMessageDefaultBackOffPolicyProperties() {

    return new DefaultBackOffPolicyProperties();
  }

  private DefaultRetryPolicyProperties testMessageDefaultRetryPolicyProperties() {

    Set<String> exceptionClasses = new TreeSet<>();
    exceptionClasses.add("java.lang.Exception");

    DefaultRetryPolicyProperties defaultRetryPolicyProperties = new DefaultRetryPolicyProperties();
    defaultRetryPolicyProperties.setRetryableExceptions(exceptionClasses);
    defaultRetryPolicyProperties.setRetryCount(2);

    return defaultRetryPolicyProperties;
  }

  /**
   * Creates bean for the {@link MessageRetryTemplate}
   *
   * @param <K> the key type
   * @param <V> the value type
   *
   * @return the {@link MessageRetryTemplate}
   * @throws IOException IOE
   */
  @Bean
  public <K, V> MessageRetryTemplate<K, V> messageDefaultRetryTemplate() throws IOException {

    MessageRetryTemplate<K, V> bean = new MessageRetryTemplate<>(testMessageRetryPolicy(), testMessageBackOffPolicy());
    bean.setMessageSender(testMessageSender());
    bean.setKafkaRecordSupport(testMessageKafkaRecordSupport());
    return bean;
  }

  /**
   * Creates Bean
   *
   * @return MessageProcessor
   */
  @Bean
  public MessageProcessor<String, String> testMessageProcessor() {

    return new MessageProcessorImpl();
  }

}
