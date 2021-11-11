package com.devonfw.module.security.jwt.common.base;

/**
 * Constants for JSON Web Tokens (JWT) and OAuth.
 *
 * @since 2020.04.001
 */
public class JwtConstants {

  /**
   * The roles/permissions assigned to the user
   *
   * @deprecated configurable via {@link com.devonfw.module.security.jwt.common.impl.JwtConfigProperties#getClaims()} in
   *             {@link com.devonfw.module.security.jwt.common.impl.JwtConfigProperties.ClaimsConfigProperties#getAccessControlsName()}.
   */
  @Deprecated
  public static final String CLAIM_ROLES = "roles";

  /**
   * The "Bearer" as token prefix (see <a href="https://tools.ietf.org/html/rfc6750">Bearer Token usage</a> for details)
   */
  public static final String TOKEN_PREFIX = "Bearer";

  /**
   * Token in the Authorization request header field (see <a
   * "https://tools.ietf.org/id/draft-ietf-oauth-v2-bearer-13.xml#rfc.section.2.1">Authorization Request Header
   * Field</a> for details)
   */
  public static final String HEADER_AUTHORIZATION = "Authorization";

  /**
   * HTTP Access-Control-Expose-Headers header is a response header that is used to expose the headers
   */
  public static final String EXPOSE_HEADERS = "Access-Control-Expose-Headers";

}
