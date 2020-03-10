package com.devonfw.module.kafka.common.messaging.api.config;

import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.listener.LoggingErrorHandler;

import com.devonfw.module.kafka.common.messaging.logging.impl.ConsumerGroupResolver;
import com.devonfw.module.kafka.common.messaging.logging.impl.MessageListenerLoggingAspect;

/**
 * A class used to create a configuration for the custom message receiver.
 *
 */
@Configuration
@EnableKafka
@EnableAspectJAutoProxy
@Import(MessageCommonConfig.class)
public class MessageReceiverConfig {

  /**
   * Creates the bean of {@link KafkaConsumerProperties} and looks for the prefix given to map the properties.
   *
   * @return the bean of {@link KafkaConsumerProperties}.
   */
  @Bean
  @ConfigurationProperties(prefix = "messaging.kafka.consumer")
  public KafkaConsumerProperties messageKafkaConsumerProperties() {

    return new KafkaConsumerProperties();
  }

  /**
   * Creates the bean of {@link KafkaListenerContainerProperties} and looks for the prefix given to map the properties.
   *
   * @return the bean of {@link KafkaListenerContainerProperties} .
   */
  @Bean
  @ConfigurationProperties(prefix = "messaging.kafka.listener.container")
  public KafkaListenerContainerProperties messageKafkaListenerContainerProperties() {

    return new KafkaListenerContainerProperties();
  }

  /**
   * Creates the bean of {@link KafkaListenerContainerFactory}
   *
   * @param messageKafkaCommonProperties the {@link MessageCommonConfig#messageKafkaCommonProperties()}
   * @param messageKafkaConsumerProperties the {@link #messageKafkaConsumerProperties()}
   * @param messageKafkaListenerContainerProperties the {@link #messageKafkaListenerContainerProperties()}
   * @param messageLoggingErrorHandler the {@link #messageLoggingErrorHandler()}
   * @return the bean of {@link KafkaListenerContainerFactory}
   */
  @Bean
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Object, Object>> kafkaListenerContainerFactory(
      KafkaCommonProperties messageKafkaCommonProperties, KafkaConsumerProperties messageKafkaConsumerProperties,
      KafkaListenerContainerProperties messageKafkaListenerContainerProperties,
      LoggingErrorHandler messageLoggingErrorHandler) {

    return createKafkaListenerContainerFactory(messageKafkaCommonProperties, messageKafkaConsumerProperties,
        messageKafkaListenerContainerProperties, messageLoggingErrorHandler);
  }

  /**
   * Creates the bean for {@link MessageListenerLoggingAspect}.
   *
   * @return the {@link MessageListenerLoggingAspect}.
   */
  @Bean
  public MessageListenerLoggingAspect messageListenerLoggingAspect() {

    return new MessageListenerLoggingAspect();
  }

  /**
   * Creates the bean for {@link LoggingErrorHandler}
   *
   * @return the LoggingErrorHandler
   */
  @Bean
  public LoggingErrorHandler messageLoggingErrorHandler() {

    return new LoggingErrorHandler();
  }

  /**
   * Creates the bean for {@link ConsumerGroupResolver}.
   *
   * @return the ConsumerGroupResolver
   */
  @Bean
  public ConsumerGroupResolver consumerGroupResolver() {

    return new ConsumerGroupResolver();
  }

  /**
   * This method is used to create {@link KafkaListenerContainerFactory}.
   *
   * @param kafkaCommonProperties the {@link KafkaCommonProperties}
   * @param kafkaConsumerProperties the {@link KafkaConsumerProperties}
   * @param kafkaListenerContainerProperties the {@link KafkaListenerContainerProperties}
   * @param messageLoggingErrorHandler the {@link LoggingErrorHandler}
   * @return the KafkaListenerContainerFactory.
   */
  public static KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Object, Object>> createKafkaListenerContainerFactory(
      KafkaCommonProperties kafkaCommonProperties, KafkaConsumerProperties kafkaConsumerProperties,
      KafkaListenerContainerProperties kafkaListenerContainerProperties,
      LoggingErrorHandler messageLoggingErrorHandler) {

    ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();

    factory
        .setConsumerFactory(MessageCommonConfig.createConsumerFactory(kafkaCommonProperties, kafkaConsumerProperties));

    Optional.ofNullable(kafkaListenerContainerProperties.getConcurrency())
        .ifPresentOrElse(concurrency -> factory.setConcurrency(concurrency), () -> factory.setConcurrency(1));

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

  private static void setAckModeToListenerFactory(String ackModeProperty,
      ConcurrentKafkaListenerContainerFactory<Object, Object> factory) {

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

}
