package com.devonfw.module.security.jwt.common.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.security.jwt.common.base.web.JwtAuthenticationFilter;

/**
 * {@link Configuration} for JSON Web Token (JWT) support for web servlets.
 *
 * @since 2020.04.001
 */
@Configuration
@ConditionalOnClass(name = "javax.servlet.Filter")
public class JwtAutoConfigurationWeb {

  /**
   * @return the {@link JwtAuthenticationFilter}.
   */
  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {

    return new JwtAuthenticationFilter();
  }

}
