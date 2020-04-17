package com.devonfw.module.security.jwt.common.api;

import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Claims;

/**
 *
 *
 * @since 2020.04.001
 */
public interface JwtCreator {

  /**
   * @param authentication the {@link Authentication} to use.
   * @return the {@link JwtManager#encodeAndSign(Claims) encoded and signed JWT}.
   */
  String create(Authentication authentication);

}
