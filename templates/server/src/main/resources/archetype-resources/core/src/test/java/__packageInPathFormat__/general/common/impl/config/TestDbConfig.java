package ${package}.general.common.impl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.test.common.base.clean.TestCleaner;
import com.devonfw.module.test.common.base.clean.TestCleanerImpl;
import com.devonfw.module.test.common.base.clean.TestCleanerPlugin;
#if ($dbMigration == 'liquibase')  
import com.devonfw.module.test.common.base.clean.TestCleanerPluginLiquibase;
#else 
import com.devonfw.module.test.common.base.clean.TestCleanerPluginFlyway;
#end

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

#if ($dbMigration == 'liquibase')  
    /**
     * @return the {@link TestCleanerPluginLiquibase}.
     */
    @Bean
    public TestCleanerPlugin testCleanerPluginLiquibase() {

      return new TestCleanerPluginLiquibase();
    }
#else 
    /**
     * @return the {@link TestCleanerPluginFlyway}.
     */
    @Bean
    public TestCleanerPlugin testCleanerPluginFlyway() {

      return new TestCleanerPluginFlyway();
    }
#end
}