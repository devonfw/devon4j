package com.devonfw.module.security.cors.common.impl;

import static java.util.Arrays.asList;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * CorsConfigProperties to keep the CORS cofigurations.
 *
 */
@Configuration
@ConfigurationProperties(prefix = "security.cors")
public class CorsConfigProperties {

  private static final String DELIMITER = "\\,";

  private boolean allowCredentials;

  private String allowedOrigins;

  private String allowedHeaders;

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
   * @return allowedOrigins
   */
  public List<String> getAllowedOrigins() {

    if (StringUtils.hasText(this.allowedOrigins)) {
      return asList(this.allowedOrigins.split(DELIMITER));
    }
    return null;
  }

  /**
   * @param allowedOrigins new value of {@link #getallowedOrigins}.
   */
  public void setAllowedOrigins(String allowedOrigins) {

    this.allowedOrigins = allowedOrigins;
  }

  /**
   * @return allowedHeaders
   */
  public List<String> getAllowedHeaders() {

    if (StringUtils.hasText(this.allowedHeaders)) {
      return asList(this.allowedHeaders.split(DELIMITER));
    }
    return null;

  }

  /**
   * @param allowedHeaders new value of {@link #getallowedHeaders}.
   */
  public void setAllowedHeaders(String allowedHeaders) {

    this.allowedHeaders = allowedHeaders;
  }

  /**
   * @return allowedMethods
   */
  public List<String> getAllowedMethods() {

    if (StringUtils.hasText(this.allowedMethods)) {
      return asList(this.allowedMethods.split(DELIMITER));
    }
    return null;
  }

  /**
   * @param allowedMethods new value of {@link #getallowedMethods}.
   */
  public void setAllowedMethods(String allowedMethods) {

    this.allowedMethods = allowedMethods;
  }

}
