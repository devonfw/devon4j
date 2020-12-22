package com.devonfw.module.security.cors.common.impl;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.devonfw.module.security.common.api.config.WebSecurityConfigurerAspect;

/**
 * Implementation of {@link WebSecurityConfigurerAspect} for CSRF protection.
 *
 * @since 2020.12.001
 */
public class WebSecurityConfigurerAspectCors implements WebSecurityConfigurerAspect {

  private CorsFilter getCorsFilter() {

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("OPTIONS");
    config.addAllowedMethod("HEAD");
    config.addAllowedMethod("GET");
    config.addAllowedMethod("PUT");
    config.addAllowedMethod("POST");
    config.addAllowedMethod("DELETE");
    config.addAllowedMethod("PATCH");
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  @Override
  public HttpSecurity configure(HttpSecurity http) throws Exception {

    return http.addFilter(getCorsFilter());
  }

}
