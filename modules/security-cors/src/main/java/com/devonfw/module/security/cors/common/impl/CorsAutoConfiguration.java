package com.devonfw.module.security.cors.common.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.security.common.api.config.WebSecurityConfigurerAspect;

/**
 * Auto-{@link Configuration} for spring to enable CORS protection.
 *
 * @since 2020.12.002
 */
@Configuration
public class CorsAutoConfiguration {

  /**
   * @return the {@link WebSecurityConfigurerAspect} to enable CORS.
   */
  @Bean
  public WebSecurityConfigurerAspect corsConfigAspect() {

    return new WebSecurityConfigurerAspectCors();
  }

  /**
   * @return the {@link CorsConfigProperties} to configure CORS.
   */
  @Bean
  public CorsConfigProperties corsConfigProperties() {

    return new CorsConfigProperties();
  }

}
