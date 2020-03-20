package com.devonfw.module.security.jwt.authentication;

import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Claims;

/**
 * Interface used for Constructing {@link Authentication} object using {@link principal} and {@link List of
 * GrantedAuthority>} and extracting token
 *
 */
public interface JwtAuthenticationFactory {

  /**
   * Constructs {@link Authentication} object and extract token
   *
   * @param jwtToken token to be extracted
   * @return {@link Authentication}
   */
  public Authentication create(Claims jwtToken);

}
