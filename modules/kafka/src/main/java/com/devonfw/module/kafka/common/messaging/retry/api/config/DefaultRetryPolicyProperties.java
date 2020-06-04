package com.devonfw.module.kafka.common.messaging.retry.api.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.devonfw.module.kafka.common.messaging.retry.impl.DefaultRetryPolicy;

/**
 * This is a property class for {@link DefaultRetryPolicy}.
 *
 */
public class DefaultRetryPolicyProperties {

  private Map<String, Long> retryPeriod = new HashMap<>();

  private Map<String, Set<String>> retryableExceptions = new HashMap<>();

  private Map<String, Boolean> retryableExceptionsTraverseCauses = new HashMap<>();

  private Map<String, Long> retryCount = new HashMap<>();

  private long retryPeriodDefault = 1800;

  private boolean retryableExceptionsTraverseCausesDefault = false;

  private long retryCountDefault = 0;

  /**
   * The retry period
   *
   * @return long
   */
  public long getRetryPeriodDefault() {

    return this.retryPeriodDefault;
  }

  /**
   * Returns a boolean value to traverse the retry exceptions.
   *
   * @return boolean
   */
  public boolean isRetryableExceptionsTraverseCausesDefault() {

    return this.retryableExceptionsTraverseCausesDefault;
  }

  /**
   * The number of times to execute retry.
   *
   * @return retryCount
   */
  public long getRetryCountDefault() {

    return this.retryCountDefault;
  }

  /**
   * @return retryPeriod
   */
  public Map<String, Long> getRetryPeriod() {

    return this.retryPeriod;
  }

  /**
   * @param retryPeriod new value of {@link #getRetryPeriod}.
   */
  public void setRetryPeriod(Map<String, Long> retryPeriod) {

    this.retryPeriod = retryPeriod;
  }

  /**
   * @return retryableExceptions
   */
  public Map<String, Set<String>> getRetryableExceptions() {

    return this.retryableExceptions;
  }

  /**
   * @param retryableExceptions new value of {@link #getRetryableExceptions}.
   */
  public void setRetryableExceptions(Map<String, Set<String>> retryableExceptions) {

    this.retryableExceptions = retryableExceptions;
  }

  /**
   * @return retryableExceptionsTraverseCauses
   */
  public Map<String, Boolean> getRetryableExceptionsTraverseCauses() {

    return this.retryableExceptionsTraverseCauses;
  }

  /**
   * @param retryableExceptionsTraverseCauses new value of {@link #getRetryableExceptionsTraverseCauses}.
   */
  public void setRetryableExceptionsTraverseCauses(Map<String, Boolean> retryableExceptionsTraverseCauses) {

    this.retryableExceptionsTraverseCauses = retryableExceptionsTraverseCauses;
  }

  /**
   * @return retryCount
   */
  public Map<String, Long> getRetryCount() {

    return this.retryCount;
  }

  /**
   * @param retryCount new value of {@link #getRetryCount}.
   */
  public void setRetryCount(Map<String, Long> retryCount) {

    this.retryCount = retryCount;
  }

}
