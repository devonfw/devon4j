package com.devonfw.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.devonfw.example.base.config.EmbeddedKafkaProperties;
import com.devonfw.module.kafka.common.messaging.api.config.MessageReceiverConfig;
import com.devonfw.module.kafka.common.messaging.api.config.MessageSenderConfig;
import com.devonfw.module.kafka.common.messaging.retry.api.config.MessageDefaultRetryConfig;
import com.devonfw.module.kafka.common.messaging.trace.api.config.TraceConfig;

/**
 * A spring boot application class used for test purpose.
 */
@SpringBootApplication
@Import({ TraceConfig.class, MessageSenderConfig.class, MessageReceiverConfig.class, MessageDefaultRetryConfig.class,
EmbeddedKafkaProperties.class })
public class TestApplication {

  /**
   * @param args the command-line arguments
   */
  public static void main(String[] args) {

    SpringApplication.run(TestApplication.class, args);
  }

}
