package com.devonfw.module.security.cors.common.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * CorsConfigProperties to keep the CORS cofigurations.
 *
 */
@Configuration
@ConfigurationProperties(prefix = "security.cors")
public class CorsConfigProperties {

  private boolean allowCredentials;

  @Value("#{'${security.cors.allowedOrigins}'.split(',')}")
  private List<String> allowedOrigins;

  @Value("#{'${security.cors.allowedHeaders}'.split(',')}")
  private List<String> allowedHeaders;

  @Value("#{'${security.cors.allowedMethods}'.split(',')}")
  private List<String> allowedMethods;

  /**
   * Decides the browser should include any cookies associated with the request.
   *
   * @return allowCredentials
   * @see org.springframework.web.cors.CorsConfiguration#isAllowCredentials()
   */
  public boolean isAllowCredentials() {

    return this.allowCredentials;
  }

  /**
   * Decides the browser should include any cookies associated with the request.
   *
   * @param allowCredentials new value of {@link #getallowCredentials}.
   * @see org.springframework.web.cors.CorsConfiguration#setAllowCredentials()
   */
  public void setAllowCredentials(boolean allowCredentials) {

    this.allowCredentials = allowCredentials;
  }

  /**
   * Get list of all allowed origins.
   *
   * @return allowedOrigins
   * @see org.springframework.web.cors.CorsConfiguration#getAllowedOrigins()
   */
  public List<String> getAllowedOrigins() {

    return this.allowedOrigins;
  }

  /**
   * Set list of all allowed origins.
   *
   * @param allowedOrigins new value of {@link #getallowedOrigins}.
   * @see org.springframework.web.cors.CorsConfiguration#setAllowedOrigins()
   */
  public void setAllowedOrigins(List<String> allowedOrigins) {

    this.allowedOrigins = allowedOrigins;
  }

  /**
   * Get list of headers that can be used during the request.
   *
   * @return allowedHeaders
   */
  public List<String> getAllowedHeaders() {

    return this.allowedHeaders;
  }

  /**
   * Set list of headers that can be used during the request.
   *
   * @param allowedHeaders new value of {@link #getallowedHeaders}.
   */
  public void setAllowedHeaders(List<String> allowedHeaders) {

    this.allowedHeaders = allowedHeaders;
  }

  /**
   * Gets list of HTTP request methods.
   *
   * @return allowedMethods
   */
  public List<String> getAllowedMethods() {

    return this.allowedMethods;
  }

  /**
   * Set list of HTTP request methods.
   *
   * @param allowedMethods new value of {@link #getallowedMethods}.
   */
  public void setAllowedMethods(List<String> allowedMethods) {

    this.allowedMethods = allowedMethods;
  }

}
