package com.devonfw.module.security.jwt.common.base.kafka;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is used when the token is missing for the validation.
 */
public class MissingTokenException extends AuthenticationException {

  /**
   * The constructor.
   *
   * @param msg the error message
   */
  public MissingTokenException(String msg) {

    super(msg);
  }

  /**
   * The constructor.
   *
   * @param msg the error message
   * @param throwable the {@link Throwable}
   */
  public MissingTokenException(String msg, Throwable throwable) {

    super(msg, throwable);
  }

  /**
   * The default serial version
   */
  private static final long serialVersionUID = 1L;

}
