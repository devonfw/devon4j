package com.devonfw.module.security.jwt;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.devonfw.module.security.jwt.filter.JwtAuthencticationFilter;

/**
 * @author VAPADWAL
 *
 */
public class JwtWebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${security.cors.enabled}")
  boolean corsEnabled = true;

  @Inject
  private UserDetailsService userDetailsService;

  @Inject
  private PasswordEncoder passwordEncoder;

  /*
   * private CorsFilter getCorsFilter() {
   *
   * UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); CorsConfiguration config = new
   * CorsConfiguration(); config.setAllowCredentials(true); config.addAllowedOrigin("*"); config.addAllowedHeader("*");
   * config.addAllowedMethod("OPTIONS"); config.addAllowedMethod("HEAD"); config.addAllowedMethod("GET");
   * config.addAllowedMethod("PUT"); config.addAllowedMethod("POST"); config.addAllowedMethod("DELETE");
   * config.addAllowedMethod("PATCH"); source.registerCorsConfiguration("/**", config); return new CorsFilter(source); }
   */

  /**
   * Configure spring security to enable login with JWT.
   */
  @Override
  public void configure(HttpSecurity http) throws Exception {

    String[] unsecuredResources = new String[] { "/login", "/security/**", "/services/rest/login",
    "/services/rest/logout" };

    http.userDetailsService(this.userDetailsService).csrf().disable().exceptionHandling().and().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
        .antMatchers(unsecuredResources).permitAll().antMatchers(HttpMethod.POST, "/login").permitAll().anyRequest()
        .authenticated().and()
        // the api/login requests are filtered with the JWTLoginFilter
        .addFilterBefore(new JwtLoginFilter("/login", authenticationManager()),
            UsernamePasswordAuthenticationFilter.class)
        // other requests are filtered to check the presence of JWT in header
        .addFilterBefore(new JwtAuthencticationFilter(null, null), UsernamePasswordAuthenticationFilter.class);

    if (this.corsEnabled) {
      // http.addFilterBefore(getCorsFilter(), CsrfFilter.class);
    }
  }

  @Override
  @SuppressWarnings("javadoc")
  public void configure(AuthenticationManagerBuilder auth) throws Exception {

    auth.inMemoryAuthentication().withUser("waiter").password(this.passwordEncoder.encode("waiter")).roles("Waiter")
        .and().withUser("cook").password(this.passwordEncoder.encode("cook")).roles("Cook").and().withUser("barkeeper")
        .password(this.passwordEncoder.encode("barkeeper")).roles("Barkeeper").and().withUser("chief")
        .password(this.passwordEncoder.encode("chief")).roles("Chief");
  }

}
