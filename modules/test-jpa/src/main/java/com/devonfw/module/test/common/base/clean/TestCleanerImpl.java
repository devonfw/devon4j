package com.devonfw.module.test.common.base.clean;

import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of {@link TestCleaner}. Simply executes all {@link TestCleanerPlugin}s on {@link #cleanup()}.
 */
public class TestCleanerImpl implements TestCleaner {

  private List<TestCleanerPlugin> plugins;

  /**
   * @param plugins the {@link List} of {@link TestCleanerPlugin}s to {@link Inject}.
   */
  @Inject
  public void setPlugins(List<TestCleanerPlugin> plugins) {

    this.plugins = plugins;
  }

  @Override
  public void cleanup() throws Exception {

    for (TestCleanerPlugin plugin : this.plugins) {
      plugin.cleanup();
    }
  }

}
