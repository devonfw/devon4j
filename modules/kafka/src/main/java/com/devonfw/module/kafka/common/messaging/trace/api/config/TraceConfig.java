package com.devonfw.module.kafka.common.messaging.trace.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.kafka.common.messaging.trace.impl.MessageSpanExtractor;
import com.devonfw.module.kafka.common.messaging.trace.impl.MessageSpanInjector;

/**
 * The configuration class to create configuration bean for the {@link MessageSpanInjector} and
 * {@link MessageSpanExtractor}
 *
 */
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
   * @return the {@link MessageSpanExtractor}
   */
  @Bean
  public MessageSpanExtractor messageSpanExtractor() {

    return new MessageSpanExtractor();
  }

}
