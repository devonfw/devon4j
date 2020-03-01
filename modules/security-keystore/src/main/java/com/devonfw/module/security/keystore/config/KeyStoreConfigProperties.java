package com.devonfw.module.security.keystore.config;

import java.security.KeyStore;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class contains properties related with {@link KeyStore}.
 *
 * @since 3.3.0
 *
 */
@Configuration
@ConfigurationProperties(prefix = "security.authentication.keystore")
public class KeyStoreConfigProperties {

  private String keyStoreLocation;

  private String keystoreType;

  private String password;

  private String keyAlias;

  private String keyStoreAlias;

  /**
   * @return the location of keyStore file (e.g. .JKS)
   */
  public String getKeyStoreLocation() {

    return this.keyStoreLocation;
  }

  /**
   * @param keyStoreLocation new value of {@link #getKeyStoreLocation}.
   */
  public void setKeyStoreLocation(String keyStoreLocation) {

    this.keyStoreLocation = keyStoreLocation;
  }

  /**
   * @return the type of KeyStore (e.g. PKCS12)
   */
  public String getKeystoreType() {

    return this.keystoreType;
  }

  /**
   * @param keystoreType new value of {@link #getKeystoreType}.
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
   * @param password new value of {@link #getPassword}.
   */
  public void setPassword(String password) {

    this.password = password;
  }

  /**
   * @return alias of the key, that is used while creating keyStore file
   */
  public String getKeyAlias() {

    return this.keyAlias;
  }

  /**
   * @param keyAlias new value of {@link #getKeyAlias}.
   */
  public void setKeyAlias(String keyAlias) {

    this.keyAlias = keyAlias;
  }

  /**
   * Returns Alias for keyStore (see {@link KeyStoreAccess#getAlias()})
   *
   * default configuration in application.properties - security.authentication.keystore.keystorealias=JWT_DEFAULT
   *
   * @return keyStoreAlias
   */
  public String getKeyStoreAlias() {

    return this.keyStoreAlias;
  }

  /**
   * @param keyStoreAlias new value of {@link #getKeyStoreAlias}.
   */
  public void setKeyStoreAlias(String keyStoreAlias) {

    this.keyStoreAlias = keyStoreAlias;
  }

}
