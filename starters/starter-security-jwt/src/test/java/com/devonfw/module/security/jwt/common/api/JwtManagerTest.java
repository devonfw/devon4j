package com.devonfw.module.security.jwt.common.api;

import java.util.Date;

import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;

/**
 * Test of {@link JwtManager}.
 */
public class JwtManagerTest extends JwtComponentTest {

  private static final String CLAIM_GROUPS = "groups";

  /**
   * Test of encoding claims to JWT and decoding to assert claims afterwards.
   */
  @Test
  public void testEncodeAndDecode() {

    // given
    String login = TEST_LOGIN;
    DefaultClaims claims = new DefaultClaims();
    claims.setSubject(login);
    claims.put(CLAIM_GROUPS, TEST_ACCESS_CONTROLS);
    Date now = new Date();
    JwtManager jwtManager = getJwtManager();

    // when
    String token = jwtManager.encodeAndSign(claims);
    Claims restoredClaims = jwtManager.decodeAndVerify(token);

    // then
    assertThat(restoredClaims.getSubject()).isEqualTo(login);
    assertThat(restoredClaims.getIssuer()).isEqualTo(TEST_ISSUER);
    assertThat(restoredClaims.getExpiration()).isBetween(addToDate(now, this.EXPIRATION_MS - this.NOT_BEFORE_DELAY_MS),
        addToDate(now, this.EXPIRATION_MS), true, false);
    assertThat(restoredClaims.getNotBefore()).isBetween(addToDate(now, -this.NOT_BEFORE_DELAY_MS - 1000), now, true,
        false);
  }

  /**
   * Test of {@link JwtManager#decodeAndVerify(String)}.
   */
  @Test
  public void testDecodeAndVerify() {

    adjustClock();

    // given
    String token = TEST_JWT;
    JwtManager jwtManager = getJwtManager();

    // when
    Claims claims = jwtManager.decodeAndVerify(token);

    // then
    assertThat(claims.getIssuer()).isEqualTo(TEST_ISSUER);
    assertThat(claims.getSubject()).isEqualTo(TEST_LOGIN);
    assertThat(claims.get(CLAIM_GROUPS)).isEqualTo(TEST_ACCESS_CONTROLS);
    assertThat(claims.getNotBefore()).isEqualTo(new Date(1587716056000L));
    assertThat(claims.getExpiration()).isEqualTo(new Date(1587730516000L));
  }

}
