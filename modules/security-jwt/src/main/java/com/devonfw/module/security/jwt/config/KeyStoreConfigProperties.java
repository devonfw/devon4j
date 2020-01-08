package com.devonfw.module.security.jwt.config;

import java.security.KeyStore;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class contains properties related with {@link KeyStore}.
 *
 * @since 3.2.0
 *
 */

@Configuration
@ConfigurationProperties(prefix = "security.authentication.keystore")
public class KeyStoreConfigProperties {

  private String keyStoreLocation;

  private String keystoreType;

  private String password;

  private String keyAlias;

  private String keyPassword;

  /**
   * @return keyStoreLocation
   */
  public String getKeyStoreLocation() {

    return this.keyStoreLocation;
  }

  /**
   * @param keyStoreLocation new value of {@link #getkeyStoreLocation}.
   */
  public void setKeyStoreLocation(String keyStoreLocation) {

    this.keyStoreLocation = keyStoreLocation;
  }

  /**
   * @return keystoreType
   */
  public String getKeystoreType() {

    return this.keystoreType;
  }

  /**
   * @param keystoreType new value of {@link #getkeystoreType}.
   */
  public void setKeystoreType(String keystoreType) {

    this.keystoreType = keystoreType;
  }

  /**
   * @return password
   */
  public String getPassword() {

    return this.password;
  }

  /**
   * @param password new value of {@link #getpassword}.
   */
  public void setPassword(String password) {

    this.password = password;
  }

  /**
   * @return keyAlias
   */
  public String getKeyAlias() {

    return this.keyAlias;
  }

  /**
   * @param keyAlias new value of {@link #getkeyAlias}.
   */
  public void setKeyAlias(String keyAlias) {

    this.keyAlias = keyAlias;
  }

  /**
   * @return keyPassword
   */
  public String getKeyPassword() {

    return this.keyPassword;
  }

  /**
   * @param keyPassword new value of {@link #getkeyPassword}.
   */
  public void setKeyPassword(String keyPassword) {

    this.keyPassword = keyPassword;
  }
}
