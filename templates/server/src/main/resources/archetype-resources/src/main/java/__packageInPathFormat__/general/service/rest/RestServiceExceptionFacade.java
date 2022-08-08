package ${package}.general.service.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ${package}.general.common.exception.ApplicationException;
import ${package}.general.common.exception.TechnicalErrorUserException;
import ${package}.general.common.exception.ValidationBusinessException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * This is an implementation of {@link ExceptionMapper} that acts as generic exception facade for REST services. It
 * {@link #toResponse(Throwable) maps} {@link Throwable exceptions} to an according HTTP status code and JSON result as
 * defined by
 * <a href="https://github.com/devonfw/devon4j/blob/develop/documentation/guide-rest.asciidoc#error-results">devonfw
 * REST error specification</a>.
 */
@ApplicationScoped
@Provider
@Named
@javax.inject.Named
public class RestServiceExceptionFacade implements ExceptionMapper<Throwable> {

  /** Key for {@link Throwable#getMessage() error message}. */
  private static final String KEY_MESSAGE = "message";

  /** Key for error {@link java.util.UUID}. */
  private static final String KEY_UUID = "uuid";

  /** Key for error code. */
  private static final String KEY_CODE = "code";

  /** Key for (validation) error details. */
  private static final String KEY_ERRORS = "errors";

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(RestServiceExceptionFacade.class);

  private final Class<? extends Throwable> transactionSystemException;

  private final Class<? extends Throwable> rollbackException;

  private ObjectMapper mapper;

  private boolean exposeInternalErrorDetails;

  /**
   * The constructor.
   */
  public RestServiceExceptionFacade() {

    super();
    this.transactionSystemException = loadException("org.springframework.transaction.TransactionSystemException");
    this.rollbackException = loadException("javax.persistence.RollbackException");
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
    } else if (exception instanceof ApplicationException) {
      return toResponse(exception, exception);
    } else {
      Throwable error = exception;
      Throwable catched = exception;
      error = getRollbackCause(exception);
      if (error == null) {
        error = unwrapUserException(exception);
      }
      if (error == null) {
        error = exception;
      }
      return toResponse(error, catched);
    }
  }

  /**
   * Unwraps a potential {@link ApplicationException#isForUser() user exception} from a wrapper exception such as
   * {@code JsonMappingException} or {@code PersistenceException}.
   *
   * @param exception the exception to unwrap.
   * @return the unwrapped {@link NlsRuntimeException} exception or {@code null} if no
   *         {@link NlsRuntimeException#isForUser() use error}.
   */
  private ApplicationException unwrapUserException(Throwable exception) {

    Throwable cause = exception.getCause();
    if (cause instanceof ApplicationException) {
      ApplicationException nlsError = (ApplicationException) cause;
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
    } else {
      return handleGenericError(exception, catched);
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

    ApplicationException userException;
    boolean logged = false;
    if (exception instanceof ApplicationException) {
      ApplicationException appException = (ApplicationException) exception;
      if (appException.isForUser()) {
        LOG.warn("Service failed due to business error: {}", appException.getMessage());
        logged = true;
      }
      userException = TechnicalErrorUserException.convert(exception);
    } else {
      userException = TechnicalErrorUserException.convert(catched);
    }
    if (!logged) {
      LOG.error("Service failed on server", userException);
    }
    return createResponse(userException);
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
        // Getting fieldname from Node
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
    ValidationBusinessException error = new ValidationBusinessException(t);
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
   * Creates the {@link Response} for the given validation exception.
   *
   * @param exception is the original validation exception.
   * @param validationException is the {@link ValidationBusinessException}.
   * @param errorsMap is a map with all validation errors
   * @return the requested {@link Response}.
   */
  protected Response createResponse(Throwable exception, ValidationBusinessException validationException,
      Map<String, List<String>> errorsMap) {

    LOG.warn("Service failed due to validation failure.", validationException);
    if (exception == validationException) {
      return createResponse(Status.BAD_REQUEST, validationException, errorsMap);
    } else {
      return createResponse(Status.BAD_REQUEST, validationException, exception.getMessage(), errorsMap);
    }
  }

  /**
   * @param exception the generic {@link ApplicationException}.
   * @return the corresponding {@link Response}.
   */
  protected Response createResponse(ApplicationException exception) {

    Status status;
    if (exception.isForUser()) {
      status = Status.BAD_REQUEST;
    } else {
      status = Status.INTERNAL_SERVER_ERROR;
    }
    return createResponse(status, exception, null);
  }

  /**
   * @param status is the HTTP {@link Status}.
   * @param exception is the catched or wrapped {@link ApplicationException}.
   * @param errorsMap is a map with all validation errors
   * @return the corresponding {@link Response}.
   */
  protected Response createResponse(Status status, ApplicationException exception,
      Map<String, List<String>> errorsMap) {

    String message;
    if (this.exposeInternalErrorDetails) {
      message = getExposedErrorDetails(exception);
    } else {
      message = exception.getLocalizedMessage();
    }
    return createResponse(status, exception, message, errorsMap);
  }

  /**
   * @param status is the HTTP {@link Status}.
   * @param error is the catched or wrapped {@link ApplicationException}.
   * @param message is the JSON message attribute.
   * @param errorsMap is a map with all validation errors
   * @return the corresponding {@link Response}.
   */
  protected Response createResponse(Status status, ApplicationException error, String message,
      Map<String, List<String>> errorsMap) {

    return createResponse(status, error, message, error.getCode(), errorsMap);
  }

  /**
   * @param status is the HTTP {@link Status}.
   * @param error is the catched or wrapped {@link ApplicationException}.
   * @param message is the JSON message attribute.
   * @param code is the {@link ApplicationException#getCode() error code}.
   * @param errorsMap is a map with all validation errors
   * @return the corresponding {@link Response}.
   */
  protected Response createResponse(Status status, ApplicationException error, String message, String code,
      Map<String, List<String>> errorsMap) {

    return createResponse(status, message, code, error.getUuid(), errorsMap);
  }

  /**
   * @param status is the HTTP {@link Status}.
   * @param message is the JSON message attribute.
   * @param code is the {@link ApplicationException#getCode() error code}.
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
      jsonMap.put(KEY_MESSAGE, message);
    }
    if (code != null) {
      jsonMap.put(KEY_CODE, code);
    }
    if (uuid != null) {
      jsonMap.put(KEY_UUID, uuid.toString());
    }
    if (errorsMap != null) {
      jsonMap.put(KEY_ERRORS, errorsMap);
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
    ApplicationException error;
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
  @javax.inject.Inject
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
