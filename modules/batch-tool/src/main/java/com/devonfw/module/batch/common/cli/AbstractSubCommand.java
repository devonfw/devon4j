package com.devonfw.module.batch.common.cli;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobRepository;

/**
 * This class provides some basic functionality for commands.
 *
 */
abstract class AbstractSubCommand {

  @Inject
  protected JobRepository jobRepository;

  @Inject
  private JobExplorer jobExplorer;

  /**
   * Find all executions for the job identified by jobIdentifier.
   *
   * @param jobIdentifier The name of the job
   * @return Returns a list of all executions for the given job.
   * @throws NoSuchJobException If the job does not exists.
   */
  protected List<JobExecution> findAllExecutions(String jobIdentifier) throws NoSuchJobException {

    List<JobExecution> jobExecutions = new ArrayList<>();
    for (JobInstance jobInstance : this.jobExplorer.findJobInstancesByJobName(jobIdentifier, 0,
        this.jobExplorer.getJobInstanceCount(jobIdentifier))) {
      jobExecutions.addAll(this.jobExplorer.getJobExecutions(jobInstance));
    }
    return jobExecutions;
  }

  /**
   * Find all executions for the job identified by jobIdentifier which match the given status.
   *
   * @param jobIdentifier The name of the job
   * @param batchStatus The status to look for
   * @return Returns a list of all executions for the given job with the given status.
   * @throws NoSuchJobException If the job does not exists.
   */
  protected List<JobExecution> findJobExecutionsWithStatus(String jobIdentifier, BatchStatus batchStatus)
      throws NoSuchJobException {

    List<JobExecution> result = new ArrayList<>();
    for (JobExecution jobExecution : findAllExecutions(jobIdentifier)) {
      if (jobExecution.getStatus() != batchStatus) {
        result.add(jobExecution);
      }
    }
    return result.isEmpty() ? null : result;
  }

}