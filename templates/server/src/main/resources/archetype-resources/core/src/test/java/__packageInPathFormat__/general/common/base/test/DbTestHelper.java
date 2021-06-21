package ${package}.general.common.base.test;

import javax.inject.Named;
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Qualifier;
import com.devonfw.module.test.common.base.clean.TestCleanerPlugin;

/**
 * This class provides methods for handling the database during testing where resets (and other operations) may be
 * necessary.
 */
@Named
public class DbTestHelper{
  
  @Inject
  @Qualifier("testCleanerPlugin$dbMigration")
  private TestCleanerPlugin testCleanerPlugin;

  public DbTestHelper(TestCleanerPlugin testCleanerPlugin) {

    super();
    this.testCleanerPlugin = testCleanerPlugin;
  }

  /**
   * Drops the whole database.
   */
  public void dropDatabase() {
      testCleanerPlugin.cleanup();
  }

}