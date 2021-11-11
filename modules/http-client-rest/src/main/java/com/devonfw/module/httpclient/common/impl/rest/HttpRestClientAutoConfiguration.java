package com.devonfw.module.httpclient.common.impl.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.devonfw.module.service.common.impl.client.ServiceClientAutoConfiguration;

/**
 * {@link Configuration} for REST (JAX-RS) clients using Java HTTP client.
 *
 * @since 2021.04.003
 */
@Configuration
@Import(ServiceClientAutoConfiguration.class)
public class HttpRestClientAutoConfiguration {

  /**
   * @return the instance of {@link ServiceClientHttpAdapterRest}.
   */
  @Bean
  public ServiceClientHttpAdapterRest serviceClientHttpAdapter() {

    return new ServiceClientHttpAdapterRest();
  }

}
