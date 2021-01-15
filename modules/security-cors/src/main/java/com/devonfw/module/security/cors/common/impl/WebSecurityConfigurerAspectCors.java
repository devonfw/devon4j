package com.devonfw.module.security.cors.common.impl;

import javax.inject.Inject;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.devonfw.module.security.common.api.config.WebSecurityConfigurerAspect;

/**
 * Implementation of {@link WebSecurityConfigurerAspect} for CORS protection.
 *
 * @since 2020.12.001
 */
public class WebSecurityConfigurerAspectCors implements WebSecurityConfigurerAspect {

  private CorsConfigProperties corsConfigProperties;

  /**
   * @param corsConfigProperties new value of {@link #getcorsConfigProperties}.
   */
  @Inject
  public void setCorsConfigProperties(CorsConfigProperties corsConfigProperties) {

    this.corsConfigProperties = corsConfigProperties;
  }

  private CorsFilter getCorsFilter() {

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(this.corsConfigProperties.isAllowCredentials());
    config.setAllowedOrigins(this.corsConfigProperties.getAllowedOrigins());
    config.setAllowedHeaders(this.corsConfigProperties.getAllowedHeaders());
    config.setAllowedMethods(this.corsConfigProperties.getAllowedMethods());
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  @Override
  public HttpSecurity configure(HttpSecurity http) throws Exception {

    return http.addFilter(getCorsFilter());
  }

}
