package com.devonfw.module.rest.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.constraints.Min;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import net.sf.mmm.util.exception.api.IllegalCaseException;
import net.sf.mmm.util.exception.api.NlsRuntimeException;
import net.sf.mmm.util.exception.api.ObjectNotFoundUserException;
import net.sf.mmm.util.exception.api.SecurityErrorUserException;
import net.sf.mmm.util.exception.api.TechnicalErrorUserException;
import net.sf.mmm.util.exception.api.ValidationErrorUserException;

import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

import com.devonfw.module.service.common.api.constants.ServiceConstants;
import com.devonfw.module.test.common.base.ModuleTest;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test-case for {@link RestServiceExceptionFacade}.
 *
 */
public class RestServiceExceptionFacadeTest extends ModuleTest {

  /** Value of {@link TechnicalErrorUserException#getCode()}. */
  private static final String CODE_TECHNICAL_ERROR = "TechnicalError";

  /** Placeholder for any UUID. */
  private static final String UUID_ANY = "<any-uuid>";

  /**
   * @return the {@link RestServiceExceptionFacade} instance to test.
   */
  protected RestServiceExceptionFacade getExceptionFacade() {

    RestServiceExceptionFacade facade = new RestServiceExceptionFacade();
    facade.setMapper(new ObjectMapper());
    return facade;
  }

  /**
   * Tests {@link RestServiceExceptionFacade#toResponse(Throwable)} with constraint violations
   */
  @Test
  public void testConstraintViolationExceptions() {

    class CounterTest {

      @Min(value = 10)
      private Integer count;

      public CounterTest(Integer count) {

        this.count = count;
      }

    }

    CounterTest counter = new CounterTest(new Integer(1));

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    Set<ConstraintViolation<CounterTest>> violations = validator.validate(counter);

    RestServiceExceptionFacade exceptionFacade = getExceptionFacade();
    String message = "{count=[" + violations.iterator().next().getMessage() + "]}";
    String errors = "{count=[" + violations.iterator().next().getMessage() + "]}";
    Throwable error = new ConstraintViolationException(violations);
    checkFacade(exceptionFacade, error, 400, message, UUID_ANY, ValidationErrorUserException.CODE, errors);

  }

  /**
   * Tests {@link RestServiceExceptionFacade#toResponse(Throwable)} with forbidden security exception including
   * subclasses.
   */
  @Test
  public void testSecurityExceptions() {

    RestServiceExceptionFacade exceptionFacade = getExceptionFacade();

    String secretMessage = "Secret information not to be revealed on client - only to be logged on server!";

    int statusCode = 403;
    String message = "forbidden";
    String code = null;

    checkFacade(exceptionFacade, new AccessDeniedException(secretMessage), statusCode, message, UUID_ANY, code);
    checkFacade(exceptionFacade, new AuthenticationCredentialsNotFoundException(secretMessage), statusCode, message,
        UUID_ANY, code, null);
    checkFacade(exceptionFacade, new BadCredentialsException(secretMessage), statusCode, message, UUID_ANY, code);
    checkFacade(exceptionFacade, new AccountExpiredException(secretMessage), statusCode, message, UUID_ANY, code);
    checkFacade(exceptionFacade, new InternalAuthenticationServiceException(secretMessage), statusCode, message,
        UUID_ANY, code, null);
    SecurityErrorUserException error = new SecurityErrorUserException();
    checkFacade(exceptionFacade, error, statusCode, message, error.getUuid().toString(), code);
  }

  /**
   * Tests {@link RestServiceExceptionFacade#toResponse(Throwable)} with forbidden security exception including
   * subclasses.
   */
  @Test
  public void testSecurityExceptionExposed() {

    RestServiceExceptionFacade exceptionFacade = getExceptionFacade();
    exceptionFacade.setExposeInternalErrorDetails(true);

    String secretMessage = "Secret information not to be revealed on client - only to be logged on server!";

    int statusCode = 403;
    String message = "The operation failed due to security restrictions. Please contact the support in case of a permission problem.";
    String code = null;

    checkFacade(exceptionFacade, new AccessDeniedException(secretMessage), statusCode,
        "SecurityErrorUserException: " + message + System.lineSeparator() + "AccessDeniedException: " + secretMessage,
        UUID_ANY, code);
  }

  /**
   * Checks that the specified {@link RestServiceExceptionFacade} provides the expected results for the given
   * {@link Throwable}.
   *
   * @param exceptionFacade is the {@link RestServiceExceptionFacade} to test.
   * @param error is the {@link Throwable} to convert.
   * @param statusCode is the expected {@link Response#getStatus() status} code.
   * @param message is the expected {@link Throwable#getMessage() error message} from the JSON result.
   * @param uuid is the expected {@link NlsRuntimeException#getUuid() UUID} from the JSON result. May be {@code null}.
   * @param code is the expected {@link NlsRuntimeException#getCode() error code} from the JSON result. May be
   *        {@code null}.
   * @return the JSON result for potential further asserts.
   */
  protected String checkFacade(RestServiceExceptionFacade exceptionFacade, Throwable error, int statusCode,
      String message, String uuid, String code) {

    return checkFacade(exceptionFacade, error, statusCode, message, uuid, code, null);
  }

  /**
   * Checks that the specified {@link RestServiceExceptionFacade} provides the expected results for the given
   * {@link Throwable}.
   *
   * @param exceptionFacade is the {@link RestServiceExceptionFacade} to test.
   * @param error is the {@link Throwable} to convert.
   * @param statusCode is the expected {@link Response#getStatus() status} code.
   * @param message is the expected {@link Throwable#getMessage() error message} from the JSON result.
   * @param uuid is the expected {@link NlsRuntimeException#getUuid() UUID} from the JSON result. May be {@code null}.
   * @param code is the expected {@link NlsRuntimeException#getCode() error code} from the JSON result. May be
   *        {@code null}.
   * @param errors is the expected validation errors in a format key-value
   * @return the JSON result for potential further asserts.
   */
  @SuppressWarnings("unchecked")
  protected String checkFacade(RestServiceExceptionFacade exceptionFacade, Throwable error, int statusCode,
      String message, String uuid, String code, String errors) {

    Response response = exceptionFacade.toResponse(error);
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(statusCode);

    Object entity = response.getEntity();
    assertThat(entity).isInstanceOf(String.class);
    String result = (String) entity;

    try {
      Map<String, Object> valueMap = exceptionFacade.getMapper().readValue(result, Map.class);
      String msg = message;
      if (msg == null) {
        msg = error.getLocalizedMessage();
      }
      assertThat(valueMap.get(ServiceConstants.KEY_MESSAGE)).isEqualTo(msg);
      if ((statusCode == 403) && (!exceptionFacade.isExposeInternalErrorDetails())) {
        assertThat(result).doesNotContain(error.getMessage());
      }
      assertThat(valueMap.get(ServiceConstants.KEY_CODE)).isEqualTo(code);
      String actualUuid = (String) valueMap.get(ServiceConstants.KEY_UUID);
      if (UUID_ANY.equals(uuid)) {
        if (actualUuid == null) {
          fail("UUID expected but not found in response: " + result);
        }
      } else {
        assertThat(actualUuid).isEqualTo(uuid);
      }

      Map<String, List<String>> errorsMap = (Map<String, List<String>>) valueMap.get(ServiceConstants.KEY_ERRORS);

      if (errors == null) {
        if (errorsMap != null) {
          fail("Errors do not expected but found in response: " + result);
        } else {
          assertThat(errorsMap).isEqualTo(errors);
        }
      } else {
        if (errorsMap != null) {
          assertThat(errorsMap.toString()).isEqualTo(errors);
        } else {
          fail("Errors expected but not found in response: " + result);
        }

      }

    } catch (Exception e) {
      throw new IllegalStateException(e.getMessage(), e);
    }
    return result;
  }

  /**
   * Tests {@link RestServiceExceptionFacade#toResponse(Throwable)} with bad request technical exception including
   * subclasses.
   */
  @Test
  public void testJaxrsInternalServerException() {

    RestServiceExceptionFacade exceptionFacade = getExceptionFacade();
    String internalMessage = "The HTTP request is invalid";
    int statusCode = 500;
    InternalServerErrorException error = new InternalServerErrorException(internalMessage);
    String expectedMessage = new TechnicalErrorUserException(error).getLocalizedMessage();
    checkFacade(exceptionFacade, error, statusCode, expectedMessage, UUID_ANY, CODE_TECHNICAL_ERROR);
  }

  /**
   * Tests {@link RestServiceExceptionFacade#toResponse(Throwable)} with bad request technical exception.
   */
  @Test
  public void testJaxrsBadRequestException() {

    RestServiceExceptionFacade exceptionFacade = getExceptionFacade();
    String message = "The HTTP request is invalid";
    Throwable error = new BadRequestException(message);
    checkFacade(exceptionFacade, error, 400, message, UUID_ANY, "400");
  }

  /**
   * Tests {@link RestServiceExceptionFacade#toResponse(Throwable)} with a {@link ValidationException}.
   */
  @Test
  public void testValidationException() {

    RestServiceExceptionFacade exceptionFacade = getExceptionFacade();
    String message = "Validation failed!";
    Throwable error = new ValidationException(message);
    checkFacade(exceptionFacade, error, 400, message, UUID_ANY, ValidationErrorUserException.CODE);
  }

  /**
   * Tests {@link RestServiceExceptionFacade#toResponse(Throwable)} with bad request technical exception including
   * subclasses.
   */
  @Test
  public void testJaxrsNotFoundException() {

    RestServiceExceptionFacade exceptionFacade = getExceptionFacade();
    String internalMessage = "Either the service URL is wrong or the requested resource does not exist";
    checkFacade(exceptionFacade, new NotFoundException(internalMessage), 404, internalMessage, UUID_ANY, "404");
  }

  /**
   * Tests {@link RestServiceExceptionFacade#toResponse(Throwable)} with bad request technical exception including
   * subclasses.
   */
  @Test
  public void testTechnicalJavaRuntimeServerException() {

    RestServiceExceptionFacade exceptionFacade = getExceptionFacade();
    String secretMessage = "Internal server error occurred";
    IllegalArgumentException error = new IllegalArgumentException(secretMessage);
    String expectedMessage = new TechnicalErrorUserException(error).getLocalizedMessage();
    checkFacade(exceptionFacade, error, 500, expectedMessage, UUID_ANY, CODE_TECHNICAL_ERROR);
  }

  /**
   * Tests {@link RestServiceExceptionFacade#toResponse(Throwable)} with bad request technical exception including
   * subclasses.
   */
  @Test
  public void testTechnicalCustomRuntimeServerException() {

    RestServiceExceptionFacade exceptionFacade = getExceptionFacade();
    String message = "Internal server error occurred";
    IllegalCaseException error = new IllegalCaseException(message);
    String expectedMessage = new TechnicalErrorUserException(error).getLocalizedMessage();
    checkFacade(exceptionFacade, error, 500, expectedMessage, error.getUuid().toString(), CODE_TECHNICAL_ERROR);
  }

  /**
   * Tests {@link RestServiceExceptionFacade#toResponse(Throwable)} with bad request technical exception including
   * subclasses.
   */
  @Test
  public void testTechnicalCustomRuntimeServerExceptionExposed() {

    RestServiceExceptionFacade exceptionFacade = getExceptionFacade();
    exceptionFacade.setExposeInternalErrorDetails(true);
    String message = "Internal server error occurred";
    IllegalCaseException error = new IllegalCaseException(message);
    String expectedMessage = "TechnicalErrorUserException: An unexpected error has occurred! We apologize any inconvenience. Please try again later."
        + System.lineSeparator() + error.getClass().getSimpleName() + ": " + error.getLocalizedMessage();
    checkFacade(exceptionFacade, error, 500, expectedMessage, error.getUuid().toString(), CODE_TECHNICAL_ERROR);
  }

  /**
   * Tests {@link RestServiceExceptionFacade#toResponse(Throwable)} with bad request technical exception including
   * subclasses.
   */
  @Test
  public void testBusinessException() {

    RestServiceExceptionFacade exceptionFacade = getExceptionFacade();
    ObjectNotFoundUserException error = new ObjectNotFoundUserException(4711L);
    checkFacade(exceptionFacade, error, 400, null, error.getUuid().toString(), "NotFound");
  }
}
