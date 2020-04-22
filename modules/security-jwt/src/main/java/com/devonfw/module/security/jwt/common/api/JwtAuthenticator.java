package com.devonfw.module.security.jwt.common.api;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Interface for authentication via JSON Web Token (JWT).
 *
 * @since 2020.04.001
 */
public interface JwtAuthenticator {

  /**
   * @param jwt the JSON Web Token (JWT) as {@link String}.
   * @return the successful authentication.
   * @throws AuthenticationException if the authentication failed.
   * @see org.springframework.security.authentication.AuthenticationManager#authenticate(Authentication)
   */
  Authentication authenticate(String jwt);

}
