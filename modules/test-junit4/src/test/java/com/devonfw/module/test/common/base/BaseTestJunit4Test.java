package com.devonfw.module.test.common.base;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This test verifies the proper working of the {@link BaseTest} class.
 *
 */
public class BaseTestJunit4Test extends BaseTest {

  private static final Logger LOG = LoggerFactory.getLogger(BaseTestJunit4Test.class);

  private static boolean firstSetup;

  /**
   * Initialize this test class.
   */
  @BeforeClass
  public static void initialize() {

    firstSetup = true;
  }

  @Override
  protected void doSetUp() {

    boolean initialSetup = isInitialSetup();
    LOG.debug("{}.isInitialSetup() = {}", getClass().getSimpleName(), Boolean.valueOf(initialSetup));
    assertThat(initialSetup).isEqualTo(firstSetup);
    super.doSetUp();
    firstSetup = false;
  }

  /**
   * First test method.
   */
  @Test
  public void test1() {

    assertThat(isInitialSetup()).isFalse();
  }

  /**
   * Second test method.
   */
  @Test
  public void test2() {

    assertThat(isInitialSetup()).isFalse();
  }

}
