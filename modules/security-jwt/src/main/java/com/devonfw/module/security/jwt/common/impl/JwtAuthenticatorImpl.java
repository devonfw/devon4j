package com.devonfw.module.security.jwt.common.impl;

import java.util.Arrays;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.core.Authentication;

import com.devonfw.module.security.common.api.accesscontrol.AccessControl;
import com.devonfw.module.security.common.api.accesscontrol.AccessControlProvider;
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

  @Inject
  private AccessControlProvider accessControlProvider;

  @Override
  public Authentication authenticate(String jwt) {

    Claims claims = this.jwtManager.decodeAndVerify(jwt);
    String principal = claims.getSubject();
    String[] roleIds = claims.get(JwtManager.CLAIM_ROLES, String.class).split(",");
    Set<AccessControl> permissions = this.accessControlProvider.expandPermissions(Arrays.asList(roleIds));
    return DefaultAuthentication.ofAccessControls(principal, jwt, permissions, claims);
  }

}
