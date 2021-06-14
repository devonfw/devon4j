package ${package}.general.common.impl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;#if($dbMigration=='liquibase')
import liquibase.Liquibase;
import liquibase.resource.ResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.database.DatabaseFactory;#end
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;
import com.devonfw.module.test.common.base.clean.TestCleaner;
import com.devonfw.module.test.common.base.clean.TestCleanerImpl;
import com.devonfw.module.test.common.base.clean.TestCleanerPlugin;#if($dbMigration=='liquibase')
import com.devonfw.module.test.common.base.clean.TestCleanerPluginLiquibase;#else
import com.devonfw.module.test.common.base.clean.TestCleanerPluginFlyway;#end

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

  #if($dbMigration=='liquibase')

  /**
     * @return the {@link TestCleanerPluginLiquibase}.
     */
    @Bean
    public TestCleanerPlugin testCleanerPluginLiquibase() {

      return new TestCleanerPluginLiquibase();
    }

  @Value("${spring.datasource.url}")
  private String spring_datasource_url;

  @Value("${spring.datasource.password}")
  private String spring_datasource_password;

  @Value("${spring.datasource.username}")
  private String spring_datasource_username;

  @Value("${spring.liquibase.change-log}")
  private String spring_liquibase_change_log;

  @Bean
  public Liquibase liquibase() throws DatabaseException {

    ResourceAccessor resourceAccessor = new FileSystemResourceAccessor();
    Database database = DatabaseFactory.getInstance().openDatabase(this.spring_datasource_url,
        this.spring_datasource_username, this.spring_datasource_password, null, null, null, null,
        resourceAccessor);
    Liquibase liquibase = new Liquibase(this.spring_liquibase_change_log, resourceAccessor, database);

    return liquibase;
  }

  #else

  /**
     * @return the {@link TestCleanerPluginFlyway}.
     */
    @Bean
    public TestCleanerPlugin testCleanerPluginFlyway() {

      return new TestCleanerPluginFlyway();
    }#end
}