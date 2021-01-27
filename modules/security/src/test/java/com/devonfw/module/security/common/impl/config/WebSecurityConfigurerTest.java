package com.devonfw.module.security.common.impl.config;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import com.devonfw.module.security.common.api.config.WebSecurityConfigurerAspect;

/**
 *
 */

@ExtendWith(MockitoExtension.class)
public class WebSecurityConfigurerTest {

  private WebSecurityConfigurerImpl webSecurityConfigurer;

  @Mock
  WebSecurityConfigurerAspect aspect;

  @BeforeEach
  public void init() {

    this.webSecurityConfigurer = new WebSecurityConfigurerImpl();
    this.webSecurityConfigurer.setAspects(singletonList(this.aspect));
  }

  /**
   * To test webSecurityConfigurer configure
   *
   * @throws Exception
   */
  @Test
  public void shouldConfigure_withAspect() throws Exception {

    // Arrange
    HttpSecurity expectedHttp = new HttpSecurity(mock(ObjectPostProcessor.class),
        mock(AuthenticationManagerBuilder.class), new HashMap<>());
    when(this.aspect.configure(expectedHttp)).thenReturn(expectedHttp);

    // Act
    HttpSecurity resultHttp = this.webSecurityConfigurer.configure(expectedHttp);

    // Assert
    assertThat(resultHttp).isEqualTo(expectedHttp);
  }
}
