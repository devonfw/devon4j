package com.devonfw.module.kafka.common.messaging.health.api.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.ConsumerFactory;

import com.devonfw.module.kafka.common.messaging.api.config.MessageReceiverConfig;
import com.devonfw.module.kafka.common.messaging.health.impl.KafkaHealthIndicator;

/**
 * A class used to create a configuration for the custom message receiver.
 *
 * @deprecated The implementation of devon4j-kafka will be abandoned. It is superseeded by Springs Kafka
 *             implementation.
 */
@Deprecated
@Configuration
@Import(MessageReceiverConfig.class)
public class MessageHealthIndicatorConfig {

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
   * @param <K> the key type
   * @param <V> the value type
   *
   * @param messageKafkaHealthIndicatorProperties the {@link #messageKafkaHealthIndicatorProperties()} bean.
   * @param consumerFactory the {@link ConsumerFactory}} bean.
   * @return the bean of {@link KafkaHealthIndicator}
   */
  @Bean
  @ConditionalOnProperty("management.endpoint.health.enabled")
  public <K, V> KafkaHealthIndicator<K, V> kafkaHealthIndicator(
      KafkaHealthIndicatorProperties messageKafkaHealthIndicatorProperties, ConsumerFactory<K, V> consumerFactory) {

    return new KafkaHealthIndicator<>(consumerFactory, messageKafkaHealthIndicatorProperties);
  }

}
