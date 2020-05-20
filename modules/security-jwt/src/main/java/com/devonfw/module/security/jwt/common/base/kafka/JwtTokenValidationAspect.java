package com.devonfw.module.security.jwt.common.base.kafka;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.devonfw.module.security.jwt.common.api.JwtAuthenticator;
import com.devonfw.module.security.jwt.common.base.JwtConstants;

/**
 *
 */
@Aspect
@Order(100)
public class JwtTokenValidationAspect {

  @Inject
  private JwtAuthenticator jwtAuthenticator;

  /**
   * This method is used to validate the token present in the headers of {@link ConsumerRecord}. It looks for the method
   * annotated {@link JwtAuthentication} and {@link KafkaListener} and validates the token in the
   * {@link ConsumerRecord#headers()}
   *
   * @param <K> the key type
   * @param <V> the value type
   * @param call the {@link ProceedingJoinPoint}
   * @param kafkaRecord the {@link ConsumerRecord}
   * @return Object
   * @throws Throwable the {@link Throwable}
   */
  @Around("@annotation(com.devonfw.module.kafka.common.messaging.auth.JwtAuthentication) && args(kafkaRecord,..)")
  public <K, V> Object authenticateToken(ProceedingJoinPoint call, ConsumerRecord<K, V> kafkaRecord) throws Throwable {

    authenticateToken(kafkaRecord);

    return call.proceed();
  }

  private <K, V> void authenticateToken(ConsumerRecord<K, V> kafkaRecord) {

    Headers headers = kafkaRecord.headers();

    Header authorizationHeader = headers.lastHeader(JwtConstants.HEADER_AUTHORIZATION);

    String unModifiedToken = null;

    if (authorizationHeader != null && authorizationHeader.value() != null) {
      unModifiedToken = new String(authorizationHeader.value(), StandardCharsets.UTF_8);
    }

    Optional.ofNullable(unModifiedToken).ifPresent(value -> validateTokenAndSetSecurityContext(value));
  }

  private void validateTokenAndSetSecurityContext(String value) {

    if (value.startsWith(JwtConstants.TOKEN_PREFIX)) {

      String token = value.substring(JwtConstants.TOKEN_PREFIX.length()).trim();

      Authentication authentication = this.jwtAuthenticator.authenticate(token);
      SecurityContextHolder.getContext().setAuthentication(authentication);

    }
  }

}
