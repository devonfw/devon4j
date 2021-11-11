package com.devonfw.module.security.cors.common.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

/**
 * {@link ConfigurationProperties} to configure Cross-Origin Resource Sharing (CORS).
 *
 * @since 2020.12.002
 */
@ConfigurationProperties(prefix = "security.cors")
public class CorsConfigProperties extends CorsConfiguration {
  private CorsConfiguration spring;

  private String pathPattern = "/**";

  /**
   * @return the {@link CorsConfiguration} from spring-web.
   */
  public CorsConfiguration getSpring() {

    return this.spring;
  }

  /**
   * Sets the cors configuration from the properties file.
   *
   * @param spring
   * @return
   */
  public void setSpring(CorsConfiguration spring) {

    this.spring = spring;
  }

  /**
   * @return the ant-style pattern for the URL paths where to apply CORS. Use "/**" to match all URL paths.
   * @see org.springframework.web.cors.UrlBasedCorsConfigurationSource#registerCorsConfiguration(String,
   *      CorsConfiguration)
   */
  public String getPathPattern() {

    return this.pathPattern;
  }

  /**
   * @param pathPattern the new value of {@link #getPathPattern()}.
   */
  public void setPathPattern(String pathPattern) {

    this.pathPattern = pathPattern;
  }
}
