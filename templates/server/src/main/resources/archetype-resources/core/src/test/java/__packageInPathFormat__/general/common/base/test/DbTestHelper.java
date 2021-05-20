package ${package}.general.common.base.test;

import javax.inject.Named;
import com.devonfw.module.test.common.base.clean.TestCleanerPlugin;
import com.devonfw.module.test.common.base.clean.TestCleanerPluginLiquibase;
import com.devonfw.module.test.common.base.clean.TestCleanerPluginFlyway;
/**
 * This class provides methods for handling the database during testing where resets (and other operations) may be
 * necessary.
 */
@Named
public class DbTestHelper{

  
  private TestCleanerPlugin testCleanerPlugin;

  public DbTestHelper(TestCleanerPlugin testCleanerPlugin) {

    super();
    this.testCleanerPlugin = testCleanerPlugin;
  }

  /**
   * Drops the whole database.
   */
  public void dropDatabase() {
#if($dbMigration == 'liquibase')
      testCleanerPlugin = new TestCleanerPluginLiquibase();
#end 
#if($dbMigration == 'flyway')
      testCleanerPlugin = new TestCleanerPluginFlyway();
#end
      testCleanerPlugin.cleanup();
  }

}