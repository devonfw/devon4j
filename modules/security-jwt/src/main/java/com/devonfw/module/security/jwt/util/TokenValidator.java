package com.devonfw.module.security.jwt.util;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import com.devonfw.module.security.jwt.authentication.JwtAuthenticationFactory;
import com.devonfw.module.security.keystore.config.KeyStoreAccessFactory;
import com.devonfw.module.security.keystore.config.KeyStoreConfigProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * Class to take request object and validate it.
 *
 * @since 3.3.0
 *
 */
@Named
public class TokenValidator {

  private static final Logger LOG = LoggerFactory.getLogger(TokenValidator.class);

  @Inject
  private KeyStoreAccessFactory keyStoreAccessFactory;

  @Inject
  private KeyStoreConfigProperties keyStoreConfigProperties;

  private JwtAuthenticationFactory jwtAuthenticationFactory;

  /**
   * Validates the token
   *
   * @param token
   *
   * @return {@link Authentication}
   */
  public Authentication validateTokenAndSignature(String token) {

    Claims claims = Jwts.parser()
        .setSigningKey(
            this.keyStoreAccessFactory.getKeys(this.keyStoreConfigProperties.getKeyStoreAlias()).getPublicKey())
        .parseClaimsJws(token).getBody();

    return this.jwtAuthenticationFactory.create(claims);

  }

  /**
   * @param jwtAuthenticationFactory {@link JwtAuthenticationFactory}..
   */
  public void setJwtAuthenticationFactory(JwtAuthenticationFactory jwtAuthenticationFactory) {

    this.jwtAuthenticationFactory = jwtAuthenticationFactory;
  }

}
