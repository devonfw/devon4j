package com.devonfw.module.kafka.common.messaging.retry.api.config;

import java.util.Set;

import com.devonfw.module.kafka.common.messaging.retry.impl.DefaultRetryPolicy;

/**
 * This is a property class for {@link DefaultRetryPolicy}.
 *
 */
public class DefaultRetryPolicyProperties {

  private long retryPeriod = 1800;

  private Set<String> retryableExceptions;

  private boolean retryableExceptionsTraverseCauses = false;

  /**
   * The retry period
   *
   * @return long
   */
  public long getRetryPeriod() {

    return this.retryPeriod;
  }

  /**
   * Set the retry period for {@link #getRetryPeriod()}. By default 1000.
   *
   * @param retryPeriod the retry period.
   */
  public void setRetryPeriod(long retryPeriod) {

    this.retryPeriod = retryPeriod;
  }

  /**
   * The retry exceptions to be handled.
   *
   * @return {@link Set}
   */
  public Set<String> getRetryableExceptions() {

    return this.retryableExceptions;
  }

  /**
   * Set the retry exceptions for {@link #getRetryableExceptions()}.
   *
   * @param retryableExceptions the retry exceptions.
   */
  public void setRetryableExceptions(Set<String> retryableExceptions) {

    this.retryableExceptions = retryableExceptions;
  }

  /**
   * Returns a boolean value to traverse the retry exceptions.
   *
   * @return boolean
   */
  public boolean isRetryableExceptionsTraverseCauses() {

    return this.retryableExceptionsTraverseCauses;
  }

  /**
   *
   * Set the boolean value for {@link #isRetryableExceptionsTraverseCauses()}
   *
   * @param retryableExceptionsTraverseCauses boolean value.
   */
  public void setRetryableExceptionsTraverseCauses(boolean retryableExceptionsTraverseCauses) {

    this.retryableExceptionsTraverseCauses = retryableExceptionsTraverseCauses;
  }

}
