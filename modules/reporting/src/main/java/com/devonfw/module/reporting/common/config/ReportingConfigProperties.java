package com.devonfw.module.reporting.common.config;

import java.util.HashMap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Reporting
 */
@Configuration
@ConfigurationProperties(prefix = "jasper.reporting")
public class ReportingConfigProperties {

  private HashMap<String, String> txtConfig;

  /**
   * @return txtConfig
   */

  public HashMap<String, String> getTxtConfig() {

    return this.txtConfig;
  }

  /**
   * @param txtConfig collection of properties
   */
  public void setTxtConfig(HashMap<String, String> txtConfig) {

    this.txtConfig = txtConfig;
  }

}
