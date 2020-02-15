package com.devonfw.module.kafka.common.messaging.retry.api.config;

import java.util.Set;

/**
 * @author ravicm
 *
 */
public class DefaultRetryPolicyProperties {

  private long retryPeriod = 1800;

  private Set<String> retryableExceptions;

  private boolean retryableExceptionsTraverseCauses = false;

  public long getRetryPeriod() {

    return this.retryPeriod;
  }

  public void setRetryPeriod(long retryPeriod) {

    this.retryPeriod = retryPeriod;
  }

  public Set<String> getRetryableExceptions() {

    return this.retryableExceptions;
  }

  public void setRetryableExceptions(Set<String> retryableExceptions) {

    this.retryableExceptions = retryableExceptions;
  }

  public boolean isRetryableExceptionsTraverseCauses() {

    return this.retryableExceptionsTraverseCauses;
  }

  public void setRetryableExceptionsTraverseCauses(boolean retryableExceptionsTraverseCauses) {

    this.retryableExceptionsTraverseCauses = retryableExceptionsTraverseCauses;
  }

}
