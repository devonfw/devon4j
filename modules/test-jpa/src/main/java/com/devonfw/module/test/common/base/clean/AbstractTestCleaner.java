package com.devonfw.module.test.common.base.clean;

/**
 * Interface providing ability to {@link #cleanup() cleanup}.
 */
public abstract interface AbstractTestCleaner {

  /**
   * Performs a cleanup of contextual data. E.g. it may rest the database so according side-effects from previous tests
   * are eliminated.
   */
  void cleanup();

}
