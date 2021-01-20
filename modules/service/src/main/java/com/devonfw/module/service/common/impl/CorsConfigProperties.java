package com.devonfw.module.service.common.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 *
 */
@ConfigurationProperties
public class CorsConfigProperties {
  private boolean allowCredentials;

  private String allowedOrigin;

  private String allowedHeader;

  private String allowedMethods;

  /**
   * @return allowCredentials
   */
  public boolean isAllowCredentials() {

    return this.allowCredentials;
  }

  /**
   * @param allowCredentials new value of {@link #getallowCredentials}.
   */
  public void setAllowCredentials(boolean allowCredentials) {

    this.allowCredentials = allowCredentials;
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
