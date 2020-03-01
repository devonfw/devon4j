package com.devonfw.module.security.jwt.util;

/**
 * Constants class for JWT claims (see <a href="https://tools.ietf.org/html/rfc7519#section-4">JWT Claims</a> for
 * details)
 * 
 * @since 3.3.0
 */
public class JwtTokenConstants {

  /**
   * The user of the application that created this JWT (see <a
   * "https://tools.ietf.org/html/rfc7519#section-4.1.2">Subject Claim</a> for details)
   */
  public static final String CLAIM_SUBJECT = "sub";

  /**
   * The name of the application that created this JWT (see
   * <a href="https://tools.ietf.org/html/rfc7519#section-4.1.1">issuer claim</a> for details)
   */
  public static final String CLAIM_ISSUER = "iss";

  /**
   * The expiration hour used combination with expiration time "exp" , after which the JWT must not be accepted for
   * processing (see <a href="https://tools.ietf.org/html/rfc7519#section-4.1.4">Expiration Time Claim</a> for details)
   */
  public static final String CLAIM_EXPIRATION = "exp";

  /**
   * The time at which JWT was issued (see <a "https://tools.ietf.org/html/rfc7519#section-4.1.6">Issued at Claim</a>
   * for details)
   */
  public static final String CLAIM_CREATED = "iat";

  /**
   * The roles/permissions assigned to the user
   */
  public static final String CLAIM_ROLES = "roles";

}
