package com.devonfw.module.security.keystore.common.api;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

/**
 * Interface which provides access to {@link java.security.KeyStore} functionality.
 *
 * @since 2020.04.001
 */
public interface KeyStoreAccess {

  /**
   * Loads the {@link PublicKey} for the given {@code alias}.
   *
   * @param alias the alias of the {@link PublicKey}.
   * @return the requested {@link PublicKey}
   */
  Certificate getCertificate(String alias);

  /**
   * Loads the {@link PublicKey} for the given {@code alias}.
   *
   * @param alias the alias of the {@link PublicKey}.
   * @return the requested {@link PublicKey}
   */
  default PublicKey getPublicKey(String alias) {

    return getCertificate(alias).getPublicKey();
  }

  /**
   * Loads the {@link PrivateKey} for the given {@code alias} using the same password as for keystore.
   *
   * @param alias the alias of the {@link PrivateKey}.
   * @return the requested {@link PrivateKey}.
   */
  PrivateKey getPrivateKey(String alias);

  /**
   * Loads the {@link PrivateKey} for the given {@code alias}.
   *
   * @param alias the alias of the {@link PrivateKey}.
   * @param password the password the {@link PrivateKey} is encrypted with.
   * @return the requested {@link PrivateKey}.
   */
  PrivateKey getPrivateKey(String alias, char[] password);

}
