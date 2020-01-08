package com.devonfw.module.security.jwt.util;

/**
 * Enum defining available Algorithms for JWT comparison.
 *
 * @since 3.2.0
 */
public enum SignatureVerifierAlgorithm {

  /**
   * Matches if Algorithm is equal to EllipticCurve
   */
  ELLIPTICCURVE("EllipticCurve"),

  /**
   * Matches if Algorithm is equal to HMAC
   */
  HMAC("HMAC"),

  /**
   * Matches if Algorithm is equal to RSA
   */
  RSA("RSA");

  private final String algorithm;

  private SignatureVerifierAlgorithm(String algorithm) {

    this.algorithm = algorithm;
  }

  @Override
  public String toString() {

    return this.algorithm;
  }
}
