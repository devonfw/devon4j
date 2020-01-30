package com.devonfw.module.security.jwt.sign;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.jwt.crypto.sign.Signer;

import com.devonfw.module.security.keystore.config.KeyStoreAccess;

/**
 * Implementation of {@link JwtSignatureAlgorithm}
 *
 * @since 3.2.0
 *
 */
@Named
public class RsaJwtSignatureAlgorithmImpl implements JwtSignatureAlgorithm {

  private final static String ALGORITHM = "RSA";

  @Inject
  private KeyStoreAccess keyStoreAccess;

  @Override
  public String getName() {

    return ALGORITHM;
  }

  @Override
  public Signer createSigner() {

    return new RsaSigner((RSAPrivateKey) this.keyStoreAccess.getPrivateKey());
  }

  @Override
  public SignatureVerifier createVerifier() {

    return new RsaVerifier((RSAPublicKey) this.keyStoreAccess.getPublicKey());
  }

}
