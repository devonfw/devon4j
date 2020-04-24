package com.devonfw.module.security.jwt.common.impl;

import java.security.PublicKey;
import java.time.Clock;
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

  private Clock clock;

  /**
   * The constructor.
   */
  public JwtManagerImpl() {

    super();
    this.clock = Clock.systemUTC();
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
    Instant now = this.clock.instant();
    // verify notBefore
    Instant notBefore = getInstant(claims.getNotBefore());
    if (notBefore != null) {
      if (now.isBefore(notBefore)) {
        throw new BadCredentialsException("Invalid JWT with notBefore (" + notBefore
            + ") in the future (check if your clock is in sync and ntpd is running)!");
      }
    } else if (validationConfig.isNotBeforeRequired()) {
      throw new BadCredentialsException("Invalid JWT without notBefore (nbf) claim!");
    }
    // verify expiration
    Instant expiration = getInstant(claims.getExpiration());
    if (expiration != null) {
      if (now.isAfter(expiration)) {
        throw new BadCredentialsException("Invalid JWT with expiration (" + expiration
            + ") in the past (check if your clock is in sync and ntpd is running)!");
      }
      // verify max validity
      Instant start = getInstant(claims.getIssuedAt());
      if (start == null) {
        start = notBefore;
      }
      if (start != null) {
        Duration validity = Duration.between(start, expiration);
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

  private Instant getInstant(Date date) {

    if (date == null) {
      return null;
    }
    return date.toInstant();
  }

  @Override
  public String encodeAndSign(Claims claims) {

    CreationConfigProperties creationConfig = this.jwtConfig.getCreation();
    claims.setIssuer(creationConfig.getIssuer());
    Instant now = this.clock.instant();
    if (creationConfig.isAddIssuedAt()) {
      claims.setIssuedAt(Date.from(now));
    }
    Instant notBefore = now.minus(creationConfig.getNotBeforeDelay());
    claims.setNotBefore(Date.from(notBefore));
    Instant expiration = now.plus(creationConfig.getValidity());
    claims.setExpiration(Date.from(expiration));
    return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.forName(this.jwtConfig.getAlgorithm()),
        this.keyStoreAccess.getPrivateKey(this.jwtConfig.getAlias())).compact();
  }

  /**
   * @return the {@link Clock} to use.
   */
  public Clock getClock() {

    return this.clock;
  }

  /**
   * @param clock the {@link Clock} to use. Allows to override for testing.
   */
  public void setClock(Clock clock) {

    this.clock = clock;
  }

}
