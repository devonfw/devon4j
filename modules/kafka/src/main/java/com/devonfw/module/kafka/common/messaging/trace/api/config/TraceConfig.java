package com.devonfw.module.kafka.common.messaging.trace.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.kafka.common.messaging.trace.impl.MessageSpanExtractor;
import com.devonfw.module.kafka.common.messaging.trace.impl.MessageSpanInjector;
import com.devonfw.module.logging.common.api.DiagnosticContextFacade;

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
   * @param diagnosticContextFacade the {@link DiagnosticContextFacade}
   * @return the {@link MessageSpanInjector}
   */
  @Bean
  public MessageSpanInjector messageSpanInjector(DiagnosticContextFacade diagnosticContextFacade) {

    MessageSpanInjector messageSpanInjector = new MessageSpanInjector();
    messageSpanInjector.setDiagnosticContextFacade(diagnosticContextFacade);
    return messageSpanInjector;
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
