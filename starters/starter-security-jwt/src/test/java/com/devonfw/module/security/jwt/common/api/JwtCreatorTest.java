package com.devonfw.module.security.jwt.common.api;

import java.util.Date;
import java.util.Set;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import com.devonfw.module.security.common.api.authentication.DefaultAuthentication;

import io.jsonwebtoken.Claims;

/**
 * Test of {@link JwtCreator}.
 */
public class JwtCreatorTest extends JwtComponentTest {

  @Inject
  private JwtCreator jwtCreator;

  /**
   * Test of {@link JwtCreator#create(Authentication)}.
   */
  @Test
  public void testDo() {

    // given
    Authentication authentication = new DefaultAuthentication(TEST_LOGIN, "******",
        Set.of(TEST_ROLE_READ_MASTER_DATA, TEST_ROLE_SAVE_USER));
    Date now = new Date();

    // when
    String token = this.jwtCreator.create(authentication);
    Claims claims = getJwtManager().decodeAndVerify(token);

    // then
    assertThat(claims.getSubject()).isEqualTo(TEST_LOGIN);
    assertThat(claims.getIssuer()).isEqualTo(TEST_ISSUER);
    assertThat(claims.getExpiration()).isBetween(addToDate(now, this.EXPIRATION_MS - this.NOT_BEFORE_DELAY_MS),
        addToDate(now, this.EXPIRATION_MS), true, false);
    assertThat(claims.getNotBefore()).isBetween(addToDate(now, -this.NOT_BEFORE_DELAY_MS - 1000), now, true, false);

  }

}
