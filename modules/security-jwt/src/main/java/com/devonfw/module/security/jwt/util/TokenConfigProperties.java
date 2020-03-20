package com.devonfw.module.security.jwt.util;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class contain properties related to {@link JwtAccessTokenConverterImpl}
 *
 * @since 3.3.0
 */
@Configuration
@ConfigurationProperties(prefix = "security.authentication.token")
public class TokenConfigProperties {

  private String issuer;

  private Duration expirationHours;

  private String algorithm;

  /**
   * @return The name of the application that created this JWT (see
   *         <a href="https://tools.ietf.org/html/rfc7519#section-4.1.1">issuer claim</a> for details)
   */
  public String getIssuer() {

    return this.issuer;
  }

  /**
   * @param issuer new value of {@link #getIssuer()}.
   */
  public void setIssuer(String issuer) {

    this.issuer = issuer;
  }

  /**
   * @return The expiration hour used combination with expiration time "exp" , after which the JWT must not be accepted
   *         for processing (see <a href="https://tools.ietf.org/html/rfc7519#section-4.1.4">Expiration Time Claim</a>
   *         for details)
   */
  public Duration getExpirationHours() {

    return this.expirationHours;
  }

  /**
   * @param expirationHours new value of {@link #getExpirationHours()}.
   */
  public void setExpirationHours(Duration expirationHours) {

    this.expirationHours = expirationHours;
  }

  /**
   * @return algorithm, which can be configured
   */
  public String getAlgorithm() {

    return this.algorithm;
  }

  /**
   * @param algorithm new value of {@link #getAlgorithm()}.
   */
  public void setAlgorithm(String algorithm) {

    this.algorithm = algorithm;
  }

}
