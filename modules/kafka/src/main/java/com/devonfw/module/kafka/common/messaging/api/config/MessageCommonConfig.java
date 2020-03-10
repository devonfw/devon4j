package com.devonfw.module.kafka.common.messaging.api.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import com.devonfw.module.kafka.common.messaging.health.api.config.KafkaHealthIndicatorProperties;
import com.devonfw.module.kafka.common.messaging.health.impl.KafkaHealthIndicator;
import com.devonfw.module.kafka.common.messaging.logging.impl.MessageLoggingSupport;
import com.devonfw.module.kafka.common.messaging.util.KafkaPropertyMapper;

/**
 * A configuration class creates the bean config for the {@link KafkaCommonProperties}, {@link MessageLoggingSupport},
 * {@link KafkaHealthIndicatorProperties}, {@link KafkaHealthIndicator} and {@link ConsumerFactory}.
 *
 */
@Configuration
@EnableConfigurationProperties
public class MessageCommonConfig {

  /**
   * Creates the bean and looks for properties with the given prefix.
   *
   * @return the bean of {@link KafkaCommonProperties}}
   */
  @Bean
  @ConfigurationProperties(prefix = "messaging.kafka.common")
  public KafkaCommonProperties messageKafkaCommonProperties() {

    return new KafkaCommonProperties();
  }

  /**
   * @return the bean of {@link MessageLoggingSupport}}
   */
  @Bean
  public MessageLoggingSupport messageLoggingSupport() {

    return new MessageLoggingSupport();
  }

  /**
   * Creates the bean and looks for properties with the given prefix when
   * <code>management.endpoint.health.enabled=true</code>.
   *
   * @return the bean of {@link KafkaHealthIndicatorProperties}}
   */
  @Bean
  @ConditionalOnProperty("management.endpoint.health.enabled")
  @ConfigurationProperties(prefix = "messaging.kafka.health")
  public KafkaHealthIndicatorProperties messageKafkaHealthIndicatorProperties() {

    return new KafkaHealthIndicatorProperties();
  }

  /**
   * Creates the bean and looks for properties with the given prefix when
   * <code>management.endpoint.health.enabled=true</code>.
   *
   * @param messageKafkaHealthIndicatorProperties the {@link #messageKafkaHealthIndicatorProperties()} bean.
   * @param messageKafkaCommonProperties the {@link #messageKafkaCommonProperties()}} bean.
   * @return the bean of {@link KafkaHealthIndicator}
   */
  @Bean
  @ConditionalOnProperty("management.endpoint.health.enabled")
  public KafkaHealthIndicator kafkaHealthIndicator(KafkaHealthIndicatorProperties messageKafkaHealthIndicatorProperties,
      KafkaCommonProperties messageKafkaCommonProperties) {

    return new KafkaHealthIndicator(createConsumerFactory(messageKafkaCommonProperties, new KafkaConsumerProperties()),
        messageKafkaHealthIndicatorProperties);
  }

  /**
   * This method is used to create {@link ConsumerFactory}
   *
   * @param kafkaCommonProperties the {@link KafkaCommonProperties}
   * @param kafkaConsumerProperties the {@link KafkaConsumerProperties}
   * @return the {@link ConsumerFactory}
   */
  public static ConsumerFactory<Object, Object> createConsumerFactory(KafkaCommonProperties kafkaCommonProperties,
      KafkaConsumerProperties kafkaConsumerProperties) {

    KafkaPropertyMapper mapper = new KafkaPropertyMapper();
    return new DefaultKafkaConsumerFactory<>(mapper.consumerProperties(kafkaCommonProperties, kafkaConsumerProperties));
  }

}
