package ${package}.general.common.impl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.test.common.base.clean.TestCleaner;
import com.devonfw.module.test.common.base.clean.TestCleanerImpl;
import com.devonfw.module.test.common.base.clean.TestCleanerPlugin;
import com.devonfw.module.test.common.base.clean.TestCleanerPluginFlyway;
import com.devonfw.module.test.common.base.clean.TestCleanerPluginLiquibase;

/**
 * {@link Configuration} for Database in JUnit tests.
 */
@Configuration
public class TestDbConfig {

  /**
   * @return the {@link TestCleaner}.
   */
  @Bean
  public TestCleaner testCleaner() {

    return new TestCleanerImpl();
  }

  /**
   * @return the {@link TestCleanerPluginFlyway}.
   */
  @Bean
  public TestCleanerPlugin testCleanerPlugin() {


  String dbMigrationValue = System.getenv($dbMigration);
  
  if(dbMigrationValue==null)
  {
    return new TestCleanerPluginFlyway();
    
  }else if(dbMigrationValue=="liquibase") {
    return new TestCleanerPluginLiquibase();
  }
}
}