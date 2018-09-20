package com.devonfw.module.batch.common.impl;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;

/**
 * {@link JobLauncher} that extends the functionality provided by the standard {@link SimpleJobLauncher}:
 * <p>
 * For batches, which always restart from scratch (i.e. those marked with restartable="false"), the parameter's are
 * 'incremented' automatically using the {@link JobParametersIncrementer} from the job (usually by adding or modifying
 * the 'run.id' parameter). It is actually just a convenience functionality so that the one starting batches does not
 * have to change the parameters manually.
 *
 */
public class JobLauncherWithAdditionalRestartCapabilities extends SimpleJobLauncher {

  private JobRepository jobRepository;

  @Override
  public JobExecution run(final Job job, JobParameters jobParameters) throws JobExecutionAlreadyRunningException,
      JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {

    if (!job.isRestartable()) {

      JobParameters originalParameters = jobParameters;
      while (this.jobRepository.isJobInstanceExists(job.getName(), jobParameters)) {

        // check if batch job is still running or was completed already
        // analogous to SimpleJobRepository#createJobExecution
        JobExecution jobExecution = this.jobRepository.getLastJobExecution(job.getName(), jobParameters);
        if (jobExecution.isRunning()) {
          throw new JobExecutionAlreadyRunningException("A job execution for this job is already running: "
              + jobExecution.getJobInstance());
        }
        BatchStatus status = jobExecution.getStatus();
        if (status == BatchStatus.COMPLETED || status == BatchStatus.ABANDONED) {
          throw new JobInstanceAlreadyCompleteException("A job instance already exists and is complete for parameters="
              + originalParameters + ".  If you want to run this job again, change the parameters.");
        }

        // if there is a NullPointerException executing the following statement
        // there has not been a JobParametersIncrementer set for the job
        jobParameters = job.getJobParametersIncrementer().getNext(jobParameters);
      }
    }

    return super.run(job, jobParameters);
  }

  @Override
  public void setJobRepository(JobRepository jobRepository) {

    super.setJobRepository(jobRepository);
    this.jobRepository = jobRepository;
  }

}
