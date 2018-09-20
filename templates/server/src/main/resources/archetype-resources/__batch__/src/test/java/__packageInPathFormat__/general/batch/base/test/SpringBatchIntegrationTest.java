package ${package}.general.batch.base.test;

import javax.inject.Inject;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;

import ${package}.general.common.base.test.TestUtil;
import com.devonfw.module.test.common.base.ComponentTest;

/**
 * Base class for all spring batch integration tests. It helps to do End-to-End job tests.
 */
public abstract class SpringBatchIntegrationTest extends ComponentTest {

  @Inject
  private JobLauncher jobLauncher;

  @Inject
  private Flyway flyway;

  @Override
  protected void doSetUp() {

    super.doSetUp();
    this.flyway.clean();
    this.flyway.migrate();
  }

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
