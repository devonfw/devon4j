package com.devonfw.module.security.jwt.common.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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

  @Inject
  private JwtConfigProperties jwtConfig;

  @SuppressWarnings("unchecked")
  @Override
  public Authentication authenticate(String jwt) {

    Claims claims = this.jwtManager.decodeAndVerify(jwt);
    String principal = claims.getSubject();
    String accessControlsName = this.jwtConfig.getClaims().getAccessControlsName();
    Collection<String> accessControlIds;
    Object accessControls = claims.get(accessControlsName);
    if (accessControls instanceof String) {
      accessControlIds = Arrays.asList(accessControls.toString().split(","));
    } else if (accessControls instanceof String[]) {
      accessControlIds = Arrays.asList((String[]) accessControls);
    } else if (accessControls instanceof Collection) {
      accessControlIds = (Collection<String>) accessControls;
    } else if (accessControls == null) {
      accessControlIds = Collections.emptyList();
    } else {
      throw new IllegalStateException("Invalid or malformed JWT claim " + accessControlsName + ": " + accessControls);
    }
    Set<AccessControl> permissions = this.accessControlProvider.expandPermissions(accessControlIds);
    return DefaultAuthentication.ofAccessControls(principal, jwt, permissions, claims);
  }

}
