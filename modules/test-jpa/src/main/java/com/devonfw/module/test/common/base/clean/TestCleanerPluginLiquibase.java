package com.devonfw.module.test.common.base.clean;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Qualifier;
import liquibase.Liquibase;
import liquibase.exception.DatabaseException;

/**
 * Implementation of {@link TestCleanerPlugin} base on {@link Liquibase}. It will {@link Liquibase#dropAll() clean} on
 * {@link #cleanup()}. Therefore after {@link #cleanup()} it will drop all database objects.
 */
@Qualifier("testCleanerPluginliquibase")
public class TestCleanerPluginLiquibase implements TestCleanerPlugin {

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
    } catch(DatabaseException databaseException ) {
      throw new RuntimeException(databaseException);
    }
  }

}
