package com.devonfw.module.security.jwt.common.base;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.devonfw.module.security.jwt.common.api.AccountCredentials;
import com.devonfw.module.security.jwt.common.api.JwtCreator;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Filter for login with username and password send as JSON that will attempt authentication and on success respond
 * create JWT and send as response for sub-sequent requests.
 *
 * @since 2020.04.001
 */
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

  private UserDetailsService userDetailsService;

  private JwtCreator jwtCreator;

  /**
   * The constructor.
   *
   * @param urlPattern the URL pattern to which this filter shall apply.
   */
  public JwtLoginFilter(String urlPattern) {

    this(new AntPathRequestMatcher(urlPattern));
  }

  /**
   * The constructor.
   *
   * @param requestMatcher the {@link RequestMatcher} to which this filter shall apply.
   */
  public JwtLoginFilter(RequestMatcher requestMatcher) {

    super(requestMatcher);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    AccountCredentials credentials = new ObjectMapper().readValue(request.getInputStream(), AccountCredentials.class);
    UserDetails user = this.userDetailsService.loadUserByUsername(credentials.getUsername());
    return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(),
        credentials.getPassword(), user.getAuthorities()));
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authentication) throws IOException {

    String token = this.jwtCreator.create(authentication);
    response.addHeader(JwtConstants.EXPOSE_HEADERS, JwtConstants.HEADER_AUTHORIZATION);
    response.addHeader(JwtConstants.HEADER_AUTHORIZATION, JwtConstants.TOKEN_PREFIX + " " + token);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException failed) {

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }

  /**
   * @return the instance of {@link JwtCreator}.
   */
  public JwtCreator getJwtCreator() {

    return this.jwtCreator;
  }

  /**
   * @param jwtCreator new value of {@link #getJwtCreator()}.
   */
  @Inject
  public void setJwtCreator(JwtCreator jwtCreator) {

    this.jwtCreator = jwtCreator;
  }

  /**
   * @return the instance of {@link UserDetailsService}.
   */
  public UserDetailsService getUserDetailsService() {

    return this.userDetailsService;
  }

  /**
   * @param userDetailsService new value of {@link #getUserDetailsService()}.
   */
  @Inject
  public void setUserDetailsService(UserDetailsService userDetailsService) {

    this.userDetailsService = userDetailsService;
  }

}
