package com.devonfw.module.security.jwt.authentication;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.devonfw.module.security.jwt.util.JwtTokenConstants;

import io.jsonwebtoken.Claims;

/**
 * Default Implementation of {@link JwtAuthenticationFactory}.
 *
 * Override this class with own implementation if required.
 *
 */
public class JwtAuthenticationFactoryImpl implements JwtAuthenticationFactory {

  @Override
  public Authentication create(Claims jwtToken) {

    String user = jwtToken.getSubject();
    Instant now = Instant.now();
    Object expObj = jwtToken.get(JwtTokenConstants.CLAIM_EXPIRATION);

    if (expObj != null) {
      Instant expiration_time = getTimeAsInstant(expObj);
      if (now.isAfter(expiration_time)) {
        throw new IllegalStateException("Token is expired" + expiration_time);
      }
    }

    List<String> roles = (List<String>) jwtToken.get(JwtTokenConstants.CLAIM_ROLES);
    List<GrantedAuthority> authorities = new ArrayList<>();
    if (roles != null) {
      for (String role : roles) {
        authorities.add(new SimpleGrantedAuthority(role));
      }
    }
    Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
    return authentication;
  }

  private Instant getTimeAsInstant(Object obj) {

    Instant time = Instant.ofEpochSecond(((Number) obj).longValue());
    return time;
  }

}
