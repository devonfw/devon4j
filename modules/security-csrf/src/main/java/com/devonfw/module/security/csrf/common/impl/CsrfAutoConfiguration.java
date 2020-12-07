package com.devonfw.module.security.csrf.common.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.security.common.api.config.WebSecurityConfigurerAspect;
import com.devonfw.module.security.csrf.service.api.rest.CsrfRestService;
import com.devonfw.module.security.csrf.service.impl.rest.CsrfRestServiceImpl;

/**
 * {@link Configuration} to enable CSRF protection.
 *
 * @since 2020.12.001
 */
@Configuration
public class CsrfAutoConfiguration {

  /**
   * @return the {@link WebSecurityConfigurerAspect} to enable CSRF.
   */
  @Bean
  public WebSecurityConfigurerAspect csrfConfigAspect() {

    return new WebSecurityConfigurerAspectCsrf();
  }

  /**
   * @return the {@link CsrfRestService} implementation.
   */
  @Bean
  public CsrfRestService csrfRestService() {

    return new CsrfRestServiceImpl();
  }

}