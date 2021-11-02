package com.devonfw.module.kafka.common.messaging.trace.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.kafka.common.messaging.trace.impl.MessageSpanExtractor;
import com.devonfw.module.kafka.common.messaging.trace.impl.MessageSpanInjector;

/**
 * The configuration class to create configuration bean for the {@link MessageSpanInjector} and
 * {@link MessageSpanExtractor}
 *
 * @deprecated The implementation of devon4j-kafka will be abandoned. It is superseeded by Springs Kafka
 *             implementation.
 */
@Deprecated
@Configuration
public class TraceConfig {

  /**
   * Creates bean for the {@link MessageSpanExtractor}
   *
   * @return the {@link MessageSpanInjector}
   */
  @Bean
  public MessageSpanInjector messageSpanInjector() {

    return new MessageSpanInjector();
  }

  /**
   * Creates bean for the {@link MessageSpanExtractor}
   *
   * @param <K> the key type
   * @param <V> the value type
   *
   * @return the {@link MessageSpanExtractor}
   */
  @Bean
  public <K, V> MessageSpanExtractor<K, V> messageSpanExtractor() {

    return new MessageSpanExtractor<>();
  }

}
