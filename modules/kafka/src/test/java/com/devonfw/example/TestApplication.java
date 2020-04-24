package com.devonfw.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.devonfw.example.base.config.TestMessageKafkaConfig;

/**
 * A spring boot application class used for test purpose.
 */
@SpringBootApplication
@Import({ TestMessageKafkaConfig.class })
public class TestApplication {

  /**
   * @param args the command-line arguments
   */
  public static void main(String[] args) {

    SpringApplication.run(TestApplication.class, args);
  }

}
