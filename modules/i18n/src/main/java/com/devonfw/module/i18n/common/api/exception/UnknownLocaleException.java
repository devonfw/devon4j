package com.devonfw.module.i18n.common.api.exception;

/**
 * Signals Exception when locale is not found
 *
 */
public class UnknownLocaleException extends RuntimeException {

  /**
   * Default serial version UID
   */
  private static final long serialVersionUID = 1L;

  /**
   * Creates a new {@link UnknownLocaleException} with the given message
   *
   * @param msg error message
   */
  public UnknownLocaleException(String msg) {

    super(msg);
  }

  /**
   * Creates a new {@link UnknownLocaleException} with the given message and the given cause
   *
   * @param msg error message
   * @param ex cause of the created exception
   */
  public UnknownLocaleException(String msg, Throwable ex) {

    super(msg, ex);
  }

}
