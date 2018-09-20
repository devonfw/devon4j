package ${package}.general.common.api.exception;

import net.sf.mmm.util.nls.api.NlsMessage;

/**
 * Abstract base class for technical custom exceptions of this application.
 */
public abstract class ApplicationTechnicalException extends ApplicationException {

  private static final long serialVersionUID = 1L;

  /**
   * @param message the error {@link #getNlsMessage() message}.
   */
  public ApplicationTechnicalException(NlsMessage message) {

    super(message);
  }

  /**
   * @param cause the error {@link #getCause() cause}.
   * @param message the error {@link #getNlsMessage() message}.
   */
  public ApplicationTechnicalException(Throwable cause, NlsMessage message) {

    super(cause, message);
  }

}
