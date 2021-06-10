package ${package}.general.batch.base.test;

import javax.inject.Inject;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;
import ${package}.general.common.base.test.TestUtil;
import com.devonfw.module.test.common.base.ComponentTest;
import com.devonfw.module.test.common.base.DbTest;
import com.devonfw.module.test.common.base.clean.TestCleanerPlugin;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Base class for all spring batch integration tests. It helps to do End-to-End job tests.
 */
public abstract class SpringBatchIntegrationTest extends ComponentTest {

  @Inject
  private JobLauncher jobLauncher;
  
  @Inject
  @Qualifier("testCleanerPlugin$dbMigration")
  private TestCleanerPlugin testCleanerPlugin;
  
  @Override
  protected void doSetUp() {
  super.doSetUp();
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