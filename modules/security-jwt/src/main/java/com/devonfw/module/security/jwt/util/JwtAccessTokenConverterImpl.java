package com.devonfw.module.security.jwt.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.devonfw.module.security.jwt.authentication.JwtAuthenticationFactory;
import com.devonfw.module.security.keystore.config.KeyStoreAccessFactory;
import com.devonfw.module.security.keystore.config.KeyStoreConfigProperties;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Converter class for managing JWT token
 *
 * @since 3.3.0
 *
 */
@Named
public class JwtAccessTokenConverterImpl {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(JwtAccessTokenConverterImpl.class);

  @Inject
  private TokenConfigProperties tokenConfigProperties;

  private JwtAuthenticationFactory jwtAuthenticationFactory;

  @Inject
  private TokenValidator tokenValidator;

  @Inject
  private KeyStoreAccessFactory keyStoreAccessFactory;

  @Inject
  private KeyStoreConfigProperties keyStoreConfigProperties;

  /**
   * @param jwtAuthenticationFactory {@link JwtAuthenticationFactory}.
   */
  public void setJwtAuthenticationFactory(JwtAuthenticationFactory jwtAuthenticationFactory) {

    this.jwtAuthenticationFactory = jwtAuthenticationFactory;
  }

  /**
   * This method allows the clients to be able to access other headers <Access-Control-Expose-Headers>
   *
   * @param res the {@HttpServletResponse}
   */
  public void addAllowedHeader(HttpServletResponse res) {

    res.addHeader(JwtTokenConstants.EXPOSE_HEADERS, JwtTokenConstants.HEADER_STRING);
  }

  /**
   * This method generates the token and sets it into headers
   *
   * @param res the {@HttpServletResponse}
   * @param auth the {@Authentication} object with the user credentials
   */
  public void addAuthentication(HttpServletResponse res, Authentication auth) {

    String token = generateToken(auth);
    res.addHeader(JwtTokenConstants.EXPOSE_HEADERS, JwtTokenConstants.HEADER_STRING);
    res.addHeader(JwtTokenConstants.HEADER_STRING, JwtTokenConstants.TOKEN_PREFIX + " " + token);
  }

  /**
   * This method validates the token and returns a {@link Authentication}
   *
   * @param request the {@link HttpServletRequest}
   * @return the {@link Authentication}
   */
  public Authentication getAuthentication(HttpServletRequest request) {

    Authentication authentication = null;

    String authToken = request.getHeader(JwtTokenConstants.HEADER_STRING);

    if (authToken != null) {
      this.tokenValidator.setJwtAuthenticationFactory(this.jwtAuthenticationFactory);
      authentication = this.tokenValidator
          .validateTokenAndSignature(authToken.replace(JwtTokenConstants.TOKEN_PREFIX, ""));
    }
    return authentication;
  }

  /**
   * This method generates the token using {@link Authentication} object.
   *
   * @param auth the {@link Authentication}
   * @return token
   */
  private String generateToken(Authentication auth) {

    List<String> roles = new ArrayList<>();
    Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
    for (GrantedAuthority authority : authorities) {
      roles.add(authority.getAuthority());
    }

    Map<String, Object> claims = new HashMap<>();
    claims.put(JwtTokenConstants.CLAIM_ISSUER, this.tokenConfigProperties.getIssuer());
    claims.put(JwtTokenConstants.CLAIM_SUBJECT, auth.getName());
    claims.put(JwtTokenConstants.CLAIM_ROLES, roles);
    claims.put(JwtTokenConstants.CLAIM_CREATED, TimeUnit.MILLISECONDS.toSeconds(generateCreationDate()));
    claims.put(JwtTokenConstants.CLAIM_EXPIRATION, TimeUnit.MILLISECONDS.toSeconds(generateExpirationDate()));

    return Jwts.builder().setClaims(claims)
        .signWith(SignatureAlgorithm.forName(this.tokenConfigProperties.getAlgorithm()),
            this.keyStoreAccessFactory.getKeys(this.keyStoreConfigProperties.getKeyStoreAlias()).getPrivateKey())
        .compact();
  }

  private Long generateCreationDate() {

    return System.currentTimeMillis();
  }

  private long generateExpirationDate() {

    long expirationTerm = TimeUnit.HOURS.toMillis(this.tokenConfigProperties.getExpirationHours().toMillis());
    return System.currentTimeMillis() + expirationTerm;
  }

}
