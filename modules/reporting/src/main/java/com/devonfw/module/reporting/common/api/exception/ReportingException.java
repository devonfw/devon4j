package com.devonfw.module.reporting.common.api.exception;

import net.sf.mmm.util.exception.api.NlsRuntimeException;
import net.sf.mmm.util.nls.api.NlsMessage;

/**
 * This is the checked exception for Reporting module.
 *
 */
public class ReportingException extends NlsRuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * The constructor.
   *
   * @param message the error {@link #getNlsMessage() message}.
   */
  public ReportingException(NlsMessage message) {

    super(message);
  }

  /**
   * Constructs an {@code ReportingException} with the root cause.
   *
   * @param cause the error {@link #getCause() cause}.
   * @param message the error {@link #getNlsMessage() message}.
   */
  public ReportingException(Throwable cause, NlsMessage message) {

    super(cause, message);
  }

  /**
   * The constructor.
   *
   * @param cause the error {@link #getCause() cause}.
   * @param message the error message.
   */
  public ReportingException(Throwable cause, String message) {

    super(cause, message);
  }

}
