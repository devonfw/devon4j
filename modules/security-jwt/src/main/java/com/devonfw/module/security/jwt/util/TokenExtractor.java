package com.devonfw.module.security.jwt.util;

import java.io.IOException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;

import com.devonfw.module.security.jwt.config.KeyStoreAccess;
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

  /**
   * Validates the token
   *
   * @param token
   *
   * @return {@link Authentication}
   */
  public Authentication validateTokenAndSignature(String token) {

    Authentication authentication = null;

    SignatureVerifier verifier = signatureVerifierFactory((this.keyStoreAccess.getPublicKey()),
        getAlgorithmFamilyType(JwtHelper.headers(token).get("alg")));

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

  /**
   * Returns algorithms from Enum {@link JwtSignatureAlgorithm}
   *
   * @param algorithms
   * @return
   */
  private String getAlgorithmFamilyType(String algorithms) {

    StringBuilder familyAlgorithm = null;
    List<JwtSignatureAlgorithm> algoLists = Arrays.asList(JwtSignatureAlgorithm.values());
    for (JwtSignatureAlgorithm algorithm : algoLists) {
      if (algorithm.getValue().equalsIgnoreCase(algorithms)) {
        familyAlgorithm = new StringBuilder(algorithm.getFamilyName());
        return familyAlgorithm.toString();
      }
    }
    return null;
  }

  /**
   * This factory method return {@link RsaVerifier} based on RSA algorithm. Current default is RSA
   *
   * @param publicKey , algorithm
   *
   * @return {@link SignatureVerifier}
   */
  private SignatureVerifier signatureVerifierFactory(PublicKey publicKey, String algorithm) {

    SignatureVerifierAlgorithm signatureVerifierAlgorithm = SignatureVerifierAlgorithm.valueOf(algorithm);
    switch (signatureVerifierAlgorithm) {
      case ELLIPTICCURVE:
        return null;
      case HMAC:
        return null;
      case RSA:
        return new RsaVerifier((RSAPublicKey) publicKey);

      default:
        return new RsaVerifier((RSAPublicKey) publicKey);
    }
  }

}
