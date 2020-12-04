package com.devonfw.module.security.csrf.common.impl;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import com.devonfw.module.security.common.api.config.WebSecurityConfigurerAspect;
import com.devonfw.module.security.csrf.common.impl.security.CsrfRequestMatcher;

/**
 * Implementation of {@link WebSecurityConfigAspect} for CSRF protection.
 *
 * @since 2020.12.001
 */
public class WebSecurityConfigurerAspectCsrf implements WebSecurityConfigurerAspect {

  @Override
  public HttpSecurity configure(HttpSecurity http) throws Exception {

    return http.csrf().requireCsrfProtectionMatcher(new CsrfRequestMatcher()).and();
  }

}
