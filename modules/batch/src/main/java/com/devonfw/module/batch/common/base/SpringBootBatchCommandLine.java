package com.devonfw.module.batch.common.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.CommandLineJobRunner;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

/**
 * Launcher for launching batch jobs from the command line when Spring Boot is used. Similar to the
 * {@link CommandLineJobRunner}, which does not work very well with Spring Boot.
 * <p>
 * Do not use this class if Spring Boot is not used!
 * <p>
 * It expects the full class name of the Spring Boot configuration class to be used as first argument, the class/XML
 * file for configuring the job as second argument and the job name as third.<br>
 * Moreover parameters can be specified as further arguments (convention: key1=value1 key2=value2 ...).
 * <p>
 * Example:<br>
 * java com.devonfw.module.batch.common.base.SpringBootBatchCommandLine com.devonfw.gastronomy.restaurant.SpringBootBatchApp
 * classpath:config/app/batch/beans-productimport.xml productImportJob drinks.file=file:import/drinks.csv
 * date(date)=2015/12/20
 * <p>
 * For stopping all running executions of a job, use the -stop option.
 * <p>
 * Example:<br>
 * java com.devonfw.module.batch.common.base.SpringBootBatchCommandLine com.devonfw.gastronomy.restaurant.SpringBootBatchApp
 * classpath:config/app/batch/beans-productimport.xml productImportJob -stop
 *
 *
 */
public class SpringBootBatchCommandLine {

  private static final Logger LOG = LoggerFactory.getLogger(SpringBootBatchCommandLine.class);

  private ResourceLoader resourceLoader = new DefaultResourceLoader();

  public static enum Operation {
    START, STOP
  };

  private JobLauncher launcher;

  private JobLocator locator;

  private JobParametersConverter parametersConverter;

  private JobOperator operator;

  public static void main(String[] args) throws Exception {

    if (args.length < 3) {

      handleIncorrectParameters();
      return;
    }

    List<String> configurations = new ArrayList<>(2);
    configurations.add(args[0]);
    configurations.add(args[1]);

    List<String> parameters = new ArrayList<>();

    Operation op = Operation.START;
    if (args.length > 3 && args[3].equalsIgnoreCase("-stop")) {

      if (args.length > 4) {

        handleIncorrectParameters();
        return;
      }

      op = Operation.STOP;
    } else {

      for (int i = 3; i < args.length; i++) {

        parameters.add(args[i]);
      }
    }

    new SpringBootBatchCommandLine().execute(op, configurations, args[2], parameters);
  }

  private static void handleIncorrectParameters() {

    LOG.error("Incorrect parameters.");
    LOG.info("Usage:");
    LOG.info("java com.devonfw.module.batch.common.base.SpringBootBatchCommandLine"
        + " <SpringBootConfiguration> <BatchJobConfiguration>" + " <JobName> param1=value1 param2=value2 ...");
    LOG.info("For stopping all running executions of a batch job:");
    LOG.info("java com.devonfw.module.batch.common.base.BatchCommandLine"
        + " <SpringBootConfiguration> <BatchJobConfiguration>" + " <JobName> -stop");
    LOG.info("Example:");
    LOG.info("java com.devonfw.module.batch.common.base.SpringBootBatchCommandLine"
        + " com.devonfw.gastronomy.restaurant.SpringBootBatchApp" + " classpath:config/app/batch/beans-productimport.xml"
        + " productImportJob drinks.file=file:import/drinks.csv" + " date(date)=2015/12/20");
  }

  protected int getReturnCode(JobExecution jobExecution) {

    if (jobExecution.getStatus() != null && jobExecution.getStatus() == BatchStatus.COMPLETED)
      return 0;
    else
      return 1;
  }

  private Object getConfiguration(String stringRepresentation) {

    // try to load a source of Spring bean definitions:
    // 1. try to load it as a (JavaConfig) class
    // 2. if that fails: try to load it as XML resource

    try {

      return Class.forName(stringRepresentation);
    } catch (ClassNotFoundException e) {

      return this.resourceLoader.getResource(stringRepresentation);
    }
  }

  private void findBeans(ConfigurableApplicationContext ctx) {

    this.launcher = ctx.getBean(JobLauncher.class);
    this.locator = ctx.getBean(JobLocator.class); // supertype of JobRegistry
    this.operator = ctx.getBean(JobOperator.class);
    try {

      this.parametersConverter = ctx.getBean(JobParametersConverter.class);
    } catch (NoSuchBeanDefinitionException e) {

      this.parametersConverter = new DefaultJobParametersConverter();
    }
  }

  /**
   * Initialize the application context and execute the operation.
   * <p>
   * The application context is closed after the operation has finished.
   *
   * @param operation The operation to start.
   * @param configurations The sources of bean configurations (either JavaConfig classes or XML files).
   * @param jobName The name of the job to launch/stop.
   * @param parameters The parameters (key=value).
   * @throws Exception
   */
  public void execute(Operation operation, List<String> configurations, String jobName, List<String> parameters)
      throws Exception {

    // get sources of configuration
    Class<?>[] configurationClasses = new Class[configurations.size()];
    for (int i = 0; i < configurations.size(); i++) {

      configurationClasses[i] = Class.forName(configurations.get(i));
    }

    SpringApplication app = new SpringApplication(configurationClasses);

    // no (web) server needed
    app.setWebEnvironment(false);

    // start the application
    ConfigurableApplicationContext ctx = app.run(new String[0]);

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

      findBeans(ctx);

      JobParameters params = this.parametersConverter
          .getJobParameters(StringUtils.splitArrayElementsIntoProperties(parameters.toArray(new String[] {}), "="));

      // execute the batch
      // the JobOperator would require special logic for a restart, so we
      // are using the JobLauncher directly here
      jobExecution = this.launcher.run(this.locator.getJob(jobName), params);

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

      findBeans(ctx);

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
