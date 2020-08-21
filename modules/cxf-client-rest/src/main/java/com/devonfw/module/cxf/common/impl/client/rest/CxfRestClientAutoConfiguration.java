package com.devonfw.module.cxf.common.impl.client.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.service.common.api.client.sync.SyncServiceClientFactory;

/**
 * {@link Configuration} for REST (JAX-RS) clients using Apache CXF.
 *
 * @since 3.0.0
 */
@Configuration
public class CxfRestClientAutoConfiguration {

  /**
   * @return an implementation of {@link SyncServiceClientFactory} based on CXF for REST (JAX-RS).
   */
  @Bean
  public SyncServiceClientFactory syncServiceClientFactoryCxfRest() {

    return new SyncServiceClientFactoryCxfRest();
  }

}
