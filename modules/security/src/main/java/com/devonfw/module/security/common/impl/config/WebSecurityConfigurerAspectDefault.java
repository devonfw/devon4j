package com.devonfw.module.security.common.impl.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import com.devonfw.module.security.common.api.config.WebSecurityConfigurerAspect;

/**
 * Default implementation of {@link WebSecurityConfigurerAspect} as the container (spring) will raise an exception if no
 * implementation is found at all.
 */
public class WebSecurityConfigurerAspectDefault implements WebSecurityConfigurerAspect {

  @Override
  public HttpSecurity configure(HttpSecurity http) {

    return http;
  }

}
