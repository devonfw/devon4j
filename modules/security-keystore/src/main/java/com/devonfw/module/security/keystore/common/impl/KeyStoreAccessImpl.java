package com.devonfw.module.security.keystore.common.impl;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.devonfw.module.security.keystore.common.api.KeyStoreAccess;

/**
 * Implementation of {@link KeyStoreAccess}
 *
 * @since 2020.04.001
 */
@Named
public class KeyStoreAccessImpl implements KeyStoreAccess {

  @Inject
  private KeyStoreConfigProperties keyStoreConfigProperties;

  private KeyStore keyStore;

  /**
   * Gets the instance of {@link KeyStore} and loads the {@link KeyStore} from JKS file
   *
   * @return {@link KeyStore}
   */
  private KeyStore getKeyStore() {

    if (this.keyStore == null) {
      try {
        this.keyStore = KeyStore.getInstance(this.keyStoreConfigProperties.getType());
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        String location = this.keyStoreConfigProperties.getLocation();
        Resource resource = resourceLoader.getResource(location);
        if (!resource.exists()) {
          throw new IllegalStateException("KeyStore (" + location + ") not found!");
        }
        try (InputStream in = resource.getInputStream()) {
          String password = this.keyStoreConfigProperties.getPassword();
          this.keyStore.load(in, password.toCharArray());
        }
      } catch (Exception e) {
        throw new IllegalStateException("Failed to load KeyStore!", e);
      }
    }
    return this.keyStore;
  }

  @Override
  public Certificate getCertificate(String alias) {

    try {
      Certificate certificate = getKeyStore().getCertificate(alias);
      if (certificate == null) {
        throw new IllegalArgumentException(
            "Could not find public key (certificate) for alias (" + alias + ") in KeyStore!");
      }
      return certificate;
    } catch (KeyStoreException e) {
      throw new IllegalStateException("Failed to load public key (" + alias + ") from KeyStore.", e);
    }
  }

  @Override
  public PrivateKey getPrivateKey(String alias) {

    return getPrivateKey(alias, this.keyStoreConfigProperties.getPassword().toCharArray());
  }

  @Override
  public PrivateKey getPrivateKey(String alias, char[] password) {

    Key key = null;
    try {
      key = getKeyStore().getKey(alias, password);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to get the key from KeyStore!", e);
    }
    if (key == null) {
      throw new IllegalArgumentException("Could not find private key for alias (" + alias + ") in KeyStore!");
    }
    return (PrivateKey) key;
  }

}
