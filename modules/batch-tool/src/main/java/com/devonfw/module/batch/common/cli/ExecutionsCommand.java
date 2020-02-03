package com.devonfw.module.batch.common.cli;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.stereotype.Component;

import com.devonfw.module.batch.common.cli.ExecutionsCommand.ListCommand;
import com.devonfw.module.batch.common.cli.ExecutionsCommand.StatusCommand;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * This class bundles subcommands which manage executions. Each subcommand is implemented in a static sub-class.
 *
 */
@Component
@Command(name = "executions", subcommands = { HelpCommand.class, ListCommand.class,
StatusCommand.class }, description = { "Manage single execution instances.",
"This is an advanced feature, normally managing your job via 'jobs' command should be sufficient.",
"Enter 'executions help SUBCOMMAND' to find out parameters for the specified subcommand." }, synopsisSubcommandLabel = "SUBCOMMAND")
public class ExecutionsCommand implements Callable<Integer> {

  @Inject
  private JobExplorer jobExplorer;

  @Override
  public Integer call() {

    // Subcommand required
    new CommandLine(this).usage(System.out);
    return -1;
  }

  @Component
  @Command(name = "status", description = "Set the status of the given execution.")
  static class StatusCommand implements Callable<Integer> {

    @Parameters(index = "0", paramLabel = "executionId", description = "ID of the execution to modify.")
    private Long executionId;

    @Parameters(index = "1", paramLabel = "status", description = "The new status you wish the execution to set to.")
    private BatchStatus status;

    @Inject
    private JobExplorer jobExplorer;

    @Inject
    private JobRepository jobRepository;

    @Override
    public Integer call() throws Exception {

      JobExecution jobExecution = this.jobExplorer.getJobExecution(this.executionId);
      if (jobExecution == null) {
        System.out.println(
            "No execution with ID " + this.executionId + " found. Use list command to find existing executions.");
        return -1;
      }
      jobExecution.setStatus(this.status);
      this.jobRepository.update(jobExecution);
      System.out.println("Done.");
      return 0;
    }

  }

  @Component
  @Command(name = "list", description = "List all executions for the job.")
  static class ListCommand implements Callable<Integer> {
    @Parameters(index = "0", paramLabel = "jobName", description = "The name of the job, which executions should managed.")
    private String jobName;

    @Option(names = "--printParams", description = "Print the parameters of the execution.")
    private boolean printParams;

    @Option(names = "--printExit", description = "Print exit status of the execution.")
    private boolean printExit;

    @Inject
    private JobExplorer jobExplorer;

    @Override
    public Integer call() throws NoSuchJobException {

      System.out.print("ID\tStart Time\tEnd Time\tStatus");
      if (this.printParams) {
        System.out.print("\tParameters");
      }
      if (this.printExit) {
        System.out.print("\tExit status");
      }
      System.out.println();
      List<JobExecution> jobExecutions = new LinkedList<>();
      for (JobInstance jobInstance : this.jobExplorer.findJobInstancesByJobName(this.jobName, 0,
          this.jobExplorer.getJobInstanceCount(this.jobName))) {
        jobExecutions.addAll(this.jobExplorer.getJobExecutions(jobInstance));
      }

      jobExecutions.sort((JobExecution e1, JobExecution e2) -> e1.getStartTime().compareTo(e2.getStartTime()));
      for (JobExecution jobExecution : jobExecutions) {
        System.out.print(jobExecution.getId());
        System.out.print("\t" + jobExecution.getStartTime());
        System.out.print("\t" + jobExecution.getEndTime());
        System.out.print("\t" + jobExecution.getStatus());
        if (this.printParams) {
          System.out.print("\t" + jobExecution.getJobParameters());
        }
        if (this.printExit) {
          System.out.print("\t" + jobExecution.getExitStatus());
        }
        System.out.println();
      }

      return 0;
    }
  }

}