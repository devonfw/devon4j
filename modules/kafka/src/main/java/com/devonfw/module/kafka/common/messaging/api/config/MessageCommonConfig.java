package com.devonfw.module.kafka.common.messaging.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;

import com.devonfw.module.kafka.common.messaging.health.api.config.KafkaHealthIndicatorProperties;
import com.devonfw.module.kafka.common.messaging.health.impl.KafkaHealthIndicator;
import com.devonfw.module.kafka.common.messaging.logging.impl.MessageLoggingSupport;

/**
 * A configuration class creates the bean configuration for the {@link KafkaCommonProperties},
 * {@link MessageLoggingSupport}, {@link KafkaHealthIndicatorProperties}, {@link KafkaHealthIndicator} and
 * {@link ConsumerFactory}.
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

}
