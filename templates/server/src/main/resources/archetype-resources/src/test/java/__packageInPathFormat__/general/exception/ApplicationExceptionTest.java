package ${package}.general.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

import ${package}.BaseTest;
import ${package}.general.common.exception.ApplicationBusinessException;
import ${package}.general.common.exception.ApplicationException;

/**
 * Test of {@link ApplicationException}.
 */
public class ApplicationExceptionTest extends BaseTest {

  /** Test of {@link ApplicationException#getUuid()}. */
  @Test
  public void testUuid() {

    // given
    String message = "something wrong";
    String code = "XYZ";
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    // when
    MyTechnicalException e = new MyTechnicalException(message);
    MyTechnicalException e2 = new MyTechnicalException(message, e);
    e.printStackTrace(pw);
    String stacktrace = sw.toString();

    // then
    assertThat(e.getCode()).isEqualTo(code);
    assertThat(e.getUuid()).isNotNull();
    assertThat(e2.getUuid()).isSameAs(e.getUuid());
    assertThat(e.getMessage()).isEqualTo(message + System.lineSeparator() + e.getUuid() + ":" + code);
    assertThat(stacktrace).startsWith(MyTechnicalException.class.getName() + ": " + message + System.lineSeparator()
        + e.getUuid() + ":" + code + System.lineSeparator());
  }

  /** Test of {@link ApplicationException#isForUser()}. */
  @Test
  public void testIsForUser() {

    // given
    String message = "something wrong";

    // when
    MyTechnicalException t = new MyTechnicalException(message);
    MyBusinessException b = new MyBusinessException(message);

    // then
    assertThat(t.isForUser()).isFalse();
    assertThat(b.isForUser()).isTrue();
  }

  @SuppressWarnings("javadoc")
  public static final class MyTechnicalException extends ApplicationException {

    public MyTechnicalException(String message, Throwable cause) {

      super(message, cause);
    }

    public MyTechnicalException(String message) {

      super(message);
    }

    @Override
    public String getCode() {

      return "XYZ";
    }

  }

  @SuppressWarnings("javadoc")
  public static final class MyBusinessException extends ApplicationBusinessException {

    public MyBusinessException(String message, Throwable cause) {

      super(message, cause);
    }

    public MyBusinessException(String message) {

      super(message);
    }

  }
}
