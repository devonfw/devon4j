package com.devonfw.module.batch.common.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.support.CommandLineJobRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

/**
 * Launcher for launching batch jobs from the command line when Spring Boot is used. It is somewhat similar to the
 * {@link CommandLineJobRunner}. The main difference is, that this launcher disables the web-app for the spring context.
 *
 * It expects the full class name of the Spring Boot configuration class to be used as first argument and the jobs
 * beanname as the second.<br>
 * Moreover parameters can be specified as further arguments (convention: key1=value1 key2=value2 ...).
 * <p>
 * Example:<br>
 * {@code java -jar my-app-batch-bootified.jar com.devonfw.application.example.SpringBootApp myJob param=value...}
 * </p>
 * <p>
 * For stopping all running executions of a job, use the -stop option.
 *
 * Example:<br>
 * {@code  java -jar my-app-batch-bootified.jar com.devonfw.application.example.SpringBootApp myJob -stop}
 * </p>
 * <p>
 * To make that work expect that the batchs is deployed in form of a "bootified" jar, whith this class here als the
 * start-class. For that you have to add the following snipped to your pom.xml:
 *
 * <pre>
 * {@code
 <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <configuration>
          <excludes>
            <exclude>config/application.properties</exclude>
          </excludes>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <configuration>
          <mainClass>com.devonfw.module.batch.common.base.SpringBootBatchCommandLine</mainClass>
          <classifier>bootified</classifier>
        </configuration>
        <executions>
          <execution>
            <goals>
              <goal>repackage</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
 * }
 * </pre>
 * </p>
 * <p>
 */
public class SpringBootBatchCommandLine {

  private static final Logger LOG = LoggerFactory.getLogger(SpringBootBatchCommandLine.class);

  /**
   * Available operations
   */
  public static enum Operation {
    /** Start the batch. */
    START,
    /** Stop the batch. */
    STOP
  };

  @Inject
  private JobLauncher launcher;

  @Inject
  private JobLocator locator;

  @Autowired(required = false) // Use Autowired here, since @Inject does not support required = false.
  private JobParametersConverter parametersConverter = new DefaultJobParametersConverter();;

  @Inject
  private JobOperator operator;

  /**
   * @param args the command-line arguments.
   * @throws Exception in case of an error.
   */
  public static void main(String[] args) throws Exception {

    if (args.length < 2) {

      handleIncorrectParameters();
      return;
    }

    List<String> parameters = new ArrayList<>();

    Operation op = Operation.START;
    if (args.length > 2 && args[2].equalsIgnoreCase("-stop")) {

      if (args.length > 3) {

        handleIncorrectParameters();
        return;
      }

      op = Operation.STOP;
    } else {

      for (int i = 2; i < args.length; i++) {

        parameters.add(args[i]);
      }
    }

    new SpringBootBatchCommandLine().execute(op, args[0], args[1], parameters);
  }

  private static void handleIncorrectParameters() {

    LOG.error("Incorrect parameters.");
    LOG.info("Usage:");
    LOG.info(
        "java -jar my-app-batch-bootified.jar <SpringBootConfiguration> <JobName> param1=value1 param2=value2 ...");
    LOG.info("For stopping all running executions of a batch job:");
    LOG.info("java -jar my-app-batch-bootified.jar <SpringBootConfiguration> <JobName> -stop");
    LOG.info("Example:");
    LOG.info("java com.devonfw.application.example.SpringBootApp exportJob pathToFile=myfile.csv");
  }

  /**
   * @param jobExecution the {@link JobExecution}.
   * @return the corresponding {@link System#exit(int) exit code}.
   */
  protected int getReturnCode(JobExecution jobExecution) {

    if (jobExecution.getStatus() != null && jobExecution.getStatus() == BatchStatus.COMPLETED)
      return 0;
    else
      return 1;
  }

  /**
   * Initialize the application context and execute the operation.
   * <p>
   * The application context is closed after the operation has finished.
   *
   * @param operation The operation to start.
   * @param configuration The sources of app context configuration.
   * @param jobName The name of the job to launch/stop.
   * @param parameters The parameters (key=value).
   * @throws Exception in case of an error.
   */
  public void execute(Operation operation, String configuration, String jobName, List<String> parameters)
      throws Exception {

    SpringApplication app = new SpringApplication(Class.forName(configuration));

    // no (web) server needed
    app.setWebApplicationType(WebApplicationType.NONE);

    // start the application
    ConfigurableApplicationContext ctx = app.run(new String[0]);

    // start injection for properties of this class (here), by manually invoking autowiring for the new context.
    ctx.getAutowireCapableBeanFactory().autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE,
        false);

    switch (operation) {
      case START:
        startBatch(ctx, jobName, parameters);
        break;
      case STOP:
        stopBatch(ctx, jobName);
        break;
      default:
        throw new RuntimeException("Unknown operation: " + operation);
    }

  }

  private void startBatch(ConfigurableApplicationContext ctx, String jobName, List<String> parameters)
      throws Exception {

    JobExecution jobExecution = null;
    try {

      JobParameters params = this.parametersConverter
          .getJobParameters(StringUtils.splitArrayElementsIntoProperties(parameters.toArray(new String[] {}), "="));

      // execute the batch
      Job job = null;
      if (this.locator != null) {
        try {
          job = this.locator.getJob(jobName);
        } catch (NoSuchJobException e) {
        }
      }
      if (job == null) {
        job = (Job) ctx.getBean(jobName);
      }

      jobExecution = this.launcher.run(job, params);

    } finally {

      // evaluate the outcome
      final int returnCode = (jobExecution == null) ? 1 : getReturnCode(jobExecution);
      if (jobExecution == null) {

        LOG.error("Batch Status: Batch could not be started.");
      } else {

        LOG.info("Batch start time: {}", jobExecution.getStartTime() == null ? "null" : jobExecution.getStartTime());
        LOG.info("Batch end time: {}", jobExecution.getEndTime() == null ? "null" : jobExecution.getEndTime());

        if (returnCode == 0) {

          LOG.info("Batch Status: {}", jobExecution.getStatus() == null ? "null" : jobExecution.getStatus());
        } else {

          LOG.error("Batch Status: {}", jobExecution.getStatus() == null ? "null" : jobExecution.getStatus());
        }
      }
      LOG.info("Return Code: {}", returnCode);

      SpringApplication.exit(ctx, new ExitCodeGenerator() {

        @Override
        public int getExitCode() {

          return returnCode;
        }
      });
    }
  }

  private void stopBatch(ConfigurableApplicationContext ctx, String jobName) throws Exception {

    int returnCode = 0;
    try {

      Set<Long> runningJobExecutionIDs = this.operator.getRunningExecutions(jobName);
      if (runningJobExecutionIDs.isEmpty()) {

        throw new JobExecutionNotRunningException("Batch job " + jobName + " is currently not being executed.");
      }

      LOG.debug("Found {} executions to be stopped (potentially" + " already in state stopping).",
          runningJobExecutionIDs.size());

      int stoppedCount = 0;
      for (Long id : runningJobExecutionIDs) {

        try {

          this.operator.stop(id);
          stoppedCount++;
        } catch (JobExecutionNotRunningException e) {

          // might have finished at this point
          // or was in state stopping already
        }
      }

      LOG.info("Actually stopped {} batch executions.", stoppedCount);

    } catch (Exception e) {

      returnCode = 1;
      throw e;
    } finally {

      final int returnCodeResult = returnCode;
      SpringApplication.exit(ctx, new ExitCodeGenerator() {

        @Override
        public int getExitCode() {

          return returnCodeResult;
        }
      });
    }
  }
}
