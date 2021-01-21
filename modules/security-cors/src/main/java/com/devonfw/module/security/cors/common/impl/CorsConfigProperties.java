package com.devonfw.module.security.cors.common.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

/**
 * CorsConfigProperties to keep the CORS cofigurations.
 *
 */
@ConfigurationProperties(prefix = "security.cors")
public class CorsConfigProperties extends CorsConfiguration {
}
