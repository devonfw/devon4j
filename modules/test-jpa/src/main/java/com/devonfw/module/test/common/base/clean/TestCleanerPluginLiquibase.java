package com.devonfw.module.test.common.base.clean;

import javax.inject.Inject;
import liquibase.Liquibase;

/**
 * @author ayudixit
 *
 */
public class TestCleanerPluginLiquibase implements TestCleanerPlugin {

  @Inject
  private Liquibase liquibase;

  /**
   * The constructor.
   */
  public TestCleanerPluginLiquibase() {

    super();
  }

  /**
   * The constructor.
   *
   * @param liquibase the {@link Liquibase} instance.
   */
  public TestCleanerPluginLiquibase(Liquibase liquibase) {

    super();
    this.liquibase = liquibase;
  }

  @Override
  public void cleanup() {

    try {
      this.liquibase.dropAll();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
