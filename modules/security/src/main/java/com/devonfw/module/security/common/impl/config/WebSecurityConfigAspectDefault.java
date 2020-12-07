package com.devonfw.module.security.common.impl.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import com.devonfw.module.security.common.api.config.WebSecurityConfigAspect;

/**
 * Default implementation of {@link WebSecurityConfigAspect} as the container (spring) will raise an exception if no
 * implementation is found at all.
 */
public class WebSecurityConfigAspectDefault implements WebSecurityConfigAspect {

  @Override
  public HttpSecurity configure(HttpSecurity http) {

    return http;
  }

}
