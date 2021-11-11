package com.devonfw.module.security.jwt.common.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.security.jwt.common.base.kafka.JwtTokenValidationAspect;

/**
 * {@link Configuration} for JSON Web Token (JWT) support for Kafka.
 *
 * @since 2020.04.001
 */
@Configuration
@ConditionalOnClass(name = "org.apache.kafka.clients.consumer.ConsumerRecord")
public class JwtAutoConfigurationKafka {

  /**
   * @return the {@link JwtTokenValidationAspect}.
   */
  @Bean
  public JwtTokenValidationAspect jwtTokenValidationAspect() {

    return new JwtTokenValidationAspect();
  }

}
