package com.devonfw.module.security.jwt.common.base.kafka;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.codec.Charsets;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devonfw.module.security.jwt.common.api.JwtComponentTest;
import com.devonfw.module.security.jwt.common.base.JwtConstants;

/**
 *
 */
@ExtendWith({ MockitoExtension.class, SpringExtension.class })
@EmbeddedKafka(topics = JwtTokenValidationAspectTest.TEST_TOPIC)
public class JwtTokenValidationAspectTest extends JwtComponentTest {

  /**
   *
   */
  public static final String TEST_TOPIC = "jwt-test";

  @Mock
  private ProceedingJoinPoint proceedingJoinPoint;

  @Mock
  private JwtAuthentication jwtAuthentication;

  @Inject
  private JwtTokenValidationAspect jwtTokenValidationAspect;

  private ConsumerRecord<String, String> consumerRecord;

  /**
   *
   */
  @BeforeEach
  public void doSetup() {

  }

  /**
   * @throws Throwable
   */
  @Test
  public void shouldValidate_whenValidTokenAndRolesPassedAsHeaders() throws Throwable {

    // Arrange
    this.consumerRecord = new ConsumerRecord<>(TEST_TOPIC, 0, 0, "jwt-test", "message");
    Headers headers = this.consumerRecord.headers();
    headers.add(JwtConstants.HEADER_AUTHORIZATION, TEST_JWT.getBytes(Charsets.UTF_8));

    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(TEST_ROLE_READ_MASTER_DATA));
    authorities.add(new SimpleGrantedAuthority(TEST_ROLE_SAVE_USER));

    when(this.jwtAuthentication.failOnMissingToken()).thenReturn(true);

    // Act
    this.jwtTokenValidationAspect.authenticateToken(this.proceedingJoinPoint, this.jwtAuthentication,
        this.consumerRecord);

    // Assert
    verify(this.proceedingJoinPoint, times(1)).proceed();
  }

  /**
   * @throws Throwable
   *
   */
  @Test
  public void shouldValidate_whenInValidateTokenIsPassed() throws Throwable {

    // Arrange
    this.consumerRecord = new ConsumerRecord<>(TEST_TOPIC, 0, 0, "jwt-test", "message");
    Headers headers = this.consumerRecord.headers();
    headers.add(JwtConstants.HEADER_AUTHORIZATION, INVALID_TEST_JWT.getBytes(Charsets.UTF_8));

    when(this.jwtAuthentication.failOnMissingToken()).thenReturn(true);

    // Act
    this.jwtTokenValidationAspect.authenticateToken(this.proceedingJoinPoint, this.jwtAuthentication,
        this.consumerRecord);

    // Assert
    verify(this.proceedingJoinPoint, never()).proceed();
  }

  /**
   * @throws Throwable
   *
   */
  @Test
  public void shouldValidate_whenTokenIsNotGiven() throws Throwable {

    // Arrange
    this.consumerRecord = new ConsumerRecord<>(TEST_TOPIC, 0, 0, "jwt-test", "message");
    when(this.jwtAuthentication.failOnMissingToken()).thenReturn(true);

    // Act
    // Assert
    assertThrows(MissingTokenException.class, () -> this.jwtTokenValidationAspect
        .authenticateToken(this.proceedingJoinPoint, this.jwtAuthentication, this.consumerRecord));
    verify(this.proceedingJoinPoint, never()).proceed();
  }
}
