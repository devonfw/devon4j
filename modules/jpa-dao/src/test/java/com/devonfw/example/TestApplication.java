package com.devonfw.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import com.devonfw.module.jpa.dataaccess.api.JpaInitializer;

/**
 * Spring-boot app for testing.
 */
@SpringBootApplication
@EntityScan
public class TestApplication {

  @Bean
  public JpaInitializer jpaInitializer() {

    return new JpaInitializer();
  }

  /**
   * @param args the command-line arguments
   */
  public static void main(String[] args) {

    SpringApplication.run(TestApplication.class, args);
  }
}
