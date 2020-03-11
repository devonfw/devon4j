package com.devonfw.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.devonfw.example.base.config.TestMessageSenderConfig;

/**
 *
 */
@SpringBootApplication
@Import({ TestMessageSenderConfig.class })
public class TestApplication {

  /**
   * @param args the command-line arguments
   */
  public static void main(String[] args) {

    SpringApplication.run(TestApplication.class, args);
  }

}
