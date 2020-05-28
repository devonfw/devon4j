package com.devonfw.module.security.jwt.common.base.kafka;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import com.devonfw.module.kafka.common.messaging.api.config.KafkaCommonProperties;

/**
 * This configuration injects information for the {@link EmbeddedKafkaBroker} into the kafka configuration.
 *
 */
@Configuration
public class EmbeddedKafkaProperties {

  @Inject
  private EmbeddedKafkaBroker embeddedKafka;

  /**
   * Creates the bean and looks for properties with the given prefix.
   *
   * @return the bean of {@link KafkaCommonProperties}}
   */
  @Bean
  @Primary
  public KafkaCommonProperties embeddedBrokerCommonProperties() {

    return new KafkaCommonProperties() {
      @Override
      public String getBootstrapServers() {

        return EmbeddedKafkaProperties.this.embeddedKafka.getBrokersAsString();
      }
    };

  }

}
