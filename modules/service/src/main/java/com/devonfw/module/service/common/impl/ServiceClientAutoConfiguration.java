package com.devonfw.module.service.common.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.service.common.api.client.ServiceClientFactory;
import com.devonfw.module.service.common.api.client.discovery.ServiceDiscoverer;
import com.devonfw.module.service.common.api.config.ServiceConfig;
import com.devonfw.module.service.common.api.header.ServiceHeaderCustomizer;
import com.devonfw.module.service.common.base.config.ServiceConfigProperties;
import com.devonfw.module.service.common.impl.discovery.ServiceDiscovererImplConfig;
import com.devonfw.module.service.common.impl.header.ServiceHeaderCustomizerAuthForward;
import com.devonfw.module.service.common.impl.header.ServiceHeaderCustomizerBasicAuth;
import com.devonfw.module.service.common.impl.header.ServiceHeaderCustomizerCorrelationId;
import com.devonfw.module.service.common.impl.header.ServiceHeaderCustomizerOAuth;

/**
 * {@link Configuration} for {@link ServiceClientFactory}.
 *
 * @since 3.0.0
 */
@Configuration
public class ServiceClientAutoConfiguration {

  /**
   * @return the implementation of {@link ServiceClientFactory}.
   */
  @Bean
  public ServiceClientFactory serviceClientFactory() {

    return new ServiceClientFactoryImpl();
  }

  /**
   * @return the implementation of {@link ServiceConfig}.
   */
  @Bean
  public ServiceConfig serviceClientConfig() {

    return new ServiceConfigProperties();
  }

  /**
   * @return an implementation of {@link ServiceDiscoverer} based on {@link #serviceClientConfig()}.
   */
  @Bean
  public ServiceDiscoverer serviceDiscovererConfig() {

    return new ServiceDiscovererImplConfig();
  }

  /**
   * @return an implementation of {@link ServiceHeaderCustomizer} passing correlation ID.
   */
  @Bean
  public ServiceHeaderCustomizer serviceHeaderCustomizerCorrelationId() {

    return new ServiceHeaderCustomizerCorrelationId();
  }

  /**
   * @return an implementation of {@link ServiceHeaderCustomizer} for basic authentication support.
   */
  @Bean
  public ServiceHeaderCustomizer serviceHeaderCustomizerBasicAuth() {

    return new ServiceHeaderCustomizerBasicAuth();
  }

  /**
   * @return an implementation of {@link ServiceHeaderCustomizer} for OAuth support.
   */
  @Bean
  public ServiceHeaderCustomizer serviceHeaderCustomizerOAuth() {

    return new ServiceHeaderCustomizerOAuth();
  }

  /**
   * @return an implementation of {@link ServiceHeaderCustomizer} for JWT support.
   */
  @Bean
  public ServiceHeaderCustomizer serviceHeaderCustomizerAuthForward() {

    return new ServiceHeaderCustomizerAuthForward();
  }
}
