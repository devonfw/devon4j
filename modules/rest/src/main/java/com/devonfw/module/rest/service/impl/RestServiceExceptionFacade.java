package com.devonfw.module.rest.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path.Node;
import javax.validation.ValidationException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import net.sf.mmm.util.exception.api.NlsRuntimeException;
import net.sf.mmm.util.exception.api.NlsThrowable;
import net.sf.mmm.util.exception.api.SecurityErrorUserException;
import net.sf.mmm.util.exception.api.TechnicalErrorUserException;
import net.sf.mmm.util.exception.api.ValidationErrorUserException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devonfw.module.service.common.api.constants.ServiceConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is an implementation of {@link ExceptionMapper} that acts as generic exception facade for REST services. It
 * {@link #toResponse(Throwable) maps} {@link Throwable exceptions} to an according HTTP status code and JSON result as
 * defined by <a href="https://github.com/oasp-forge/oasp4j-wiki/wiki/guide-rest#error-results">OASP REST error
 * specification</a>.
 *
 * @since 2.0.0
 */
@Provider
public class RestServiceExceptionFacade implements ExceptionMapper<Throwable> {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(RestServiceExceptionFacade.class);

  private final List<Class<? extends Throwable>> securityExceptions;

  private final Class<? extends Throwable> transactionSystemException;

  private final Class<? extends Throwable> rollbackException;

  private ObjectMapper mapper;

  private boolean exposeInternalErrorDetails;

  /**
   * The constructor.
   */
  public RestServiceExceptionFacade() {

    super();
    this.securityExceptions = new ArrayList<>();
    registerToplevelSecurityExceptions();
    this.transactionSystemException = loadException("org.springframework.transaction.TransactionSystemException");
    this.rollbackException = loadException("javax.persistence.RollbackException");
  }

  /**
   * Registers a {@link Class} as a top-level security {@link Throwable exception}. Instances of this class and all its
   * subclasses will be handled as security errors. Therefore an according HTTP error code is used and no further
   * details about the exception is send to the client to prevent
   * <a href="https://www.owasp.org/index.php/Top_10_2013-A6-Sensitive_Data_Exposure">sensitive data exposure</a>.
   *
   * @param securityException is the {@link Class} reflecting the security error.
   */
  protected void registerToplevelSecurityException(Class<? extends Throwable> securityException) {

    this.securityExceptions.add(securityException);
  }

  /**
   * This method registers the {@link #registerToplevelSecurityException(Class) top-level security exceptions}. You may
   * override it to add additional or other classes.
   */
  protected void registerToplevelSecurityExceptions() {

    this.securityExceptions.add(SecurityException.class);
    this.securityExceptions.add(SecurityErrorUserException.class);
    registerToplevelSecurityExceptions("org.springframework.security.access.AccessDeniedException");
    registerToplevelSecurityExceptions("org.springframework.security.authentication.AuthenticationServiceException");
    registerToplevelSecurityExceptions(
        "org.springframework.security.authentication.AuthenticationCredentialsNotFoundException");
    registerToplevelSecurityExceptions("org.springframework.security.authentication.BadCredentialsException");
    registerToplevelSecurityExceptions("org.springframework.security.authentication.AccountExpiredException");
  }

  /**
   * @param className the className to be registered
   */
  protected void registerToplevelSecurityExceptions(String className) {

    Class<? extends Throwable> securityException = loadException(className);
    if (securityException != null) {
      registerToplevelSecurityException(securityException);
    }
  }

  private Class<? extends Throwable> loadException(String className) {

    try {
      @SuppressWarnings("unchecked")
      Class<? extends Throwable> exception = (Class<? extends Throwable>) Class.forName(className);
      return exception;
    } catch (ClassNotFoundException e) {
      LOG.info("Exception {} was not found on classpath and can not be handled by this {}.", className,
          getClass().getSimpleName());
    } catch (Exception e) {
      LOG.error("Exception {} is invalid and can not be handled by this {}.", className, getClass().getSimpleName(), e);
    }
    return null;
  }

  @Override
  public Response toResponse(Throwable exception) {

    if (exception instanceof WebApplicationException) {
      return createResponse((WebApplicationException) exception);
    } else if (exception instanceof NlsRuntimeException) {
      return toResponse(exception, exception);
    } else {
      Throwable error = exception;
      Throwable catched = exception;
      error = getRollbackCause(exception);
      if (error == null) {
        error = unwrapNlsUserError(exception);
      }
      if (error == null) {
        error = exception;
      }
      return toResponse(error, catched);
    }
  }

  /**
   * Unwraps potential NLS user error from a wrapper exception such as {@code JsonMappingException} or
   * {@code PersistenceException}.
   *
   * @param exception the exception to unwrap.
   * @return the unwrapped {@link NlsRuntimeException} exception or {@code null} if no
   *         {@link NlsRuntimeException#isForUser() use error}.
   */
  private NlsRuntimeException unwrapNlsUserError(Throwable exception) {

    Throwable cause = exception.getCause();
    if (cause instanceof NlsRuntimeException) {
      NlsRuntimeException nlsError = (NlsRuntimeException) cause;
      if (nlsError.isForUser()) {
        return nlsError;
      }
    }
    return null;
  }

  private Throwable getRollbackCause(Throwable exception) {

    Class<?> exceptionClass = exception.getClass();
    if (exceptionClass == this.transactionSystemException) {
      Throwable cause = exception.getCause();
      if (cause != null) {
        exceptionClass = cause.getClass();
        if (exceptionClass == this.rollbackException) {
          return cause.getCause();
        }
      }
    }
    return null;
  }

  /**
   * @see #toResponse(Throwable)
   *
   * @param exception the exception to handle
   * @param catched the original exception that was cached. Either same as {@code error} or a (child-)
   *        {@link Throwable#getCause() cause} of it.
   * @return the response build from the exception.
   */
  protected Response toResponse(Throwable exception, Throwable catched) {

    if (exception instanceof ValidationException) {
      return handleValidationException(exception, catched);
    } else if (exception instanceof ValidationErrorUserException) {
      return createResponse(exception, (ValidationErrorUserException) exception, null);
    } else {
      Class<?> exceptionClass = exception.getClass();
      for (Class<?> securityError : this.securityExceptions) {
        if (securityError.isAssignableFrom(exceptionClass)) {
          return handleSecurityError(exception, catched);
        }
      }
      return handleGenericError(exception, catched);
    }
  }

  /**
   * Creates the {@link Response} for the given validation exception.
   *
   * @param exception is the original validation exception.
   * @param error is the wrapped exception or the same as <code>exception</code>.
   * @param errorsMap is a map with all validation errors
   * @return the requested {@link Response}.
   */
  protected Response createResponse(Throwable exception, ValidationErrorUserException error,
      Map<String, List<String>> errorsMap) {

    LOG.warn("Service failed due to validation failure.", error);
    if (exception == error) {
      return createResponse(Status.BAD_REQUEST, error, errorsMap);
    } else {
      return createResponse(Status.BAD_REQUEST, error, exception.getMessage(), errorsMap);
    }
  }

  /**
   * Exception handling for generic exception (fallback).
   *
   * @param exception the exception to handle
   * @param catched the original exception that was cached. Either same as {@code error} or a (child-)
   *        {@link Throwable#getCause() cause} of it.
   * @return the response build from the exception
   */
  protected Response handleGenericError(Throwable exception, Throwable catched) {

    NlsRuntimeException userError;
    boolean logged = false;
    if (exception instanceof NlsThrowable) {
      NlsThrowable nlsError = (NlsThrowable) exception;
      if (!nlsError.isTechnical()) {
        LOG.warn("Service failed due to business error: {}", nlsError.getMessage());
        logged = true;
      }
      userError = TechnicalErrorUserException.getOrCreateUserException(exception);
    } else {
      userError = TechnicalErrorUserException.getOrCreateUserException(catched);
    }
    if (!logged) {
      LOG.error("Service failed on server", userError);
    }
    return createResponse(userError);
  }

  /**
   * Exception handling for security exception.
   *
   * @param exception the exception to handle
   * @param catched the original exception that was cached. Either same as {@code error} or a (child-)
   *        {@link Throwable#getCause() cause} of it.
   * @return the response build from exception
   */
  protected Response handleSecurityError(Throwable exception, Throwable catched) {

    NlsRuntimeException error;
    if ((exception == catched) && (exception instanceof NlsRuntimeException)) {
      error = (NlsRuntimeException) exception;
    } else {
      error = new SecurityErrorUserException(catched);
    }
    LOG.warn("Service failed due to security error", error);
    // NOTE: for security reasons we do not send any details about the error to the client!
    String message;
    String code = null;
    if (this.exposeInternalErrorDetails) {
      message = getExposedErrorDetails(error);
    } else {
      message = "forbidden";
    }
    return createResponse(Status.FORBIDDEN, message, code, error.getUuid(), null);
  }

  /**
   * Exception handling for validation exception.
   *
   * @param exception the exception to handle
   * @param catched the original exception that was cached. Either same as {@code error} or a (child-)
   *        {@link Throwable#getCause() cause} of it.
   * @return the response build from the exception.
   */
  protected Response handleValidationException(Throwable exception, Throwable catched) {

    Throwable t = catched;
    Map<String, List<String>> errorsMap = null;
    if (exception instanceof ConstraintViolationException) {
      ConstraintViolationException constraintViolationException = (ConstraintViolationException) exception;
      Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();
      errorsMap = new HashMap<>();

      for (ConstraintViolation<?> violation : violations) {
        Iterator<Node> it = violation.getPropertyPath().iterator();
        String fieldName = null;

        // Getting fieldname from the exception
        while (it.hasNext()) {
          fieldName = it.next().toString();
        }

        List<String> errorsList = errorsMap.get(fieldName);

        if (errorsList == null) {
          errorsList = new ArrayList<>();
          errorsMap.put(fieldName, errorsList);
        }

        errorsList.add(violation.getMessage());

      }

      t = new ValidationException(errorsMap.toString(), catched);
    }
    ValidationErrorUserException error = new ValidationErrorUserException(t);
    return createResponse(t, error, errorsMap);
  }

  /**
   * @param error is the {@link Throwable} to extract message details from.
   * @return the exposed message(s).
   */
  protected String getExposedErrorDetails(Throwable error) {

    StringBuilder buffer = new StringBuilder();
    Throwable e = error;
    while (e != null) {
      if (buffer.length() > 0) {
        buffer.append(System.lineSeparator());
      }
      buffer.append(e.getClass().getSimpleName());
      buffer.append(": ");
      buffer.append(e.getLocalizedMessage());
      e = e.getCause();
    }
    return buffer.toString();
  }

  /**
   * Create the {@link Response} for the given {@link NlsRuntimeException}.
   *
   * @param error the generic {@link NlsRuntimeException}.
   * @return the corresponding {@link Response}.
   */
  protected Response createResponse(NlsRuntimeException error) {

    Status status;
    if (error.isTechnical()) {
      status = Status.INTERNAL_SERVER_ERROR;
    } else {
      status = Status.BAD_REQUEST;
    }
    return createResponse(status, error, null);
  }

  /**
   * Create a response message as a JSON-String from the given parts.
   *
   * @param status is the HTTP {@link Status}.
   * @param error is the catched or wrapped {@link NlsRuntimeException}.
   * @param errorsMap is a map with all validation errors
   * @return the corresponding {@link Response}.
   */
  protected Response createResponse(Status status, NlsRuntimeException error, Map<String, List<String>> errorsMap) {

    String message;
    if (this.exposeInternalErrorDetails) {
      message = getExposedErrorDetails(error);
    } else {
      message = error.getLocalizedMessage();
    }
    return createResponse(status, error, message, errorsMap);
  }

  /**
   * Create a response message as a JSON-String from the given parts.
   *
   * @param status is the HTTP {@link Status}.
   * @param error is the catched or wrapped {@link NlsRuntimeException}.
   * @param message is the JSON message attribute.
   * @param errorsMap is a map with all validation errors
   * @return the corresponding {@link Response}.
   */
  protected Response createResponse(Status status, NlsRuntimeException error, String message,
      Map<String, List<String>> errorsMap) {

    return createResponse(status, error, message, error.getCode(), errorsMap);
  }

  /**
   * Create a response message as a JSON-String from the given parts.
   *
   * @param status is the HTTP {@link Status}.
   * @param error is the catched or wrapped {@link NlsRuntimeException}.
   * @param message is the JSON message attribute.
   * @param code is the {@link NlsRuntimeException#getCode() error code}.
   * @param errorsMap is a map with all validation errors
   * @return the corresponding {@link Response}.
   */
  protected Response createResponse(Status status, NlsRuntimeException error, String message, String code,
      Map<String, List<String>> errorsMap) {

    return createResponse(status, message, code, error.getUuid(), errorsMap);
  }

  /**
   * Create a response message as a JSON-String from the given parts.
   *
   * @param status is the HTTP {@link Status}.
   * @param message is the JSON message attribute.
   * @param code is the {@link NlsRuntimeException#getCode() error code}.
   * @param uuid the {@link UUID} of the response message.
   * @param errorsMap is a map with all validation errors
   * @return the corresponding {@link Response}.
   */
  protected Response createResponse(Status status, String message, String code, UUID uuid,
      Map<String, List<String>> errorsMap) {

    String json = createJsonErrorResponseMessage(message, code, uuid, errorsMap);
    return Response.status(status).entity(json).build();
  }

  /**
   * Create a response message as a JSON-String from the given parts.
   *
   * @param message the message of the response message
   * @param code the code of the response message
   * @param uuid the uuid of the response message
   * @param errorsMap is a map with all validation errors
   * @return the response message as a JSON-String
   */
  protected String createJsonErrorResponseMessage(String message, String code, UUID uuid,
      Map<String, List<String>> errorsMap) {

    Map<String, Object> jsonMap = new HashMap<>();
    if (message != null) {
      jsonMap.put(ServiceConstants.KEY_MESSAGE, message);
    }
    if (code != null) {
      jsonMap.put(ServiceConstants.KEY_CODE, code);
    }
    if (uuid != null) {
      jsonMap.put(ServiceConstants.KEY_UUID, uuid.toString());
    }

    if (errorsMap != null) {
      jsonMap.put(ServiceConstants.KEY_ERRORS, errorsMap);
    }

    String responseMessage = "";
    try {
      responseMessage = this.mapper.writeValueAsString(jsonMap);
    } catch (JsonProcessingException e) {
      LOG.error("Exception facade failed to create JSON.", e);
      responseMessage = "{}";
    }
    return responseMessage;

  }

  /**
   * Add a response message to an existing response.
   *
   * @param exception the {@link WebApplicationException}.
   * @return the response with the response message added
   */
  protected Response createResponse(WebApplicationException exception) {

    Response response = exception.getResponse();
    int statusCode = response.getStatus();
    Status status = Status.fromStatusCode(statusCode);
    NlsRuntimeException error;
    if (exception instanceof ServerErrorException) {
      error = new TechnicalErrorUserException(exception);
      LOG.error("Service failed on server", error);
      return createResponse(status, error, null);
    } else {
      UUID uuid = UUID.randomUUID();
      if (exception instanceof ClientErrorException) {
        LOG.warn("Service failed due to unexpected request. UUDI: {}, reason: {} ", uuid, exception.getMessage());
      } else {
        LOG.warn("Service caused redirect or other error. UUID: {}, reason: {}", uuid, exception.getMessage());
      }
      return createResponse(status, exception.getMessage(), String.valueOf(statusCode), uuid, null);
    }

  }

  /**
   * @return the {@link ObjectMapper} for JSON mapping.
   */
  public ObjectMapper getMapper() {

    return this.mapper;
  }

  /**
   * @param mapper the mapper to set
   */
  @Inject
  public void setMapper(ObjectMapper mapper) {

    this.mapper = mapper;
  }

  /**
   * @param exposeInternalErrorDetails - {@code true} if internal exception details shall be exposed to clients (useful
   *        for debugging and testing), {@code false} if such details are hidden to prevent
   *        <a href="https://www.owasp.org/index.php/Top_10_2013-A6-Sensitive_Data_Exposure">Sensitive Data Exposure</a>
   *        (default, has to be used in production environment).
   */
  public void setExposeInternalErrorDetails(boolean exposeInternalErrorDetails) {

    this.exposeInternalErrorDetails = exposeInternalErrorDetails;
    if (exposeInternalErrorDetails) {
      String message = "****** Exposing of internal error details is enabled! This violates OWASP A6 (Sensitive Data Exposure) and shall only be used for testing/debugging and never in production. ******";
      LOG.warn(message);
      // CHECKSTYLE:OFF (for development only)
      System.err.println(message);
      // CHECKSTYLE:ON
    }
  }

  /**
   * @return exposeInternalErrorDetails the value set by {@link #setExposeInternalErrorDetails(boolean)}.
   */
  public boolean isExposeInternalErrorDetails() {

    return this.exposeInternalErrorDetails;
  }

}
