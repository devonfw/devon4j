package com.devonfw.module.security.jwt.common.api;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * The manager for JSON Web Token (JWT). A regular app typically only needs to {@link #decodeAndVerify(String) decode
 * and verify} JWTs. In case your app issues JWTs (e.g. in case you build a gateway or reverse-proxy)
 *
 * @since 2020.04.001
 */
public interface JwtManager {

  /**
   * Custom {@link Claims#get(String, Class) claim} for the
   * {@link com.devonfw.module.security.common.api.accesscontrol.AccessControl roles/permissions} assigned to the user.
   *
   * @deprecated configurable via {@link com.devonfw.module.security.jwt.common.impl.JwtConfigProperties#getClaims()} in
   *             {@link com.devonfw.module.security.jwt.common.impl.JwtConfigProperties.ClaimsConfigProperties#getAccessControlsName()}.
   */
  @Deprecated
  String CLAIM_ROLES = "roles";

  /** Custom {@link Claims#get(String, Class) claim} for the email address of the user. */
  String CLAIM_MAIL = "mail";

  /**
   * @param jwt the JSON Web Token (JWT) encoded as {@link String}.
   * @return the decoded JWT.
   */
  Jws<Claims> decode(String jwt);

  /**
   * @param jwt the {@link #decode(String) decoded} JSON Web Token (JWT) to verify.
   * @throws RuntimeException if the verification fails.
   */
  void verify(Jws<Claims> jwt);

  /**
   * @param jwt the JSON Web Token (JWT) encoded as {@link String}.
   * @return the {@link Claims} of the {@link #decode(String) decoded} and {@link #verify(Jws) verified} JWT.
   */
  default Claims decodeAndVerify(String jwt) {

    Jws<Claims> token = decode(jwt);
    verify(token);
    return token.getBody();
  }

  /**
   * @param claims the {@link Claims} for the JSON Web Token (JWT) to create.
   * @return the encoded and signed JWT.
   */
  String encodeAndSign(Claims claims);

}
