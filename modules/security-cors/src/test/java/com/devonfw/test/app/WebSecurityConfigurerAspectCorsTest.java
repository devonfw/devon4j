package com.devonfw.test.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import com.devonfw.module.security.cors.common.impl.CorsConfigProperties;
import com.devonfw.module.security.cors.common.impl.WebSecurityConfigurerAspectCors;

/**
 * WebSecurityConfigurerAspectCorsTest
 */

@ExtendWith(MockitoExtension.class)
public class WebSecurityConfigurerAspectCorsTest {

  private WebSecurityConfigurerAspectCors webSecurityConfigurerAspectCors;

  @BeforeEach
  public void init() {

    this.webSecurityConfigurerAspectCors = new WebSecurityConfigurerAspectCors();
    CorsConfigProperties corsConfigProperties = new CorsConfigProperties();
    this.webSecurityConfigurerAspectCors.setCorsConfigProperties(corsConfigProperties);
  }

  /**
   * To test webSecurityConfigurer configure
   *
   * @throws Exception
   */
  @Test
  public void shouldConfigure_withProperties() throws Exception {

    // Arrange
    HttpSecurity expectedHttp = new HttpSecurity(mock(ObjectPostProcessor.class),
        mock(AuthenticationManagerBuilder.class), new HashMap<>());
    // Act
    HttpSecurity resultHttp = this.webSecurityConfigurerAspectCors.configure(expectedHttp);
    // Assert
    assertThat(resultHttp).isEqualTo(expectedHttp);
  }
}
