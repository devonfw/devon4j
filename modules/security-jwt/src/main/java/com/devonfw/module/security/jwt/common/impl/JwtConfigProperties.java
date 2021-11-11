package com.devonfw.module.security.jwt.common.impl;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * {@link ConfigurationProperties} for JSON Web Token (JWT) support.
 *
 * @since 2020.04.001
 */
@Configuration
@ConfigurationProperties(prefix = "security.authentication.jwt")
public class JwtConfigProperties {

  private String algorithm = "RSA";

  private String alias = "jwt";

  private ClaimsConfigProperties claims = new ClaimsConfigProperties();

  private ValidationConfigProperties validation = new ValidationConfigProperties();

  private CreationConfigProperties creation = new CreationConfigProperties();

  /**
   * @return the {@link ValidationConfigProperties configuration for validation of JWTs during authentication}.
   */
  public ValidationConfigProperties getValidation() {

    return this.validation;
  }

  /**
   * @return the {@link CreationConfigProperties configuration for creation of JWTs when issued by this app}. Most
   *         applications only validate JWTs for authentication and do not need this configuration.
   */
  public CreationConfigProperties getCreation() {

    return this.creation;
  }

  /**
   * @return the {@link ClaimsConfigProperties configuration of the JWT claims}.
   */
  public ClaimsConfigProperties getClaims() {

    return this.claims;
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
   * @return the alias of the key from the keystore used to verify or sign the token.
   */
  public String getAlias() {

    return this.alias;
  }

  /**
   * @param alias new value of {@link #getAlias()}.
   */
  public void setAlias(String alias) {

    this.alias = alias;
  }

  /**
   * {@link ConfigurationProperties} for validation of JWTs during authentication.
   *
   * @see JwtConfigProperties#getValidation()
   */
  public static class ValidationConfigProperties {

    private boolean notBeforeRequired = true;

    private boolean expirationRequired = true;

    private Duration maxValidity = Duration.ofHours(12);

    /**
     * @return {@code true} if {@link io.jsonwebtoken.Claims#getNotBefore() not before} has to be present in JWT for
     *         validation.
     */
    public boolean isNotBeforeRequired() {

      return this.notBeforeRequired;
    }

    /**
     * @param notBeforeRequired new value of {@link #isNotBeforeRequired()}.
     */
    public void setNotBeforeRequired(boolean notBeforeRequired) {

      this.notBeforeRequired = notBeforeRequired;
    }

    /**
     * @return {@code true} if
     */
    public boolean isExpirationRequired() {

      return this.expirationRequired;
    }

    /**
     * @param expirationRequired new value of {@link #isExpirationRequired()}.
     */
    public void setExpirationRequired(boolean expirationRequired) {

      this.expirationRequired = expirationRequired;
    }

    /**
     * @return the maximum allowed value for the delay from {@link io.jsonwebtoken.Claims#getIssuedAt() issued at} or
     *         {@link io.jsonwebtoken.Claims#getNotBefore() not before} to {@link io.jsonwebtoken.Claims#getExpiration()
     *         expiration} used for validation of JWT.
     */
    public Duration getMaxValidity() {

      return this.maxValidity;
    }

    /**
     * @param maxExpiration new value of {@link #getMaxValidity()}.
     */
    public void setMaxValidity(Duration maxExpiration) {

      this.maxValidity = maxExpiration;
    }

  }

  /**
   * {@link ConfigurationProperties} for creation of JWTs when issued by this app.
   *
   * @see JwtConfigProperties#getCreation()
   */
  public static class CreationConfigProperties {

    private Duration validity = Duration.ofHours(8);

    private Duration notBeforeDelay = Duration.ofMinutes(5);

    private String issuer = "devonfw";

    private boolean addIssuedAt;

    /**
     * @return the {@link Duration} how long an issued JWT should be valid from now (plus {@link #getNotBeforeDelay()
     *         not before delay}). Used to create {@link io.jsonwebtoken.Claims#getExpiration() expiration}.
     */
    public Duration getValidity() {

      return this.validity;
    }

    /**
     * @param validity new value of {@link #getValidity()}.
     */
    public void setValidity(Duration validity) {

      this.validity = validity;
    }

    /**
     * @return the delay to subtract from {@link java.time.Instant#now() now} to issue
     *         {@link io.jsonwebtoken.Claims#getNotBefore() not before} of JWT (to prevent errors in case clocks of
     *         issuing server and validating server are slightly out of sync).
     */
    public Duration getNotBeforeDelay() {

      return this.notBeforeDelay;
    }

    /**
     * @param notBeforeDelay new value of {@link #getNotBeforeDelay()}.
     */
    public void setNotBeforeDelay(Duration notBeforeDelay) {

      this.notBeforeDelay = notBeforeDelay;
    }

    /**
     * @return name of the {@link io.jsonwebtoken.Claims#getIssuer() issuer} of the token.
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
     * @return {@code true} to also add {@link io.jsonwebtoken.Claims#getIssuedAt() issued at} claim.
     */
    public boolean isAddIssuedAt() {

      return this.addIssuedAt;
    }

    /**
     * @param addIssuedAt new value of {@link #isAddIssuedAt()}.
     */
    public void setAddIssuedAt(boolean addIssuedAt) {

      this.addIssuedAt = addIssuedAt;
    }

  }

  /**
   * {@link ConfigurationProperties} for claims of the JWT.
   *
   * @see JwtConfigProperties#getClaims
   */
  public static class ClaimsConfigProperties {

    private String accessControlsName = "groups";

    private boolean accessControlsArray = true;

    /**
     * @return name of the claim containing the access-controls (groups, roles, permissions). Defaults to "groups"
     *         according to
     *         <a href="https://www.eclipse.org/community/eclipse_newsletter/2017/september/article2.php">MP-JWT</a>
     *         standard.
     */
    public String getAccessControlsName() {

      return this.accessControlsName;
    }

    /**
     * @param accessControlsName new value of {@link #getAccessControlsName()}.
     */
    public void setAccessControlsName(String accessControlsName) {

      this.accessControlsName = accessControlsName;
    }

    /**
     * @return {@code true} if a JSON array should be used to encode the actual items in the
     *         {@link #getAccessControlsName() access controls claim}, {@code false} to use a comma separated JSON
     *         string. Defaults to {@code true} according to
     *         <a href="https://www.eclipse.org/community/eclipse_newsletter/2017/september/article2.php">MP-JWT</a>
     *         standard.
     */
    public boolean isAccessControlsArray() {

      return this.accessControlsArray;
    }

    /**
     * @param accessControlsArray new value of {@link #isAccessControlsArray()}.
     */
    public void setAccessControlsArray(boolean accessControlsArray) {

      this.accessControlsArray = accessControlsArray;
    }

  }

}
