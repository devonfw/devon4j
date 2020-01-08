package com.devonfw.module.security.jwt.util;

import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * Utility class for JWT token managing
 *
 * @since 3.2.0
 *
 */
@Named
public class TokenAuthenticationUtil extends JwtAccessTokenConverter implements InitializingBean {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(TokenAuthenticationUtil.class);

  @Inject
  private TokenConfigProperties tokenConfigProperties;

  @Inject
  private TokenExtractor tokenExtractor;

  private JsonParser jsonParser = JsonParserFactory.create();

  private CharSequence content;

  private Signer signer;

  private Map<String, String> headers;

  /**
   * This method allows the clients to be able to access other headers <Access-Control-Expose-Headers>
   *
   * @param res the {@HttpServletResponse}
   */
  public void addAllowedHeader(HttpServletResponse res) {

    res.addHeader(this.tokenConfigProperties.getExposeHeaders(),
        this.tokenConfigProperties.getHeaderString() + ", " + this.tokenConfigProperties.getHeaderOtp());
  }

  /**
   * This method generates the token and sets it into headers
   *
   * @param res the {@HttpServletResponse}
   * @param auth the {@Authentication} object with the user credentials
   */
  public void addAuthentication(HttpServletResponse res, Authentication auth, PrivateKey privateKey) {

    String token = generateToken(auth, privateKey);
    res.addHeader(this.tokenConfigProperties.getExposeHeaders(), this.tokenConfigProperties.getHeaderString());
    res.addHeader(this.tokenConfigProperties.getHeaderString(),
        this.tokenConfigProperties.getTokenPrefix() + " " + token);

  }

  /**
   * This method sets a header field in order to notify the user for further authentication requirements
   *
   * @param res the {@HttpServletResponse}
   * @param auth the {@Authentication} object with the user credentials
   */
  public void addRequiredAuthentication(HttpServletResponse res, Authentication auth) {

    // Add possible required authentication factors into the header
    res.addHeader(this.tokenConfigProperties.getHeaderOtp(), auth.getDetails().toString());
  }

  /**
   * This method validates the token and returns a {@link Authentication}
   *
   * @param request the {@link HttpServletRequest}
   * @return the {@link Authentication}
   */
  public Authentication getAuthentication(HttpServletRequest request) {

    Authentication authentication = null;
    try {
      request.setCharacterEncoding("ISO-8859-1");
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException("Problem with Encoding " + e);
    }
    String authToken = request.getHeader(this.tokenConfigProperties.getHeaderString());

    if (authToken != null) {
      authentication = this.tokenExtractor
          .validateTokenAndSignature(authToken.replace(this.tokenConfigProperties.getTokenPrefix(), ""));
    }
    return authentication;
  }

  /**
   * This method generates the token using {@link Authentication} object.
   *
   * @param auth the {@link Authentication}
   * @param privateKey the {@link PrivateKey}
   * @return token
   */
  private String generateToken(Authentication auth, PrivateKey privateKey) {

    List<String> scopes = new ArrayList<>();
    Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
    for (GrantedAuthority authority : authorities) {
      scopes.add(authority.getAuthority());
    }

    Map<String, Object> claims = new HashMap<>();
    claims.put(this.tokenConfigProperties.getClaimIssuer(), this.tokenConfigProperties.getIssuer());
    claims.put(this.tokenConfigProperties.getClaimSubject(), auth.getName());
    claims.put(this.tokenConfigProperties.getClaimScope(), scopes);
    claims.put(this.tokenConfigProperties.getClaimRoles(), scopes);
    claims.put(this.tokenConfigProperties.getClaimCreated(), generateCreationDate() / 1000);
    claims.put(this.tokenConfigProperties.getClaimExpiration(), generateExpirationDate() / 1000);
    String payload = this.jsonParser.formatMap(claims);
    this.signer = new RsaSigner((RSAPrivateKey) privateKey);
    payload = JwtHelper.encode(payload, this.signer).getEncoded();
    return payload;
  }

  private Long generateCreationDate() {

    return new Date().getTime();
  }

  private Long generateExpirationDate() {

    int expirationTerm = (60 * 60 * 1000) * this.tokenConfigProperties.getExpiratioHours();
    return new Date(new Date().getTime() + expirationTerm).getTime();
  }

}
