package com.devonfw.module.security.jwt.config;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Interface which provides access to {@link KeyStore} functionality. Check {@link KeyStoreAccessImpl} for
 * implementation.
 *
 * @since 3.2.0
 */
public interface KeyStoreAccess {

  /**
   * Loads the public key using keyalias
   *
   * @return {@link PublicKey}
   */
  public PublicKey getPublicKey();

  /**
   * Loads the private key using keyalias
   *
   * @param alias
   * @param password
   * @return {@link PrivateKey}
   */
  public PrivateKey getPrivateKey(String alias, String password);

}
