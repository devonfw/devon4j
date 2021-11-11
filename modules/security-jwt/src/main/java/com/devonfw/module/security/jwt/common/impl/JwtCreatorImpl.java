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

  @Inject
  private JwtConfigProperties jwtConfig;

  @Override
  public String create(Authentication authentication) {

    Claims claims = null;
    if (authentication instanceof DefaultAuthentication) {
      // see JwtAuthenticatorImpl
      Map<String, Object> attributes = ((DefaultAuthentication) authentication).getAttributes();
      if (attributes instanceof Claims) {
        Object credentials = authentication.getCredentials();
        if (credentials instanceof String) {
          // in this case we already have the existing encoded JWT
          return (String) credentials;
        }
        claims = (Claims) attributes;
      }
    }
    if (claims == null) {
      claims = new DefaultClaims();
      claims.setSubject(authentication.getName());
      Set<String> permissions = AdvancedAuthentication.getPermissions(authentication);
      String accessControlsName = this.jwtConfig.getClaims().getAccessControlsName();
      Object accessControls;
      if (this.jwtConfig.getClaims().isAccessControlsArray()) {
        accessControls = permissions;
      } else {
        accessControls = String.join(",", permissions);
      }
      claims.put(accessControlsName, accessControls);
    }
    return this.jwtManager.encodeAndSign(claims);
  }

}
