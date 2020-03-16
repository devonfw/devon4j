package com.devonfw.module.kafka.common.messaging.retry.api.config;

import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageBackOffPolicy;

/**
 * This is a property class to define the retry pattern for {@link MessageBackOffPolicy}
 *
 */
public class DefaultBackOffPolicyProperties {

  private long retryDelay = 60000;

  private double retryDelayMultiplier = 1.0;

  private long retryMaxDelay = 60000;

  private long retryReEnqueueDelay = 1000;

  /**
   * The retry enque delay to send again the message to kafka. By default 1000.
   *
   * @return long
   */
  public long getRetryReEnqueueDelay() {

    return this.retryReEnqueueDelay;
  }

  /**
   * Set the retry enque delay for {@link #getRetryReEnqueueDelay()}
   *
   * @param retryReEnqueueDelay the enque delay.
   */
  public void setRetryReEnqueueDelay(long retryReEnqueueDelay) {

    this.retryReEnqueueDelay = retryReEnqueueDelay;
  }

  /**
   * The retry delay.
   *
   * @return long.
   */
  public long getRetryDelay() {

    return this.retryDelay;
  }

  /**
   * Set the retry delay for {@link #getRetryDelay()}. By default 1000
   *
   * @param retryDelay the retry delay.
   */
  public void setRetryDelay(long retryDelay) {

    this.retryDelay = retryDelay;
  }

  /**
   * The retry delay multiplier
   *
   * @return double.
   */
  public double getRetryDelayMultiplier() {

    return this.retryDelayMultiplier;
  }

  /**
   * Set the retry delay multiplier for {@link #getRetryDelayMultiplier()}. By default 1.0
   *
   * @param retryDelayMultiplier the retry delay multiplier.
   */
  public void setRetryDelayMultiplier(double retryDelayMultiplier) {

    this.retryDelayMultiplier = retryDelayMultiplier;
  }

  /**
   * The retry max delay.
   *
   * @return the long.
   */
  public long getRetryMaxDelay() {

    return this.retryMaxDelay;
  }

  /**
   * Set the retry max delay for {@link #getRetryMaxDelay()}. by default 60000.
   * 
   * @param retryMaxDelay the retry max delay.
   */
  public void setRetryMaxDelay(long retryMaxDelay) {

    this.retryMaxDelay = retryMaxDelay;
  }

}
