package com.devonfw.module.security.cors.common.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.security.common.api.config.WebSecurityConfigurerAspect;

/**
 * {@link Configuration} to enable CSRF protection.
 *
 * @since 2020.12.001
 */
@Configuration
public class CorsAutoConfiguration {

  /**
   * @return the {@link WebSecurityConfigurerAspect} to enable CSRF.
   */
  @Bean
  public WebSecurityConfigurerAspect csrfConfigAspect() {

    return new WebSecurityConfigurerAspectCsrf();
  }

}