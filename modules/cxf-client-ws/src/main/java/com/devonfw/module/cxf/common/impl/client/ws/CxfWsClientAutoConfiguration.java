package com.devonfw.module.cxf.common.impl.client.ws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.service.common.api.client.sync.SyncServiceClientFactory;

/**
 * {@link Configuration} for SOAP (JAX-WS) clients using Apache CXF.
 *
 * @since 3.0.0
 */
@Configuration
public class CxfWsClientAutoConfiguration {

  /**
   * @return an implemenation of {@link SyncServiceClientFactory} based on CXF for SAOP (JAX-WS).
   */
  @Bean
  public SyncServiceClientFactory syncServiceClientFactoryCxfWs() {

    return new SyncServiceClientFactoryCxfWs();
  }

}
