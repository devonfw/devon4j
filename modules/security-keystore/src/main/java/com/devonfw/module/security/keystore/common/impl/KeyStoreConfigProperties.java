package com.devonfw.module.security.keystore.common.impl;

import java.security.KeyStore;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.security.keystore.common.api.KeyStoreAccess;

/**
 * This class contains the configuration for {@link KeyStoreAccess}.
 *
 * @since 2020.04.001
 */
@Configuration
@ConfigurationProperties(prefix = "security.keystore")
public class KeyStoreConfigProperties {

  private String location;

  private String type = "PKCS12";

  private String password;

  /**
   * @return the location of {@link KeyStore} as a spring resource URI (e.g. starting with "file://" or "classpath:").
   */
  public String getLocation() {

    return this.location;
  }

  /**
   * @param keyStoreLocation new value of {@link #getLocation()}.
   */
  public void setLocation(String keyStoreLocation) {

    this.location = keyStoreLocation;
  }

  /**
   * @return the type of {@link java.security.KeyStore} (e.g. "PKCS12", "JKS", or "JCEKS")
   */
  public String getType() {

    return this.type;
  }

  /**
   * @param keystoreType new value of {@link #getType()}.
   */
  public void setType(String keystoreType) {

    this.type = keystoreType;
  }

  /**
   * @return the password of the {@link java.security.KeyStore} itself. Please consider <a href=
   *         "https://github.com/devonfw/devon4j/blob/develop/documentation/guide-configuration.asciidoc#password-encryption">encryption
   *         of sensitive configuration values</a>.
   */
  public String getPassword() {

    return this.password;
  }

  /**
   * @param password new value of {@link #getPassword()}.
   */
  public void setPassword(String password) {

    this.password = password;
  }

}
