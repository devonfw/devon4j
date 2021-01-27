package com.devonfw.module.security.common.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.security.common.api.config.WebSecurityConfigurer;
import com.devonfw.module.security.common.api.config.WebSecurityConfigurerAspect;
import com.devonfw.module.security.common.impl.config.WebSecurityConfigurerAspectDefault;
import com.devonfw.module.security.common.impl.config.WebSecurityConfigurerImpl;

/**
 * {@link Configuration} for modular security based on spring-security.
 *
 * @since 2020.12.001
 */
public class SecurityAutoConfiguration {

  /**
   * @return the {@link WebSecurityConfigurer} that encapsulates all {@link WebSecurityConfigurerAspectDefault aspects}.
   */
  @Bean
  public WebSecurityConfigurer getWebSecurityConfig() {

    return new WebSecurityConfigurerImpl();
  }

  /**
   * @return the default {@link WebSecurityConfigurerAspect}.
   */
  @Bean
  public WebSecurityConfigurerAspectDefault getWebSecurityConfigAspectDefault() {

    return new WebSecurityConfigurerAspectDefault();
  }

}
