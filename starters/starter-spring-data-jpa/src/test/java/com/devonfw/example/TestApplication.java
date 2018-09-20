package com.devonfw.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.devonfw.module.jpa.dataaccess.impl.data.GenericRepositoryFactoryBean;

/**
 * Spring-boot app for testing.
 */
@SpringBootApplication
@EntityScan
@EnableJpaRepositories(repositoryFactoryBeanClass = GenericRepositoryFactoryBean.class)
public class TestApplication {

  /**
   * @param args the command-line arguments
   */
  public static void main(String[] args) {

    SpringApplication.run(TestApplication.class, args);
  }
}
