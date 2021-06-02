package ${package}.general.batch.base.test;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;
import ${package}.general.common.base.test.TestUtil;
import com.devonfw.module.test.common.base.ComponentTest;
import com.devonfw.module.test.common.base.clean.TestCleanerPlugin;
import com.devonfw.module.test.common.base.clean.TestCleanerPluginFlyway;
/**
 * Base class for all spring batch integration tests. It helps to do End-to-End job tests.
 */
public abstract class SpringBatchIntegrationTest extends ComponentTest {

  @Inject
  private JobLauncher jobLauncher;

  @Inject
  private TestCleanerPlugin testCleanerPlugin;

  @Override
  protected void doSetUp() {
  super.doSetUp();
#if($dbMigration == 'flyway')
   testCleanerPlugin = new TestCleanerPluginFlyway();
#else if($dbMigration == 'liquibase')
   testCleanerPlugin = new TestCleanerPluginLiquibase();
#end
   testCleanerPlugin.cleanup();
  }

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