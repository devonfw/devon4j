package com.devonfw.module.security.common;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Login Filter for Json Web Token
 *
 */
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

  /**
   * The constructor.
   *
   * @param url the login url
   * @param authManager the {@link AuthenticationManager}
   */
  public JWTLoginFilter(String url, AuthenticationManager authManager) {

    super(new AntPathRequestMatcher(url));
    setAuthenticationManager(authManager);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
      throws AuthenticationException, IOException, ServletException {

    AccountCredentials creds = new ObjectMapper().readValue(req.getInputStream(), AccountCredentials.class);
    return getAuthenticationManager()
        .authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword()));
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
      Authentication auth) throws IOException, ServletException {

    TokenAuthenticationService.addAuthentication(res, auth);
  }
}
