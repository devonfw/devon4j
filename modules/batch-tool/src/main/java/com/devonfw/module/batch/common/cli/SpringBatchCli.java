package com.devonfw.module.batch.common.cli;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot Application
 *
 */
@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchCli {

  /**
   * @param args
   */
  public static void main(String[] args) {

    SpringApplication app = new SpringApplication(SpringBatchCli.class);
    app.setBannerMode(Mode.OFF);
    System.setProperty("logging.level.root", "ERROR");
    System.exit(SpringApplication.exit(app.run(args)));
  }

}
