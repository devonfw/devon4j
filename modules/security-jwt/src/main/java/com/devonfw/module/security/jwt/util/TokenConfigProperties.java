package com.devonfw.module.security.jwt.util;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.security.jwt.authentication.AuthenticationTokenDetails;

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

  private String tokenPrefix;

  private String headerString;

  private String exposeHeaders;

  private String algorithm;

  private String tokenDetailsName;

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
   * @return The "Bearer" as token prefix (see <a href="https://tools.ietf.org/html/rfc6750">Bearer Token usage</a> for
   *         details)
   */
  public String getTokenPrefix() {

    return this.tokenPrefix;
  }

  /**
   * @param tokenPrefix new value of {@link #getTokenPrefix()}.
   */
  public void setTokenPrefix(String tokenPrefix) {

    this.tokenPrefix = tokenPrefix;
  }

  /**
   * @return The "Authorization" as header String
   */
  public String getHeaderString() {

    return this.headerString;
  }

  /**
   * @param headerString new value of {@link #getHeaderString()}.
   */
  public void setHeaderString(String headerString) {

    this.headerString = headerString;
  }

  /**
   * @return The "Access-Control-Expose-Headers" response header indicates which headers can be exposed as part of the
   *         response
   */
  public String getExposeHeaders() {

    return this.exposeHeaders;
  }

  /**
   * @param exposeHeaders new value of {@link #getExposeHeaders()}.
   */
  public void setExposeHeaders(String exposeHeaders) {

    this.exposeHeaders = exposeHeaders;
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

  /**
   * default configuration in application.properties - security.authentication.token.tokendetailsname=JWT_DEFAULT
   *
   * @return configured token details for (see {@link AuthenticationTokenDetails#getName()})
   */
  public String getTokenDetailsName() {

    return this.tokenDetailsName;
  }

  /**
   * @param tokenDetailsName new value of {@link #gettokenDetailsName}.
   */
  public void setTokenDetailsName(String tokenDetailsName) {

    this.tokenDetailsName = tokenDetailsName;
  }

}
