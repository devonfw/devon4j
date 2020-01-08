package com.devonfw.module.security.jwt.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class contain properties related to {@link TokenAuthenticationUtil}
 *
 * @since 3.2.0
 */
@Configuration
@ConfigurationProperties(prefix = "security.authentication.token")
public class TokenConfigProperties {

  private String issuer;

  private Integer expiratioHours;

  private String tokenPrefix;

  private String headerString;

  private String headerOtp;

  private String exposeHeaders;

  private String claimSubject;

  private String claimIssuer;

  private String claimExpiration;

  private String claimCreated;

  private String claimScope;

  private String claimRoles;

  /**
   * @return issuer
   */
  public String getIssuer() {

    return this.issuer;
  }

  /**
   * @param issuer new value of {@link #getissuer}.
   */
  public void setIssuer(String issuer) {

    this.issuer = issuer;
  }

  /**
   * @return expiratioHours
   */
  public Integer getExpiratioHours() {

    return this.expiratioHours;
  }

  /**
   * @param expiratioHours new value of {@link #getexpiratioHours}.
   */
  public void setExpiratioHours(Integer expiratioHours) {

    this.expiratioHours = expiratioHours;
  }

  /**
   * @return tokenPrefix
   */
  public String getTokenPrefix() {

    return this.tokenPrefix;
  }

  /**
   * @param tokenPrefix new value of {@link #gettokenPrefix}.
   */
  public void setTokenPrefix(String tokenPrefix) {

    this.tokenPrefix = tokenPrefix;
  }

  /**
   * @return headerString
   */
  public String getHeaderString() {

    return this.headerString;
  }

  /**
   * @param headerString new value of {@link #getheaderString}.
   */
  public void setHeaderString(String headerString) {

    this.headerString = headerString;
  }

  /**
   * @return headerOtp
   */
  public String getHeaderOtp() {

    return this.headerOtp;
  }

  /**
   * @param headerOtp new value of {@link #getheaderOtp}.
   */
  public void setHeaderOtp(String headerOtp) {

    this.headerOtp = headerOtp;
  }

  /**
   * @return exposeHeaders
   */
  public String getExposeHeaders() {

    return this.exposeHeaders;
  }

  /**
   * @param exposeHeaders new value of {@link #getexposeHeaders}.
   */
  public void setExposeHeaders(String exposeHeaders) {

    this.exposeHeaders = exposeHeaders;
  }

  /**
   * @return claimSubject
   */
  public String getClaimSubject() {

    return this.claimSubject;
  }

  /**
   * @param claimSubject new value of {@link #getclaimSubject}.
   */
  public void setClaimSubject(String claimSubject) {

    this.claimSubject = claimSubject;
  }

  /**
   * @return claimIssuer
   */
  public String getClaimIssuer() {

    return this.claimIssuer;
  }

  /**
   * @param claimIssuer new value of {@link #getclaimIssuer}.
   */
  public void setClaimIssuer(String claimIssuer) {

    this.claimIssuer = claimIssuer;
  }

  /**
   * @return claimExpiration
   */
  public String getClaimExpiration() {

    return this.claimExpiration;
  }

  /**
   * @param claimExpiration new value of {@link #getclaimExpiration}.
   */
  public void setClaimExpiration(String claimExpiration) {

    this.claimExpiration = claimExpiration;
  }

  /**
   * @return claimCreated
   */
  public String getClaimCreated() {

    return this.claimCreated;
  }

  /**
   * @param claimCreated new value of {@link #getclaimCreated}.
   */
  public void setClaimCreated(String claimCreated) {

    this.claimCreated = claimCreated;
  }

  /**
   * @return claimScope
   */
  public String getClaimScope() {

    return this.claimScope;
  }

  /**
   * @param claimScope new value of {@link #getclaimScope}.
   */
  public void setClaimScope(String claimScope) {

    this.claimScope = claimScope;
  }

  /**
   * @return claimRoles
   */
  public String getClaimRoles() {

    return this.claimRoles;
  }

  /**
   * @param claimRoles new value of {@link #getclaimRoles}.
   */
  public void setClaimRoles(String claimRoles) {

    this.claimRoles = claimRoles;
  }

}
