package com.devonfw.module.security.csrf.common.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.devonfw.module.security.common.api.config.WebSecurityConfigAspect;

/**
 * Implementation of {@link WebSecurityConfigAspect} for CSRF protection.
 *
 * @since 2020.12.001
 */
public class WebSecurityConfigAspectCsrf implements WebSecurityConfigAspect {

  @Inject
  @Named("CsrfRequestMatcher")
  private RequestMatcher requestMatcher;

  @Override
  public HttpSecurity configure(HttpSecurity http) throws Exception {

    return http.csrf().requireCsrfProtectionMatcher(this.requestMatcher).and();
  }

}
