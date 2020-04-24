package com.devonfw.module.security.jwt.common.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.core.Authentication;

import com.devonfw.module.security.common.api.authentication.DefaultAuthentication;
import com.devonfw.module.security.jwt.common.api.JwtAuthenticator;
import com.devonfw.module.security.jwt.common.api.JwtManager;

import io.jsonwebtoken.Claims;

/**
 * Implementation of {@link JwtAuthenticator}.
 *
 * @since 2020.04.001
 */
@Named
public class JwtAuthenticatorImpl implements JwtAuthenticator {

  @Inject
  private JwtManager jwtManager;

  @Override
  public Authentication authenticate(String jwt) {

    Claims claims = this.jwtManager.decodeAndVerify(jwt);
    String principal = claims.getSubject();
    String roles = claims.get(JwtManager.CLAIM_ROLES, String.class);
    Set<String> permissions = new HashSet<>(Arrays.asList(roles.split(",")));
    return new DefaultAuthentication(principal, jwt, permissions, claims);
  }

}
