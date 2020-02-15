package com.devonfw.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.devonfw.module.kafka.common.messaging.api.config.MessageReceiverConfig;
import com.devonfw.module.kafka.common.messaging.api.config.MessageSenderConfig;
import com.devonfw.module.kafka.common.messaging.trace.api.config.TraceConfig;

/**
 * @author ravicm
 *
 */
@SpringBootApplication
@Import({ MessageSenderConfig.class, MessageReceiverConfig.class, TraceConfig.class })
public class TestApplication {

  /**
   * @param args the command-line arguments
   */
  public static void main(String[] args) {

    SpringApplication.run(TestApplication.class, args);
  }

}
