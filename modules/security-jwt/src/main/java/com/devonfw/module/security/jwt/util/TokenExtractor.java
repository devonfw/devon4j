package com.devonfw.module.security.jwt.util;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;

import com.devonfw.module.security.jwt.authentication.AuthenticationTokenDetails;
import com.devonfw.module.security.jwt.authentication.AuthenticationTokenDetailsFactory;
import com.devonfw.module.security.jwt.sign.JwtSignatureAlgorithm;
import com.devonfw.module.security.jwt.sign.JwtSignatureAlgorithmFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class to take request object and extract token and validate it.
 *
 * @since 3.3.0
 *
 */
@Named
public class TokenExtractor {

  private static final Logger LOG = LoggerFactory.getLogger(TokenExtractor.class);

  private ObjectMapper objectMapper = new ObjectMapper();

  private Map<String, Object> tokenClaims;

  @Inject
  private JwtSignatureAlgorithmFactory jwtSignatureAlgorithmFactory;

  @Inject
  private AuthenticationTokenDetailsFactory authenticationTokenFactory;

  @Inject
  private TokenConfigProperties tokenConfigProperties;

  /**
   * Validates the token
   *
   * @param token
   *
   * @return {@link Authentication}
   */
  public Authentication validateTokenAndSignature(String token) {

    Authentication authentication = null;

    JwtSignatureAlgorithm jwtSignatureAlgorithm = this.jwtSignatureAlgorithmFactory
        .getAlgorithms(this.tokenConfigProperties.getAlgorithm());
    AuthenticationTokenDetails authenticationToken = this.authenticationTokenFactory
        .getAuthenticationTokenDetails(this.tokenConfigProperties.getTokenDetailsName());

    SignatureVerifier verifier = jwtSignatureAlgorithm.createVerifier();
    Jwt jwt = JwtHelper.decodeAndVerify(token, verifier);

    String claims = jwt.getClaims();
    try {
      this.tokenClaims = this.objectMapper.readValue(claims, Map.class);

      LOG.info("Token Claims " + this.tokenClaims.toString());

      Instant now = Instant.now();

      Object expObj = this.tokenClaims.get(JwtTokenConstants.CLAIM_EXPIRATION);

      if (expObj != null) {
        Instant expiration_time = getTimeAsInstant(expObj);
        if (now.isAfter(expiration_time)) {
          throw new IllegalStateException("Token is expired" + expiration_time);
        }
      }

      String user = this.tokenClaims.get(JwtTokenConstants.CLAIM_SUBJECT).toString();

      List<String> roles = (List<String>) this.tokenClaims.get(JwtTokenConstants.CLAIM_ROLES);

      List<GrantedAuthority> authorities = new ArrayList<>();
      if (roles != null) {
        for (String role : roles) {
          authorities.add(new SimpleGrantedAuthority(role));
        }
      }
      authenticationToken.setAuthorities(authorities);
      authenticationToken.setPrincipal(user);
      authentication = authenticationToken.composeAuthenticationTokenDetails();

    } catch (IOException e) {
      throw new IllegalStateException("Token claims cannot be read:" + e);
    }
    return authentication;
  }

  private Instant getTimeAsInstant(Object obj) {

    Instant time = Instant.ofEpochSecond(((Number) obj).longValue());
    return time;
  }

}
