package com.devonfw.module.kafka.common.messaging.trace.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.kafka.common.messaging.trace.impl.MessageSpanExtractor;
import com.devonfw.module.kafka.common.messaging.trace.impl.MessageSpanInjector;

/**
 * @author ravicm
 *
 */
@Configuration
public class TraceConfig {

  /**
   * @return
   */
  @Bean
  @ConfigurationProperties(prefix = "spring.application")
  public MessageTraceProperties messageTraceProperties() {

    return new MessageTraceProperties();
  }

  /**
   * @return
   */
  @Bean
  public MessageSpanInjector messageSpanInjector() {

    return new MessageSpanInjector();
  }

  /**
   * @return
   */
  @Bean
  public MessageSpanExtractor messageSpanExtractor() {

    return new MessageSpanExtractor();
  }

}
