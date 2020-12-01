package com.devonfw.module.security.common.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.security.common.api.config.WebSecurityConfig;
import com.devonfw.module.security.common.api.config.WebSecurityConfigAspect;
import com.devonfw.module.security.common.impl.config.WebSecurityConfigAspectDefault;
import com.devonfw.module.security.common.impl.config.WebSecurityConfigImpl;

/**
 * {@link Configuration} for modular security based on spring-security.
 *
 * @since 2020.12.001
 */
public class SecurityAutoConfiguration {

  /**
   * @return the {@link WebSecurityConfig} that encapsulates all {@link WebSecurityConfigAspectDefault aspects}.
   */
  @Bean
  public WebSecurityConfig getWebSecurityConfig() {

    return new WebSecurityConfigImpl();
  }

  /**
   * @return the default {@link WebSecurityConfigAspect}.
   */
  @Bean
  public WebSecurityConfigAspectDefault getWebSecurityConfigAspectDefault() {

    return new WebSecurityConfigAspectDefault();
  }

}
