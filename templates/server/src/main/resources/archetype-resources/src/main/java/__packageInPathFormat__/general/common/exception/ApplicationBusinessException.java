package ${package}.general.common.exception;

/**
 * TODO hohwille This type ...
 *
 */
public abstract class ApplicationBusinessException extends ApplicationException {

  /**
   * The constructor.
   *
   * @param message the {@link #getMessage() message} describing the problem briefly.
   */
  public ApplicationBusinessException(String message) {

    super(message);
  }

  /**
   * The constructor.
   *
   * @param message the {@link #getMessage() message} describing the problem briefly.
   * @param cause is the {@link #getCause() cause} of this exception.
   */
  public ApplicationBusinessException(String message, Throwable cause) {

    super(message, cause);
  }

  @Override
  public boolean isForUser() {

    return true;
  }

}
