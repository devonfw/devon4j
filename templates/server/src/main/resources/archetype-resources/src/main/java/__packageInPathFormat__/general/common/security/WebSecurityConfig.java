package ${package}.general.common.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import ${package}.general.common.SpringProfileConstants;

/**
 * Security configuration based on {@link BaseWebSecurityConfig}. This configuration is by purpose designed most simple
 * for two channels of authentication: simple login form and rest-url.
 */
@Configuration
@EnableWebSecurity
@Profile(SpringProfileConstants.NOT_JUNIT)
public class WebSecurityConfig extends BaseWebSecurityConfig {

  /**
   * The constructor.
   */
  public WebSecurityConfig() {

    super();
  }

}
