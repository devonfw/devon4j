package ${package}.general.common.api.exception;

import net.sf.mmm.util.nls.api.NlsMessage;

/**
 * Abstract base class for business exceptions of this application.
 */
public abstract class ApplicationBusinessException extends ApplicationException {

  private static final long serialVersionUID = 1L;

  /**
   * @param message the error {@link #getNlsMessage() message}.
   */
  public ApplicationBusinessException(NlsMessage message) {

    super(message);
  }

  /**
   * @param cause the error {@link #getCause() cause}.
   * @param message the error {@link #getNlsMessage() message}.
   */
  public ApplicationBusinessException(Throwable cause, NlsMessage message) {

    super(cause, message);
  }

  @Override
  public boolean isTechnical() {

    return false;
  }

}
