package com.devonfw.module.security.cors.common.impl;

/**
 * @author sujithna
 *
 */
public class CorsProperties {
  private Boolean allowcredentials;

  private String allowedOrigin;

  private String allowedHeader;

  private String allowedMethods;

  /**
   * @return allowcredentials
   */
  public Boolean getAllowcredentials() {

    return this.allowcredentials;
  }

  /**
   * @param allowcredentials new value of {@link #getallowcredentials}.
   */
  public void setAllowcredentials(Boolean allowcredentials) {

    this.allowcredentials = allowcredentials;
  }

  /**
   * @return allowedOrigin
   */
  public String getAllowedOrigin() {

    return this.allowedOrigin;
  }

  /**
   * @param allowedOrigin new value of {@link #getallowedOrigin}.
   */
  public void setAllowedOrigin(String allowedOrigin) {

    this.allowedOrigin = allowedOrigin;
  }

  /**
   * @return allowedHeader
   */
  public String getAllowedHeader() {

    return this.allowedHeader;
  }

  /**
   * @param allowedHeader new value of {@link #getallowedHeader}.
   */
  public void setAllowedHeader(String allowedHeader) {

    this.allowedHeader = allowedHeader;
  }

  /**
   * @return allowedMethods
   */
  public String getAllowedMethods() {

    return this.allowedMethods;
  }

  /**
   * @param allowedMethods new value of {@link #getallowedMethods}.
   */
  public void setAllowedMethods(String allowedMethods) {

    this.allowedMethods = allowedMethods;
  }

}
