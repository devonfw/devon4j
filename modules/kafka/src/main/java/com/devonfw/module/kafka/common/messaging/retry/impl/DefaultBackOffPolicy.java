package com.devonfw.module.kafka.common.messaging.retry.impl;

import java.time.Instant;

import org.springframework.util.Assert;

import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageBackOffPolicy;
import com.devonfw.module.kafka.common.messaging.retry.api.config.DefaultBackOffPolicyProperties;

/**
 * @author ravicm
 *
 */
public class DefaultBackOffPolicy implements MessageBackOffPolicy {

  private long retryReEnqueueDelay;

  private long retryDelay;

  private double retryDelayMultiplier;

  private long retryMaxDelay;

  /**
   * The constructor.
   *
   * @param properties
   */
  public DefaultBackOffPolicy(DefaultBackOffPolicyProperties properties) {

    Assert.isTrue(properties.getRetryDelay() > 0, "The property  \"retry-delay \" must be> 0.");
    Assert.isTrue(properties.getRetryDelayMultiplier() > 0.0, "The property \"retry-delay-multiplier\" must be> 0.");
    Assert.isTrue(properties.getRetryMaxDelay() > 0, "The property \"retry-max-delay \" must be> 0.");

    this.retryReEnqueueDelay = properties.getRetryReEnqueueDelay();
    this.retryDelay = properties.getRetryDelay();
    this.retryDelayMultiplier = properties.getRetryDelayMultiplier();
    this.retryMaxDelay = properties.getRetryMaxDelay();
  }

  @Override
  public String getNextRetryTimestamp(long retryCount, String retryUntilTimestamp) {

    long delayValue = (long) (this.retryDelay * Math.pow(this.retryDelayMultiplier, retryCount));

    if (delayValue > this.retryMaxDelay) {
      delayValue = this.retryMaxDelay;
    }

    String result = Instant.now().plusMillis(delayValue).toString();

    if (result.compareTo(retryUntilTimestamp) > 0) {
      result = retryUntilTimestamp;
    }

    return result;
  }

  @Override
  public void sleepBeforeReEnqueue() {

    if (this.retryReEnqueueDelay > 0) {
      try {
        Thread.sleep(this.retryReEnqueueDelay);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

}
