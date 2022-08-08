package ${package}.general.common.exception;

/**
 * {@link ApplicationException} wrapping an arbitrary technical error to a generic exception for end-users or clients.
 * It helps to prevent "sensitive data exposure", an OWASP top ten security vulnerability.
 */
public class TechnicalErrorUserException extends ApplicationException {

  /** @see #getCode() */
  public static final String CODE = "TechnicalError";

  /**
   * The constructor.
   *
   * @param cause is the {@link #getCause() cause} of this exception.
   */
  public TechnicalErrorUserException(Throwable cause) {

    super("An unexpected error has occurred! We apologize any inconvenience. Please try again later.", cause);
    assert (cause != null);
  }

  @Override
  public final String getCode() {

    return CODE;
  }

  @Override
  public final boolean isForUser() {

    return true;
  }

  /**
   * Gets the given {@link Throwable} as {@link #isForUser() user} {@link ApplicationException} or converts it to such.
   *
   * @param exception is the {@link Throwable} to convert.
   * @return the {@link ApplicationException} with {@link #isForUser()} returning {@code true} .
   */
  public static ApplicationException convert(Throwable exception) {

    if (exception instanceof ApplicationException) {
      ApplicationException applicationException = (ApplicationException) exception;
      if (applicationException.isForUser()) {
        return applicationException;
      }
    }
    return new TechnicalErrorUserException(exception);
  }
}
