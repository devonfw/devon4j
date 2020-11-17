package com.devonfw.module.security.csrf.common.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.security.csrf.service.api.rest.CsrfRestService;
import com.devonfw.module.security.csrf.service.impl.rest.CsrfRestServiceImpl;

@Configuration
public class CsrfAutoConfiguration {
  @Bean
  public CsrfRestService csrfRestService() {

    return new CsrfRestServiceImpl();
  }

  @Bean
  public CsrfTokenImpl csrfTokenImpl() {

    return new CsrfTokenImpl();
  }
}
