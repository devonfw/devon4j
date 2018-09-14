package ${package}.general.common.base.test;

import javax.inject.Named;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;

/**
 * This class provides methods for handling the database during testing where resets (and other operations) may be
 * necessary.
 */
@Named
public class DbTestHelper {

  private Flyway flyway;

  private MigrationVersion migrationVersion;

  /**
   * The constructor.
   *
   * @param flyway an instance of type {@link Flyway}.
   */
  public DbTestHelper(Flyway flyway) {
    super();
    this.flyway = flyway;
  }

  /**
   * Drops the whole database.
   */
  public void dropDatabase() {

    this.flyway.clean();
  }

  /**
   * Calls {@link #dropDatabase()} internally, and migrates to the highest available migration (default) or to the
   * {@code migrationVersion} specified by {@link #setMigrationVersion(String)}.
   */
  public void resetDatabase() {

    dropDatabase();
    if (this.migrationVersion != null) {
      this.flyway.setTarget(this.migrationVersion);
    }
    this.flyway.migrate();
  }

  /**
   * This method sets the internal value of the {@code migrationVersion}.
   *
   * @param migrationVersion new {@code String} value of {@code migrationVersion}. Must not be null
   */
  public void setMigrationVersion(String migrationVersion) {

    this.migrationVersion = MigrationVersion.fromVersion(migrationVersion);
  }

}
