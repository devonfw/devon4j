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
}
