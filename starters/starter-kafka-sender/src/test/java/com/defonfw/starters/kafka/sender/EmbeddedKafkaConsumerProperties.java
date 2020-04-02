package com.defonfw.starters.kafka.sender;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import com.devonfw.module.kafka.common.messaging.api.config.KafkaCommonProperties;
import com.devonfw.module.kafka.common.messaging.api.config.KafkaProducerProperties;
import com.devonfw.module.kafka.common.messaging.util.KafkaPropertyMapper;

/**
 * This configuration injects information for the {@link EmbeddedKafkaBroker} into the kafka configuration.
 *
 */
@Configuration
public class EmbeddedKafkaConsumerProperties {

  @Inject
  private EmbeddedKafkaBroker embeddedKafka;

  @Inject
  private KafkaCommonProperties kafkaCommonProperties;

  @Bean
  public ProducerFactory<Object, Object> producerFactory(KafkaCommonProperties commonProperties,
      KafkaProducerProperties producerProperties) {

    this.kafkaCommonProperties.setBootstrapServers(this.embeddedKafka.getBrokersAsString());
    KafkaPropertyMapper mapper = new KafkaPropertyMapper();
    return new DefaultKafkaProducerFactory<>(mapper.producerProperties(commonProperties, producerProperties));
  }
}
