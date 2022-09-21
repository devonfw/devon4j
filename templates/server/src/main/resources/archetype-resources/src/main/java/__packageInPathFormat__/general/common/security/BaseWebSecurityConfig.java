package ${package}.general.common.security;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.inject.Inject;

/**
 * This type serves as a base class for extensions of the {@code WebSecurityConfigurerAdapter} and provides a default
 * configuration. <br/>
 * Security configuration is based on {@link WebSecurityConfigurerAdapter}. This configuration is by purpose designed
 * most simple for two channels of authentication: simple login form and rest-url.
 */
public abstract class BaseWebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Inject
  private UserDetailsService userDetailsService;

  @Inject
  private PasswordEncoder passwordEncoder;

  /**
   * Configure spring security to enable a simple webform-login + a simple rest login.
   */
  @Override
  public void configure(HttpSecurity http) throws Exception {

    String[] unsecuredResources = new String[] { "/login", "/security/**", "/services/rest/login",
    "/services/rest/logout" };

    // http = http.csrf().requireCsrfProtectionMatcher(this.requestMatcher).and();
    // disable CSRF protection by default
    http = http.csrf().disable().userDetailsService(this.userDetailsService)
        // define all urls that are not to be secured
        .authorizeRequests().antMatchers(unsecuredResources).permitAll().anyRequest().authenticated().and()
        // configure parameters for simple form login (and logout)
        .formLogin().successHandler(new SimpleUrlAuthenticationSuccessHandler()).defaultSuccessUrl("/")
        .failureUrl("/login.html?error").loginProcessingUrl("/j_spring_security_login").usernameParameter("username")
        .passwordParameter("password").and()
        // logout via POST is possible
        .logout().logoutSuccessUrl("/login.html").and();
  }

  @SuppressWarnings("javadoc")
  @Inject
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

    auth.inMemoryAuthentication().withUser("admin").password(this.passwordEncoder.encode("admin"))
        .authorities("basic.Admin");
  }

}
