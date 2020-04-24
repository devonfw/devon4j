package com.devonfw.module.security.jwt.common.impl;

import java.time.Duration;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.devonfw.module.test.common.base.ComponentTest;
import com.devonfw.test.app.TestApplication;

/**
 * {@link ComponentTest} for {@link JwtConfigProperties}.
 */
@SpringBootTest(classes = { TestApplication.class }, webEnvironment = WebEnvironment.NONE)
public class JwtConfigPropertiesTest extends ComponentTest {

  @Inject
  private JwtConfigProperties jwtConfig;

  /** Test of {@link JwtConfigProperties} from application.properties. */
  @Test
  public void testConfig() {

    JwtConfigProperties config = this.jwtConfig;
    assertThat(config.getAlgorithm()).isEqualTo("ECDSA");
    assertThat(config.getAlias()).isEqualTo("alias");
    assertThat(config.getValidation().getMaxValidity()).isEqualTo(Duration.ofHours(42));
    assertThat(config.getValidation().isExpirationRequired()).isFalse();
    assertThat(config.getValidation().isNotBeforeRequired()).isFalse();
    assertThat(config.getCreation().isAddIssuedAt()).isTrue();
    assertThat(config.getCreation().getValidity()).isEqualTo(Duration.ofHours(4));
    assertThat(config.getCreation().getNotBeforeDeplay()).isEqualTo(Duration.ofMinutes(1));
  }

  /** Test of {@link JwtConfigProperties} defaults. */
  @Test
  public void testDefaults() {

    JwtConfigProperties config = new JwtConfigProperties();
    assertThat(config.getAlgorithm()).isEqualTo("RSA");
    assertThat(config.getAlias()).isEqualTo("jwt");
    assertThat(config.getValidation().getMaxValidity()).isEqualTo(Duration.ofHours(12));
    assertThat(config.getValidation().isExpirationRequired()).isTrue();
    assertThat(config.getValidation().isNotBeforeRequired()).isTrue();
    assertThat(config.getCreation().isAddIssuedAt()).isFalse();
  }

}
