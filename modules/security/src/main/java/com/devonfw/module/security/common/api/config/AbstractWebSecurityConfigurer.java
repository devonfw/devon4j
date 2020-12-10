package com.devonfw.module.security.common.api.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Abstract interface to allow modular {@link #configure(HttpSecurity) configuration} of {@link HttpSecurity} via
 * {@link WebSecurityConfigurerAspect aspects} that can even be driven by spring-boot-starters from devon4j.
 *
 * @since 2020.12.001
 */
public abstract interface AbstractWebSecurityConfigurer {

  /**
   * @param http the {@link HttpSecurity} to configure modular aspects that can even be driven by spring-boot-starters
   *        from devon4j.
   * @return the {@link HttpSecurity} with potential aspect(s) configured.
   * @throws Exception in case of an error.
   */
  HttpSecurity configure(HttpSecurity http) throws Exception;

}
