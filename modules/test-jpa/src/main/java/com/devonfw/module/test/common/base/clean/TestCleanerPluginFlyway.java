package com.devonfw.module.test.common.base.clean;

import javax.inject.Inject;

import org.flywaydb.core.Flyway;

/**
 * Implementation of {@link TestCleanerPlugin} base on {@link Flyway}. It will {@link Flyway#clean() clean} and
 * {@link Flyway#migrate() migrate} on {@link #cleanup()}. Therefore after {@link #cleanup()} only DDL and master-data
 * will be left in the database.
 */
public class TestCleanerPluginFlyway implements TestCleanerPlugin {

  @Inject
  private Flyway flyway;

  /**
   * The constructor.
   */
  public TestCleanerPluginFlyway() {

    super();
  }

  /**
   * The constructor.
   *
   * @param flyway the {@link Flyway} instance.
   */
  public TestCleanerPluginFlyway(Flyway flyway) {

    super();
    this.flyway = flyway;
  }

  @Override
  public void cleanup() throws Exception {

    this.flyway.clean();
    this.flyway.migrate();
  }

}
