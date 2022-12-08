package ${package}.general.common.exception;

/**
 * {@link ApplicationBusinessException} for validation errors.
 *
 * @see javax.validation.ValidationException
 * @see javax.validation.ConstraintViolationException
 */
public class ValidationBusinessException extends ApplicationBusinessException {

  /**
   * The constructor.
   *
   * @param cause is the {@link #getCause() cause} of this exception.
   */
  public ValidationBusinessException(Throwable cause) {

    super("Validation failed - please ensure to provide complete and correct data.", cause);
  }

}
