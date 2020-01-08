package com.devonfw.module.security.jwt.util;

import java.security.SignatureException;

/**
 * Enum representation of standard JWT signature algorithm
 *
 * @since 3.2.0
 */
public enum JwtSignatureAlgorithm {

  /** for No digital signature or MAC performed */
  NONE("none", "No digital signature or MAC performed", "None", null, false),

  /** algorithm name for HMAC using SHA-256 */
  HS256("HS256", "HMAC using SHA-256", "HMAC", "HmacSHA256", true),

  /** algorithm name for HMAC using SHA-384 */
  HS384("HS384", "HMAC using SHA-384", "HMAC", "HmacSHA384", true),

  /** algorithm name for HMAC using SHA-512 */
  HS512("HS512", "HMAC using SHA-512", "HMAC", "HmacSHA512", true),

  /** algorithm name for RSASSA-PKCS-v1_5 using SHA-256 */
  RS256("RS256", "RSASSA-PKCS-v1_5 using SHA-256", "RSA", "SHA256withRSA", true),

  /** algorithm name for RSASSA-PKCS-v1_5 using SHA-384 */
  RS384("RS384", "RSASSA-PKCS-v1_5 using SHA-384", "RSA", "SHA384withRSA", true),

  /** algorithm name for RSASSA-PKCS-v1_5 using SHA-512 */
  RS512("RS512", "RSASSA-PKCS-v1_5 using SHA-512", "RSA", "SHA512withRSA", true),

  /**
   * JWA algorithm name for {@code ECDSA using P-256 and SHA-256}. <b>This is not a JDK standard algorithm and requires
   * that a JCA provider like BouncyCastle be in the runtime classpath.</b> BouncyCastle will be used automatically if
   * found in the runtime classpath.
   */
  ES256("ES256", "ECDSA using P-256 and SHA-256", "EllipticCurve", "SHA256withECDSA", false),

  /**
   * JWA algorithm name for {@code ECDSA using P-384 and SHA-384}. <b>This is not a JDK standard algorithm and requires
   * that a JCA provider like BouncyCastle be in the runtime classpath.</b> BouncyCastle will be used automatically if
   * found in the runtime classpath.
   */
  ES384("ES384", "ECDSA using P-384 and SHA-384", "EllipticCurve", "SHA384withECDSA", false),

  /**
   * JWA algorithm name for {@code ECDSA using P-512 and SHA-512}. <b>This is not a JDK standard algorithm and requires
   * that a JCA provider like BouncyCastle be in the runtime classpath.</b> BouncyCastle will be used automatically if
   * found in the runtime classpath.
   */
  ES512("ES512", "ECDSA using P-512 and SHA-512", "EllipticCurve", "SHA512withECDSA", false),

  /**
   * JWA algorithm name for {@code RSASSA-PSS using SHA-256 and MGF1 with SHA-256}. <b>This is not a JDK standard
   * algorithm and requires that a JCA provider like BouncyCastle be in the runtime classpath.</b> BouncyCastle will be
   * used automatically if found in the runtime classpath.
   */
  PS256("PS256", "RSASSA-PSS using SHA-256 and MGF1 with SHA-256", "RSA", "SHA256withRSAandMGF1", false),

  /**
   * JWA algorithm name for {@code RSASSA-PSS using SHA-384 and MGF1 with SHA-384}. <b>This is not a JDK standard
   * algorithm and requires that a JCA provider like BouncyCastle be in the runtime classpath.</b> BouncyCastle will be
   * used automatically if found in the runtime classpath.
   */
  PS384("PS384", "RSASSA-PSS using SHA-384 and MGF1 with SHA-384", "RSA", "SHA384withRSAandMGF1", false),

  /**
   * JWA algorithm name for {@code RSASSA-PSS using SHA-512 and MGF1 with SHA-512}. <b>This is not a JDK standard
   * algorithm and requires that a JCA provider like BouncyCastle be in the classpath.</b> BouncyCastle will be used
   * automatically if found in the runtime classpath.
   */
  PS512("PS512", "RSASSA-PSS using SHA-512 and MGF1 with SHA-512", "RSA", "SHA512withRSAandMGF1", false);

  private final String value;

  private final String description;

  private final String familyName;

  private final String jcaName;

  private final boolean jdkStandard;

  private JwtSignatureAlgorithm(String value, String description, String familyName, String jcaName,
      boolean jdkStandard) {

    this.value = value;
    this.description = description;
    this.familyName = familyName;
    this.jcaName = jcaName;
    this.jdkStandard = jdkStandard;
  }

  /**
   * Returns the JWA algorithm name constant.
   *
   * @return value
   */
  public String getValue() {

    return this.value;
  }

  /**
   * Returns the JWA algorithm description.
   *
   * @return description.
   */
  public String getDescription() {

    return this.description;
  }

  /**
   * Returns the cryptographic family name of the signature algorithm.
   *
   * @return familyName.
   */
  public String getFamilyName() {

    return this.familyName;
  }

  /**
   * Returns the name of the JCA algorithm used to compute the signature.
   *
   * @return jcaName.
   */
  public String getJcaName() {

    return this.jcaName;
  }

  /**
   * Returns {@code true} if the algorithm is supported by standard JDK distributions or {@code false} if the algorithm
   * implementation is not in the JDK and must be provided by a separate runtime JCA Provider (like BouncyCastle for
   * example).
   *
   * @return {@code true} if the algorithm is supported by standard JDK distributions or {@code false} if the algorithm
   *         implementation is not in the JDK and must be provided by a separate runtime JCA Provider (like BouncyCastle
   *         for example).
   */
  public boolean isJdkStandard() {

    return this.jdkStandard;
  }

  /**
   * Returns {@code true} if the enum instance represents an HMAC signature algorithm, {@code false} otherwise.
   *
   * @return {@code true} if the enum instance represents an HMAC signature algorithm, {@code false} otherwise.
   */
  public boolean isHmac() {

    return name().startsWith("HS");
  }

  /**
   * Returns {@code true} if the enum instance represents an RSA public/private key pair signature algorithm,
   * {@code false} otherwise.
   *
   * @return {@code true} if the enum instance represents an RSA public/private key pair signature algorithm,
   *         {@code false} otherwise.
   */
  public boolean isRsa() {

    return getDescription().startsWith("RSASSA");
  }

  /**
   * Returns {@code true} if the enum instance represents an Elliptic Curve signature algorithm, {@code false}
   * otherwise.
   *
   * @return {@code true} if the enum instance represents an Elliptic Curve signature algorithm, {@code false}
   *         otherwise.
   */
  public boolean isEllipticCurve() {

    return name().startsWith("ES");
  }

  /**
   * Looks up and returns the corresponding {@code SignatureAlgorithm} enum instance based on a
   * case-<em>insensitive</em> name comparison.
   *
   * @param value The case-insensitive name of the {@code SignatureAlgorithm} instance to return
   * @return the corresponding {@code SignatureAlgorithm} enum instance based on a case-<em>insensitive</em> name
   *         comparison.
   * @throws SignatureException if the specified value does not match any {@code SignatureAlgorithm} name.
   */
  public static JwtSignatureAlgorithm forName(String value) throws SignatureException {

    for (JwtSignatureAlgorithm alg : values()) {
      if (alg.getValue().equalsIgnoreCase(value)) {
        return alg;
      }
    }

    throw new SignatureException("Unsupported signature algorithm '" + value + "'");
  }
}
