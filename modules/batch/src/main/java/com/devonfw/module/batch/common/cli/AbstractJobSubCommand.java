package com.devonfw.module.batch.common.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.repository.JobRepository;

abstract class AbstractJobSubCommand {

  @Inject
  protected JobRepository jobRepository;

  @Inject
  private JobExplorer jobExplorer;

  private Long getLongIdentifier(String jobIdentifier) {

    try {
      return new Long(jobIdentifier);
    } catch (NumberFormatException e) {
      // Not an ID - must be a name
      return null;
    }
  }

  /**
   * @param jobIdentifier a job execution id or job name
   * @param minStatus the highest status to exclude from the result
   * @return
   */
  private List<JobExecution> getJobExecutionsWithStatusGreaterThan(String jobIdentifier, BatchStatus minStatus) {

    Long executionId = getLongIdentifier(jobIdentifier);
    if (executionId != null) {
      JobExecution jobExecution = this.jobExplorer.getJobExecution(executionId);
      if (jobExecution.getStatus().isGreaterThan(minStatus)) {
        return Arrays.asList(jobExecution);
      }
      return Collections.emptyList();
    }

    int start = 0;
    int count = 100;
    List<JobExecution> executions = new ArrayList<JobExecution>();
    List<JobInstance> lastInstances = this.jobExplorer.getJobInstances(jobIdentifier, start, count);

    while (!lastInstances.isEmpty()) {

      for (JobInstance jobInstance : lastInstances) {
        List<JobExecution> jobExecutions = this.jobExplorer.getJobExecutions(jobInstance);
        if (jobExecutions == null || jobExecutions.isEmpty()) {
          continue;
        }
        for (JobExecution jobExecution : jobExecutions) {
          if (jobExecution.getStatus().isGreaterThan(minStatus)) {
            executions.add(jobExecution);
          }
        }
      }

      start += count;
      lastInstances = this.jobExplorer.getJobInstances(jobIdentifier, start, count);

    }

    return executions;

  }

  protected List<JobExecution> getStoppedJobExecutions(String jobIdentifier) {

    List<JobExecution> jobExecutions = getJobExecutionsWithStatusGreaterThan(jobIdentifier, BatchStatus.STARTED);
    if (jobExecutions.isEmpty()) {
      return null;
    }
    List<JobExecution> result = new ArrayList<JobExecution>();
    for (JobExecution jobExecution : jobExecutions) {
      if (jobExecution.getStatus() != BatchStatus.ABANDONED) {
        result.add(jobExecution);
      }
    }
    return result.isEmpty() ? null : result;
  }

  protected List<JobExecution> getRunningJobExecutions(String jobIdentifier) {

    List<JobExecution> jobExecutions = getJobExecutionsWithStatusGreaterThan(jobIdentifier, BatchStatus.COMPLETED);
    if (jobExecutions.isEmpty()) {
      return null;
    }
    List<JobExecution> result = new ArrayList<JobExecution>();
    for (JobExecution jobExecution : jobExecutions) {
      if (jobExecution.isRunning()) {
        result.add(jobExecution);
      }
    }
    return result.isEmpty() ? null : result;
  }
}