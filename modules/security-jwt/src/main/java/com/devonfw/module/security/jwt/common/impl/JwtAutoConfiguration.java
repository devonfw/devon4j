package com.devonfw.module.security.jwt.common.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.security.jwt.common.api.JwtAuthenticator;
import com.devonfw.module.security.jwt.common.api.JwtCreator;
import com.devonfw.module.security.jwt.common.api.JwtManager;

/**
 * {@link Configuration} for JSON Web Token (JWT) support.
 *
 * @since 2020.04.001
 */
@Configuration
public class JwtAutoConfiguration {

  /**
   * @return the implementation of {@link JwtManager}.
   */
  @Bean
  public JwtManager jwtManager() {

    return new JwtManagerImpl();
  }

  /**
   * @return the implementation of {@link JwtCreator}.
   */
  @Bean
  public JwtCreator jwtCreator() {

    return new JwtCreatorImpl();
  }

  /**
   * @return the implementation of {@link JwtAuthenticator}.
   */
  @Bean
  public JwtAuthenticator jwtAuthenticator() {

    return new JwtAuthenticatorImpl();
  }

  /**
   * @return the {@link JwtConfigProperties} mapped for spring {@code application.properties}.
   */
  @Bean
  public JwtConfigProperties jwtConfigProperties() {

    return new JwtConfigProperties();
  }

}
