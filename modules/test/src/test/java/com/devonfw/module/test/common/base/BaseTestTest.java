package com.devonfw.module.test.common.base;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;

import com.devonfw.module.test.common.base.BaseTest;

/**
 * This test verifies the proper working of the {@link BaseTest} class.
 *
 * @author jmolinar
 */
public class BaseTestTest extends Assertions {

  private static boolean EXPECTED_RESULT;

  class MyTest extends BaseTest {

    @Override
    protected void doSetUp() {

      assertThat(INITIALIZED).isEqualTo(EXPECTED_RESULT);
    }
  }

  @Test
  public void testInitialization() {

    BaseTest test = new MyTest();
    // Set the expected result before running setUp()-
    // Setup calls assertThat to test the expected result
    EXPECTED_RESULT = false;
    test.setUp();

    EXPECTED_RESULT = true;
    test.setUp();

  }

  @After
  public void tearDown() {

    // Just in case some test method will be added
    MyTest.INITIALIZED = false;
  }

}
