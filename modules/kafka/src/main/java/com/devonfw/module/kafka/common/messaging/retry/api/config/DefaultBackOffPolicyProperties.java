package com.devonfw.module.kafka.common.messaging.retry.api.config;

/**
 * @author ravicm
 *
 */
public class DefaultBackOffPolicyProperties {

  private long retryDelay = 60000;

  private double retryDelayMultiplier = 1.0;

  private long retryMaxDelay = 60000;

  private long retryReEnqueueDelay = 1000;

  public long getRetryReEnqueueDelay() {

    return this.retryReEnqueueDelay;
  }

  public void setRetryReEnqueueDelay(long retryReEnqueueDelay) {

    this.retryReEnqueueDelay = retryReEnqueueDelay;
  }

  public long getRetryDelay() {

    return this.retryDelay;
  }

  public void setRetryDelay(long retryDelay) {

    this.retryDelay = retryDelay;
  }

  public double getRetryDelayMultiplier() {

    return this.retryDelayMultiplier;
  }

  public void setRetryDelayMultiplier(double retryDelayMultiplier) {

    this.retryDelayMultiplier = retryDelayMultiplier;
  }

  public long getRetryMaxDelay() {

    return this.retryMaxDelay;
  }

  public void setRetryMaxDelay(long retryMaxDelay) {

    this.retryMaxDelay = retryMaxDelay;
  }

}
