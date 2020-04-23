package com.devonfw.module.security.jwt.common.impl;

import java.security.PublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.authentication.BadCredentialsException;

import com.devonfw.module.security.jwt.common.api.JwtManager;
import com.devonfw.module.security.jwt.common.base.JwtConstants;
import com.devonfw.module.security.jwt.common.impl.JwtConfigProperties.CreationConfigProperties;
import com.devonfw.module.security.jwt.common.impl.JwtConfigProperties.ValidationConfigProperties;
import com.devonfw.module.security.keystore.common.api.KeyStoreAccess;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Implementation of {@link JwtManager}.
 *
 * @since 2020.04.001
 */
@Named
public class JwtManagerImpl implements JwtManager {

  @Inject
  private KeyStoreAccess keyStoreAccess;

  @Inject
  private JwtConfigProperties jwtConfig;

  /**
   * The constructor.
   */
  public JwtManagerImpl() {

    super();
  }

  @Override
  public Jws<Claims> decode(String jwt) {

    PublicKey key = this.keyStoreAccess.getPublicKey(this.jwtConfig.getAlias());
    Jws<Claims> token = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt.replace(JwtConstants.TOKEN_PREFIX, ""));
    return token;
  }

  @Override
  public void verify(Jws<Claims> jwt) {

    // verify algorithm
    if (!Objects.equals(jwt.getHeader().getAlgorithm(), this.jwtConfig.getAlgorithm())) {
      throw new BadCredentialsException("Invalid JWT with algorithm (" + jwt.getHeader().getAlgorithm()
          + ") - expected (" + this.jwtConfig.getAlgorithm() + ")!");
    }
    ValidationConfigProperties validationConfig = this.jwtConfig.getValidation();
    Claims claims = jwt.getBody();
    Date now = new Date();
    // verify notBefore
    Date notBefore = claims.getNotBefore();
    if (notBefore != null) {
      if (now.before(notBefore)) {
        throw new BadCredentialsException("Invalid JWT with notBefore (" + notBefore
            + ") in the future (check if your clock is in sync and ntpd is running)!");
      }
    } else if (validationConfig.isNotBeforeRequired()) {
      throw new BadCredentialsException("Invalid JWT without notBefore (nbf) claim!");
    }
    // verify expiration
    Date expiration = claims.getExpiration();
    if (expiration != null) {
      if (now.after(expiration)) {
        throw new BadCredentialsException("Invalid JWT with expiration (" + expiration
            + ") in the past (check if your clock is in sync and ntpd is running)!");
      }
      // verify max validity
      Date start = claims.getIssuedAt();
      if (start == null) {
        start = notBefore;
      }
      if (start != null) {
        Duration validity = Duration.between(start.toInstant(), expiration.toInstant());
        Duration maxValidity = validationConfig.getMaxValidity();
        Duration validityDelta = maxValidity.minus(validity);
        if (validityDelta.isNegative()) {
          throw new BadCredentialsException(
              "Invalid JWT with validity (" + validity + ") longer than allowed maximum (" + maxValidity + ")!");
        }

      }
    } else if (validationConfig.isNotBeforeRequired()) {
      throw new BadCredentialsException("Invalid JWT without expiration (exp) claim!");
    }

  }

  @Override
  public String encodeAndSign(Claims claims) {

    CreationConfigProperties creationConfig = this.jwtConfig.getCreation();
    claims.setIssuer(creationConfig.getIssuer());
    Date now = new Date();
    if (creationConfig.isAddIssuedAt()) {
      claims.setIssuedAt(now);
    }
    Instant nowInstant = now.toInstant();
    Instant notBefore = nowInstant.minus(creationConfig.getNotBeforeDeplay());
    claims.setNotBefore(Date.from(notBefore));
    Instant expiration = nowInstant.plus(creationConfig.getValidity());
    claims.setExpiration(Date.from(expiration));
    return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.forName(this.jwtConfig.getAlgorithm()),
        this.keyStoreAccess.getPrivateKey(this.jwtConfig.getAlias())).compact();
  }

}
