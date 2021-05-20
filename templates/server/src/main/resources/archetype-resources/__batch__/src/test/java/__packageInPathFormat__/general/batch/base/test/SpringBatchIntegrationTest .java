package ${package}.general.batch.base.test;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;
import ${package}.general.common.base.test.TestUtil;
import com.devonfw.module.test.common.base.ComponentTest;
#if ($dbMigration == 'liquibase')  
  import liquibase.Liquibase;
#else 
 import org.flywaydb.core.Flyway;
#end

/**
 * Base class for all spring batch integration tests. It helps to do End-to-End job tests.
 */
public abstract class SpringBatchIntegrationTest extends ComponentTest {

  @Inject
  private JobLauncher jobLauncher;

  #if ($dbMigration == 'liquibase')  
    @Inject
    private Liquibase liquibase;
  #else 
    @Inject
    private Flyway flyway;
  #end
  
  #if ($dbMigration == 'liquibase')  
    @Override
    protected void doSetUp() {
      super.doSetUp();
      try {
        this.liquibase.dropAll();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  #else 
    @Override
    protected void doSetUp() {

      super.doSetUp();
      this.flyway.clean();
      this.flyway.migrate();
    }
  #end
  

  @Override
  protected void doTearDown() {

    super.doTearDown();
    TestUtil.logout();
  }

  /**
   * @param job job to configure
   * @return jobLauncherTestUtils
   */
  public JobLauncherTestUtils getJobLauncherTestUtils(Job job) {

    JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
    jobLauncherTestUtils.setJob(job);
    jobLauncherTestUtils.setJobLauncher(this.jobLauncher);

    return jobLauncherTestUtils;
  }
}