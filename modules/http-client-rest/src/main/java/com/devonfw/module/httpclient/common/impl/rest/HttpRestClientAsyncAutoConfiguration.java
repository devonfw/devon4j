package com.devonfw.module.httpclient.common.impl.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.devonfw.module.service.common.api.client.async.AsyncServiceClientFactory;
import com.devonfw.module.service.common.api.client.sync.SyncServiceClientFactory;
import com.devonfw.module.service.common.impl.client.ServiceClientAutoConfiguration;

/**
 * {@link Configuration} for REST (JAX-RS) clients using Java HTTP client.
 *
 * @since 2020.08.001
 */
@Configuration
@Import(ServiceClientAutoConfiguration.class)
public class HttpRestClientAsyncAutoConfiguration {

  /**
   * @return an implementation of {@link SyncServiceClientFactory} based on Java HTTP client for REST (JAX-RS).
   */
  @Bean
  public AsyncServiceClientFactory asyncServiceClientFactoryHttpRest() {

    return new AsyncServiceClientFactoryHttpRest();
  }

}
