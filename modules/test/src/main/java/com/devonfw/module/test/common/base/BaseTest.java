package com.devonfw.module.test.common.base;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 * This is the {@code abstract} base class for all tests. In most cases it will be convenient to extend this class. <br>
 * <br/>
 * Although it does not contain abstract methods, the class itself is declared {@code abstract} to clarify its purpose.
 * <br/>
 * <br/>
 * This class provides {@code final} methods {@link #setUp()} and {@link #tearDown()} which call {@code protected}
 * methods {@link #doSetUp()} and {@link #doTearDown()} internally. <br/>
 * Both methods {@link #doSetUp()} and {@link #doTearDown()} are left empty here. If some default behaviour is desired
 * during test setup or teardown these methods should be overridden by the subclass. <br/>
 * Implementations <b>must</b> call the super implementation. This call should always happen at the beginning of the
 * implementation. This helps to avoid confusion of call orders. <br/>
 * <br/>
 * The following listing should clarify the intention:
 *
 * <pre>
 * public class MyTest extends BaseTest {
 *
 *   &#64;Override
 *   protected void doSetUp() {
 *
 *     super.doSetUp();
 *     // ... my code
 *   }
 * }
 * </pre>
 *
 * Additional value is provided by {@link #isInitialSetup()} that may be helpful for specific implementations of
 * {@link #doSetUp()} where you want to do some cleanup only once for the test-class rather than for every test method.
 * Unlike {@link BeforeAll} this can be used in non-static method code so you have access to injected dependencies.
 */
public abstract class BaseTest extends Assertions {

  private static boolean initialSetup;

  /**
   * Initializes this test class and resets {@link #isInitialSetup() initial setup flag}.
   */
  @BeforeAll
  public static void setUpClass() {

    initialSetup = true;
  }

  /**
   * Suggests to use {@link #doSetUp()} method before each tests.
   */
  @BeforeEach
  public final void setUp() {

    // Simply sets INITIALIZED to true when setUp is called for the first time.
    doSetUp();
    if (initialSetup) {
      initialSetup = false;
    }
  }

  /**
   * Suggests to use {@link #doTearDown()} method before each tests.
   */
  @AfterEach
  public final void tearDown() {

    doTearDown();
  }

  /**
   * @return {@code true} if this JUnit class is invoked for the first time (first test method is called), {@code false}
   *         otherwise (if this is a subsequent invocation).
   */
  protected boolean isInitialSetup() {

    return initialSetup;
  }

  /**
   * Provides initialization previous to the creation of the text fixture.
   */
  protected void doSetUp() {

  }

  /**
   * Provides clean up after tests.
   */
  protected void doTearDown() {

  }
}