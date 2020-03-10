package com.devonfw.module.kafka.common.messaging.trace.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.kafka.common.messaging.trace.impl.MessageSpanExtractor;
import com.devonfw.module.kafka.common.messaging.trace.impl.MessageSpanInjector;
import com.devonfw.module.logging.common.api.DiagnosticContextFacade;

/**
 * @author ravicm
 *
 */
@Configuration
public class TraceConfig {

  /**
   * @param diagnosticContextFacade
   * @return
   */
  @Bean
  public MessageSpanInjector messageSpanInjector(DiagnosticContextFacade diagnosticContextFacade) {

    MessageSpanInjector messageSpanInjector = new MessageSpanInjector();
    messageSpanInjector.setDiagnosticContextFacade(diagnosticContextFacade);
    return messageSpanInjector;
  }

  /**
   * @return
   */
  @Bean
  public MessageSpanExtractor messageSpanExtractor() {

    return new MessageSpanExtractor();
  }

}
