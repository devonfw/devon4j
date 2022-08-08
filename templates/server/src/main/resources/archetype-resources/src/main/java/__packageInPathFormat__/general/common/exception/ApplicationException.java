package ${package}.general.common.exception;

import java.util.UUID;

/**
 * Extends {@link RuntimeException} with the following features:
 * <ul>
 * <li>a {@link #getUuid() UUID} unique per exception instance automatically generated once per exception
 * {@link #getCause() chain}.</li>
 * <li>an {@link #getCode() error code} that should be unique per exception type.</li>
 * <li>distinction between exceptions {@link #isForUser() intended for end-users} and internal (technical ones) to
 * prevent OWASP sensitive data exposure.</li>
 * </ul>
 * <b>NOTE:</b><br>
 * Exceptions should only occur in unexpected or undesired situations. Never use exceptions for control flows.
 *
 * For native language support add <a href="https://github.com/m-m-m/base">io.github.m-m-m:mmm-base</a> as dependency
 * and use {@code io.github.mmm.base.exception.ApplicationException} instead of this class. Code taken from <a href=
 * "https://github.com/m-m-m/base/blob/2cd81645b6a9eb9fa8bb95744b7fbe8f5915a08a/core/src/main/java/io/github/mmm/base/exception/ApplicationException.java">ApplicationException</a>
 * with friendly permission of the author to do whatever you like with the code including any commercial use.
 */
public abstract class ApplicationException extends RuntimeException {

  private final UUID uuid;

  /**
   * The constructor.
   *
   * @param message the {@link #getMessage() message} describing the problem briefly.
   */
  public ApplicationException(String message) {

    this(message, null);
  }

  /**
   * The constructor.
   *
   * @param message the {@link #getMessage() message} describing the problem briefly.
   * @param cause is the {@link #getCause() cause} of this exception.
   */
  public ApplicationException(String message, Throwable cause) {

    this(message, cause, null);
  }

  /**
   * The constructor.
   *
   * @param message the {@link #getMessage() message} describing the problem briefly.
   * @param cause is the {@link #getCause() cause} of this exception. May be <code>null</code>.
   * @param uuid the explicit {@link #getUuid() UUID} or <code>null</code> to initialize by default (from given
   *        {@link Throwable} or as new {@link UUID}).
   */
  protected ApplicationException(String message, Throwable cause, UUID uuid) {

    super(message, cause);
    if (uuid == null) {
      if ((cause != null) && (cause instanceof ApplicationException)) {
        this.uuid = ((ApplicationException) cause).getUuid();
      } else {
        this.uuid = createUuid();
      }
    } else {
      this.uuid = uuid;
    }
  }

  /**
   * @return a new {@link UUID} for {@link #getUuid()}.
   */
  protected UUID createUuid() {

    return UUID.randomUUID();
  }

  /**
   * This method gets the {@link UUID} of this {@link ApplicationException}. When a new {@link ApplicationException} is
   * created, a {@link UUID} is assigned. In case the {@link ApplicationException} is created from another
   * {@link ApplicationException} as cause, the existing {@link UUID} will be used from that cause. Otherwise a new
   * {@link UUID} is generated. <br>
   * The {@link UUID} will appear in the {@link #printStackTrace() stacktrace} but NOT in the
   * {@link Throwable#getMessage() message}. It will therefore be written to log-files if this exception is logged. If
   * you supply the {@link UUID} to the end-user, he can provide it with the problem report so an administrator or
   * software developer can easily find the stacktrace in the log-files.
   *
   * @return the {@link UUID} of this object.
   */
  public final UUID getUuid() {

    return this.uuid;
  }

  @Override
  public String getMessage() {

    StringBuilder buffer = new StringBuilder();
    buffer.append(super.getMessage());
    buffer.append(System.lineSeparator());
    buffer.append(this.uuid);
    String code = getCode();
    if (!getClass().getSimpleName().equals(code)) {
      buffer.append(":");
      buffer.append(code);
    }
    return buffer.toString();
  }

  /**
   * This method gets the <em>code</em> that identifies the detailed type of this object. While {@link #getUuid() UUID}
   * is unique per instance of a {@link ApplicationException} this code is a short and readable identifier representing
   * the {@link ApplicationException} {@link Class}. The default implementation returns the {@link Class#getSimpleName()
   * simple name}. However, the code should remain stable after refactoring (so at least after the rename the previous
   * code should be returned as {@link String} literal). This code may be used as a compact identifier to reference the
   * related problem or information as well as for automatic tests of error situations that should remain stable even if
   * the message text gets improved or the locale is unknown.
   *
   * @return the error code.
   */
  public String getCode() {

    return getClass().getSimpleName();
  }

  /**
   * <b>Note:</b> Please consider using {@code net.sf.mmm.nls.exception.NlsException} with i18n support in case you are
   * creating exceptions for end-users.
   *
   * @return {@code true} if the {@link #getMessage() message} of this exception is for end-users (or clients),
   *         {@code false} otherwise (for internal {@link #isTechnical() technical} errors).
   */
  public boolean isForUser() {

    return false;
  }

}
