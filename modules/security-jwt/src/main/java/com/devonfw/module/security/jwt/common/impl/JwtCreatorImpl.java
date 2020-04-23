package com.devonfw.module.security.jwt.common.impl;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.core.Authentication;

import com.devonfw.module.security.common.api.authentication.AdvancedAuthentication;
import com.devonfw.module.security.common.api.authentication.DefaultAuthentication;
import com.devonfw.module.security.jwt.common.api.JwtCreator;
import com.devonfw.module.security.jwt.common.api.JwtManager;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;

/**
 * Implementation of {@link JwtCreator}.
 *
 * @since 2020.04.001
 */
@Named
public class JwtCreatorImpl implements JwtCreator {

  @Inject
  private JwtManager jwtManager;

  @Override
  public String create(Authentication authentication) {

    Claims claims = null;
    if (authentication instanceof DefaultAuthentication) {
      // see JwtAuthenticatorImpl
      Map<String, Object> attributes = ((DefaultAuthentication) authentication).getAttributes();
      if (attributes instanceof Claims) {
        Object credentials = authentication.getCredentials();
        if (credentials instanceof String) {
          return (String) credentials;
        }
        claims = (Claims) attributes;
      }
    }
    if (claims == null) {
      claims = new DefaultClaims();
      claims.setSubject(authentication.getName());
      Set<String> permissions = AdvancedAuthentication.getPermissions(authentication);
      String roles = String.join(",", permissions);
      claims.put(JwtManager.CLAIM_ROLES, roles);
    }
    return this.jwtManager.encodeAndSign(claims);
  }

}
