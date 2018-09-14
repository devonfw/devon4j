package com.devonfw.module.security.common.api.exception;

/**
 * Signals an exception during reading the security configuration
 *
 */
public class InvalidConfigurationException extends RuntimeException {

  /**
   * Default serial version UID
   */
  private static final long serialVersionUID = 1L;

  /**
   * Creates a new {@link InvalidConfigurationException} with the given message
   *
   * @param message error message
   */
  public InvalidConfigurationException(String message) {

    super(message);
  }

  /**
   * Creates a new {@link InvalidConfigurationException} with the given message and the given cause
   *
   * @param message error message
   * @param ex cause of the created exception
   */
  public InvalidConfigurationException(String message, Throwable ex) {

    super(message, ex);
  }

}
