package com.devonfw.module.security.jwt.sign;

import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.jwt.crypto.sign.Signer;

/**
 * Interface used in the Signing or Verification process of a Token
 *
 * @since 3.2.0
 */
public interface JwtSignatureAlgorithm {

  /**
   * Returns the Name of Algorithm
   *
   * @return {@link String}
   */
  public String getName();

  /**
   * Returns an instance of {@link Signer} for specific Algorithm.
   *
   * @return {@link Signer}
   */
  public Signer createSigner();

  /**
   * Returns an instance of {@link SignatureVerifier} for specific Algorithm.
   *
   * @return {@link SignatureVerifier}
   */
  public SignatureVerifier createVerifier();

}
