package com.devonfw.module.security.jwt.common.base.kafka;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * This annotation is used to indicate that the Consumer class can be authenticated before consuming the message with
 * the jwt token given in the {@link ProducerRecord} headers.
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface JwtAuthentication {

  /**
   * This attribute is used to denote to fail the pre-auth when the token is missing.
   */
  public boolean failOnMissingToken() default true;
}
