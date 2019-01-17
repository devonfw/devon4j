package com.devonfw.module.security.common;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.kerberos.authentication.KerberosAuthenticationProvider;
import org.springframework.security.kerberos.authentication.KerberosServiceAuthenticationProvider;
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosClient;
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosTicketValidator;
import org.springframework.security.kerberos.web.authentication.SpnegoAuthenticationProcessingFilter;
import org.springframework.security.kerberos.web.authentication.SpnegoEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;



/**
 * Configuration and declaration of needed security settings (authorization/authentication) of the application.
 **/
//@Configuration
//@EnableWebSecurity
public class KerberosWebSecurityConfig extends WebSecurityConfigurerAdapter {

  // TODO:: generalise properties injected
  // TODO: Extend SpnegoEntryPoint and create a class where you can handle errors
  // TODO:: AbstractAuthenticationProvider class needs to be created
  // TOD: kerberos config class

//  @Value("${security.cors.enabled}")
  boolean corsEnabled = false;

//  @Inject
//  private UserDetailsService dummyUserDetailsService;

  @Inject
  private KerberosConfigProperties kerbprop ;

//  @Inject
//  AuthenticationManager authenticationManager;

//  @Inject
//  private PasswordEncoder passwordEncoder;

  private CorsFilter getCorsFilter() {

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("OPTIONS");
    config.addAllowedMethod("HEAD");
    config.addAllowedMethod("GET");
    config.addAllowedMethod("PUT");
    config.addAllowedMethod("POST");
    config.addAllowedMethod("DELETE");
    config.addAllowedMethod("PATCH");
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {

    String[] unsecuredResources = new String[] { "/login", "/security/**", "/services/rest/login",
    "/services/rest/logout" };

//    http.httpBasic().authenticationEntryPoint(new SpnegoEntryPoint()).and().userDetailsService(dummyUserDetailsService())
//        .csrf().disable().authorizeRequests().antMatchers(unsecuredResources).permitAll().anyRequest().authenticated()
//        .and().logout().permitAll().and().addFilterBefore(
//            spnegoAuthenticationProcessingFilter(authenticationManagerBean()), BasicAuthenticationFilter.class);

    http.httpBasic().authenticationEntryPoint(new SpnegoEntryPoint()).and().userDetailsService(dummyUserDetailsService())
    .csrf().disable().authorizeRequests().antMatchers(unsecuredResources).permitAll().anyRequest().authenticated()
    .and().logout().permitAll().and().addFilterBefore(
        spnegoAuthenticationProcessingFilter(authenticationManagerBean()), BasicAuthenticationFilter.class);


//    http.exceptionHandling().authenticationEntryPoint(new SpnegoEntryPoint()).and().userDetailsService(userDetailsService())
//    .csrf().disable().authorizeRequests().antMatchers(unsecuredResources).permitAll().anyRequest().authenticated()
//    .and().logout().permitAll().and().addFilterBefore(
//    		spnegoAuthenticationProcessingFilter(authenticationManagerBean()), BasicAuthenticationFilter.class);


//    http
//    //
//    .userDetailsService(this.userDetailsService).exceptionHandling().authenticationEntryPoint(new SpnegoEntryPoint()).and()
//    // define all urls that are not to be secured
//    .authorizeRequests().antMatchers(unsecuredResources).permitAll().anyRequest().authenticated().and()
//
//    // activate crsf check for a selection of urls (but not for login & logout)
//    .csrf().disable()
//
//    // configure parameters for simple form login (and logout)
//    .formLogin().successHandler(new SimpleUrlAuthenticationSuccessHandler()).defaultSuccessUrl("/")
//    .failureUrl("/login.html?error").loginProcessingUrl("/j_spring_security_login").usernameParameter("username")
//    .passwordParameter("password").and()
//    // logout via POST is possible
//    .logout().logoutSuccessUrl("/login.html").and().
//
//    addFilterBefore(
//            spnegoAuthenticationProcessingFilter(authenticationManagerBean()), BasicAuthenticationFilter.class);

    if (this.corsEnabled)
      http.addFilterBefore(getCorsFilter(), CsrfFilter.class);
  }

  @Inject
  protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

    auth.authenticationProvider(kerberosAuthenticationProvider()).authenticationProvider(kerberosServiceAuthenticationProvider());
    //
  }

  @Bean
  public KerberosAuthenticationProvider kerberosAuthenticationProvider() {

    KerberosAuthenticationProvider provider = new KerberosAuthenticationProvider();
    SunJaasKerberosClient client = new SunJaasKerberosClient();
    provider.setKerberosClient(client);
    provider.setUserDetailsService(dummyUserDetailsService());
    return provider;
  }

  @Bean
  public KerberosServiceAuthenticationProvider kerberosServiceAuthenticationProvider() {

    KerberosServiceAuthenticationProvider authenticationProvider = new KerberosServiceAuthenticationProvider();
    SunJaasKerberosTicketValidator ticketValidator = sunJaasKerberosTicketValidator();
    authenticationProvider.setTicketValidator(ticketValidator);
    authenticationProvider.setUserDetailsService(dummyUserDetailsService());
    return authenticationProvider;
  }

  @Bean
  public SunJaasKerberosTicketValidator sunJaasKerberosTicketValidator() {

    SunJaasKerberosTicketValidator ticketValidator = new SunJaasKerberosTicketValidator();
    ticketValidator.setServicePrincipal(this.kerbprop.getServicePrincipalName());
    ticketValidator.setKeyTabLocation(new FileSystemResource(this.kerbprop.getKeytabLocation()));
    ticketValidator.setDebug(true);
    return ticketValidator;
  }

//  @Bean
//  public KerberosFilter kerberosFilter(AuthenticationManager authenticationManager) {
//
//    KerberosFilter filter = new KerberosFilter();
//    filter.setAuthenticationManager(authenticationManager);
//    return filter;
//  }

  @Bean
  public SpnegoAuthenticationProcessingFilter spnegoAuthenticationProcessingFilter(
      AuthenticationManager authenticationManager) {

    SpnegoAuthenticationProcessingFilter filter = new SpnegoAuthenticationProcessingFilter();
    filter.setAuthenticationManager(authenticationManager);
    return filter;
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
      return super.authenticationManagerBean();
  }

  @Bean
  public DummyUserDetailsService dummyUserDetailsService() {
      return new DummyUserDetailsService();
  }

}