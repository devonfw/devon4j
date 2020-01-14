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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;

import com.devonfw.module.security.jwt.config.KeyStoreAccess;
import com.devonfw.module.security.jwt.sign.JwtSignatureAlgorithmFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class to take request object and extract token and validate it.
 *
 * @since 3.2.0
 *
 */
@Named
public class TokenExtractor {

  private static final Logger LOG = LoggerFactory.getLogger(TokenExtractor.class);

  private ObjectMapper objectMapper = new ObjectMapper();

  @Inject
  private KeyStoreAccess keyStoreAccess;

  private Map<String, Object> tokenClaims;

  @Inject
  private JwtSignatureAlgorithmFactory jwtSignatureAlgorithmFactory;

  /**
   * Validates the token
   *
   * @param token
   *
   * @return {@link Authentication}
   */
  public Authentication validateTokenAndSignature(String token) {

    Authentication authentication = null;

    com.devonfw.module.security.jwt.sign.JwtSignatureAlgorithm jwtSignatureAlgorithm = this.jwtSignatureAlgorithmFactory
        .getAlgorithms("RSA");

    SignatureVerifier verifier = jwtSignatureAlgorithm.createVerifier();
    Jwt jwt = JwtHelper.decodeAndVerify(token, verifier);

    String claims = jwt.getClaims();
    try {
      this.tokenClaims = this.objectMapper.readValue(claims, Map.class);

      LOG.info("Token Claims " + this.tokenClaims.toString());

      Instant now = Instant.now();

      Object nbfObj = this.tokenClaims.get("nbf");
      Object expObj = this.tokenClaims.get("exp");

      if (nbfObj != null) {
        Instant nbf = Instant.ofEpochSecond(((Number) nbfObj).longValue());
        if (now.isBefore(nbf)) {
          throw new IllegalStateException("Token is not valid before " + nbf);
        }

      }

      if (expObj != null) {
        Instant expiration_time = Instant.ofEpochSecond(((Number) expObj).longValue());

        if (now.isAfter(expiration_time)) {
          throw new IllegalStateException("Token is expired" + expiration_time);
        }
      }

      String user = this.tokenClaims.get("sub").toString();

      List<String> roles = (List<String>) this.tokenClaims.get("roles");

      List<GrantedAuthority> authorities = new ArrayList<>();
      for (String role : roles) {
        authorities.add(new SimpleGrantedAuthority(role));
      }

      authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
    } catch (IOException e) {

      throw new IllegalStateException("Token claims cannot be read:" + e);
    }
    return authentication;
  }

}
