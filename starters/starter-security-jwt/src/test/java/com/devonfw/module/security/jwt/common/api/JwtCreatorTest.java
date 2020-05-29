package com.devonfw.module.security.jwt.common.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.devonfw.module.security.common.api.authentication.DefaultAuthentication;
import com.devonfw.test.app.TestJwtAccessControlConfig;

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
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(TestJwtAccessControlConfig.GROUP_READ_MASTER_DATA));
    authorities.add(new SimpleGrantedAuthority(TestJwtAccessControlConfig.GROUP_SAVE_USER));
    Authentication authentication = new DefaultAuthentication(TEST_LOGIN, "******", authorities);
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
