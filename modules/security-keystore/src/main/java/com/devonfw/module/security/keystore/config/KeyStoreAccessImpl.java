package com.devonfw.module.security.keystore.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * Implementation of {@link KeyStoreAccess}
 *
 * @since 3.2.0
 *
 */
@Named
public class KeyStoreAccessImpl implements KeyStoreAccess {

  private static final Logger LOG = LoggerFactory.getLogger(KeyStoreAccessImpl.class);

  @Inject
  private KeyStoreConfigProperties keyStoreConfigProperties;

  /**
   * Gets the instance of {@link KeyStore} and loads the {@link KeyStore} from JKS file
   *
   * @return {@link KeyStore}
   */
  private KeyStore getKeyStore() {

    KeyStore keyStore = null;
    try {

      keyStore = KeyStore.getInstance(this.keyStoreConfigProperties.getKeystoreType());

      Resource keyStoreLocation = new FileSystemResource(new File(this.keyStoreConfigProperties.getKeyStoreLocation()));
      try (InputStream in = keyStoreLocation.getInputStream()) {

        keyStore.load(in, this.keyStoreConfigProperties.getPassword().toCharArray()); // "changeit".toCharArray()

        LOG.info("Keystore aliases " + keyStore.aliases().nextElement().toString());
      } catch (IOException | NoSuchAlgorithmException | CertificateException e) {

        throw new IllegalStateException("Failed to load the KeyStore!", e);
      }
    } catch (KeyStoreException e) {

      throw new IllegalStateException("Failed to instantiate the KeyStore!", e);
    }
    return keyStore;
  }

  @Override
  public PublicKey getPublicKey() {

    PublicKey publicKey = null;
    try {
      Certificate certificate = getKeyStore().getCertificate(this.keyStoreConfigProperties.getKeyAlias());
      publicKey = certificate.getPublicKey();
    } catch (KeyStoreException e) {
      throw new IllegalStateException("keystore has not been initialized .", e);
    }
    return publicKey;
  }

  @Override
  public PrivateKey getPrivateKey() {

    Key key = null;
    try {
      key = getKeyStore().getKey(this.keyStoreConfigProperties.getKeyAlias(),
          this.keyStoreConfigProperties.getPassword().toCharArray());

    } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
      throw new IllegalStateException("Failed to get the key from KeyStore!", e);
    }
    return (PrivateKey) key;
  }

}
