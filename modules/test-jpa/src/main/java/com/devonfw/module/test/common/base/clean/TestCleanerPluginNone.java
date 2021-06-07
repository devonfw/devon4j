package com.devonfw.module.test.common.base.clean;

/**
 * Implementation of {@link TestCleanerPlugin} that simply does nothing. May be used to satisfy injections as spring
 * would throw an exception if no {@link TestCleanerPlugin} is available at all for injection into
 * {@link TestCleanerImpl}.
 */
public class TestCleanerPluginNone implements TestCleanerPlugin {

  @Override
  public void cleanup() throws Exception {

    // do nothing by design
  }

}
