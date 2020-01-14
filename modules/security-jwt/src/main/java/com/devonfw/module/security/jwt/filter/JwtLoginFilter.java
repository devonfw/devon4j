package com.devonfw.module.security.jwt.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.devonfw.module.security.jwt.config.KeyStoreAccess;
import com.devonfw.module.security.jwt.config.KeyStoreConfigProperties;
import com.devonfw.module.security.jwt.util.AccountCredentials;
import com.devonfw.module.security.jwt.util.TokenAuthenticationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * The filter requires that you set the authenticationManager property. An AuthenticationManager is required to process
 * the authentication request tokens created by implementing classes.
 *
 * This filter will intercept a request and attempt to perform authentication from that request if the request matches
 * the setRequiresAuthenticationRequestMatcher(RequestMatcher).
 *
 * Authentication is performed by the attemptAuthentication method, which must be implemented by subclasses.
 *
 * @since 3.2.0
 *
 */
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

  /**
   * Logger instance.
   */
  private static final Logger LOG = LoggerFactory.getLogger(JwtLoginFilter.class);

  private UserDetailsService userDetailsService;

  @Inject
  private KeyStoreAccess keyStoreAccess;

  @Inject
  private KeyStoreConfigProperties keyStoreConfigProperties;

  @Inject
  private TokenAuthenticationUtil tokenAuthenticationUtil;

  public JwtLoginFilter(String url, AuthenticationManager authManager, UserDetailsService userDetailsService) {

    super(new AntPathRequestMatcher(url));
    setAuthenticationManager(authManager);
    this.userDetailsService = userDetailsService;

  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
      throws AuthenticationException, IOException, ServletException {

    AccountCredentials creds = new ObjectMapper().readValue(req.getInputStream(), AccountCredentials.class);
    UserDetails user = this.userDetailsService.loadUserByUsername(creds.getUsername());
    return getAuthenticationManager().authenticate(
        new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword(), user.getAuthorities()));
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
      Authentication auth) throws IOException, ServletException {

    this.tokenAuthenticationUtil.addAuthentication(res, auth);
    this.tokenAuthenticationUtil.addAllowedHeader(res);
    this.tokenAuthenticationUtil.addRequiredAuthentication(res, auth);

  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse res,
      AuthenticationException failed) {

    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }

}
