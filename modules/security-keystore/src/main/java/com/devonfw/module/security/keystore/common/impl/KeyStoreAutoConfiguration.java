package com.devonfw.module.security.keystore.common.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.security.keystore.common.api.KeyStoreAccess;

/**
 * {@link Configuration} for standardized {@link java.security.KeyStore} support.
 *
 * @since 2020.04.001
 */
@Configuration
public class KeyStoreAutoConfiguration {

  /**
   * @return the implementation of {@link KeyStoreAccess}.
   */
  @Bean
  public KeyStoreAccess keyStoreAccess() {

    return new KeyStoreAccessImpl();
  }

  /**
   * @return the {@link KeyStoreConfigProperties} mapped for spring {@code application.properties}.
   */
  @Bean
  public KeyStoreConfigProperties keyStoreConfigProperties() {

    return new KeyStoreConfigProperties();
  }

}
