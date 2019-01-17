package com.devonfw.module.security.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.devonfw.module.security.common.api.datatype.Role;
import com.devonfw.module.security.common.api.to.UserDetailsClientTo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Service class for JWT token managing
 *
 */
public class TokenAuthenticationService {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(TokenAuthenticationService.class);

  static final String ISSUER = "MyThaiStarApp";

  static final Integer EXPIRATION_HOURS = 1;

  static final String SECRET = "ThisIsASecret";

  static final String TOKEN_PREFIX = "Bearer";

  static final String HEADER_STRING = "Authorization";

  static final String EXPOSE_HEADERS = "Access-Control-Expose-Headers";

  static final String CLAIM_SUBJECT = "sub";

  static final String CLAIM_ISSUER = "iss";

  static final String CLAIM_EXPIRATION = "exp";

  static final String CLAIM_CREATED = "iat";

  static final String CLAIM_SCOPE = "scope";

  static final String CLAIM_ROLES = "roles";

  /**
   * This method returns the token once the Authentication has been successful
   *
   * @param res the {@HttpServletResponse}
   * @param auth the {@Authentication} object with the user credentials
   */
  static void addAuthentication(HttpServletResponse res, Authentication auth) {

    String token = generateToken(auth);
    res.addHeader(EXPOSE_HEADERS, HEADER_STRING);
    res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + token);
  }

  /**
   * This method validates the token and returns a {@link UsernamePasswordAuthenticationToken}
   *
   * @param request the {@link HttpServletRequest}
   * @return the {@link UsernamePasswordAuthenticationToken}
   */
  static Authentication getAuthentication(HttpServletRequest request) {

    String token = request.getHeader(HEADER_STRING);
    if (token != null) {

      // The JWT parser will throw an exception if the token is not well formed or the token has expired
      String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
          .getSubject();
      return user != null ? new UsernamePasswordAuthenticationToken(user, null, getAuthorities(token)) : null;

    }

    return null;
  }

  static Collection<? extends GrantedAuthority> getAuthorities(String token) {

    List<String> roles = getRolesFromToken(token);
    List<GrantedAuthority> authorities = new ArrayList<>();
    for (String role : roles) {
      authorities.add(new SimpleGrantedAuthority(role));
    }
    return authorities;

  }

  static String generateToken(Authentication auth) {

    List<String> scopes = new ArrayList<>();
    Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
    for (GrantedAuthority authority : authorities) {
      scopes.add(authority.getAuthority());
    }

    Map<String, Object> claims = new HashMap<>();
    claims.put(CLAIM_ISSUER, ISSUER);
    claims.put(CLAIM_SUBJECT, auth.getName());
    claims.put(CLAIM_SCOPE, scopes);
    claims.put(CLAIM_ROLES, scopes);
    claims.put(CLAIM_CREATED, generateCreationDate() / 1000);
    claims.put(CLAIM_EXPIRATION, generateExpirationDate() / 1000);
    LOG.info(claims.toString());
    return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, SECRET).compact();
  }

  static Long generateCreationDate() {

    return new Date().getTime();
  }

  static Long generateExpirationDate() {

    int expirationTerm = (60 * 60 * 1000) * EXPIRATION_HOURS;
    return new Date(new Date().getTime() + expirationTerm).getTime();
  }

  /**
   * Extracts and returns the {@link UserDetailsClientTo} from the JWT token
   *
   * @param token the JWT token
   * @return the {@link UserDetailsClientTo} object
   */
  public static UserDetailsClientTo getUserdetailsFromToken(String token) {

    UserDetailsClientTo userDetails = new UserDetailsClientTo();
    try {
      String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
          .getSubject();

      List<String> roles = getRolesFromToken(token);
      if (user != null) {
        userDetails.setName(user);
      }
      if (!roles.isEmpty()) {

        for (Role c : Role.values()) {
          if (c.name().equals(roles.get(0))) {
            userDetails.setRole(c);
          }
        }

      }
    } catch (Exception e) {
      LOG.error(e.getMessage());
      userDetails = null;
    }

    return userDetails;
  }

  static List<String> getRolesFromToken(String token) {

    return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
        .get(CLAIM_SCOPE, List.class);
  }

}
