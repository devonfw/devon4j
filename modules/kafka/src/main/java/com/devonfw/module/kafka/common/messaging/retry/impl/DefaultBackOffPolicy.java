package com.devonfw.module.kafka.common.messaging.retry.impl;

import java.time.Instant;
import java.util.Optional;

import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageBackOffPolicy;
import com.devonfw.module.kafka.common.messaging.retry.api.config.DefaultBackOffPolicyProperties;

/**
 * This is an implementation class for the {@link MessageBackOffPolicy}.
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
   * @param properties the {@link DefaultBackOffPolicyProperties}
   */
  public DefaultBackOffPolicy(DefaultBackOffPolicyProperties properties) {

    Optional.ofNullable(properties).ifPresent(this::checkProperties);

    this.retryReEnqueueDelay = properties.getRetryReEnqueueDelay();
    this.retryDelay = properties.getRetryDelay();
    this.retryDelayMultiplier = properties.getRetryDelayMultiplier();
    this.retryMaxDelay = properties.getRetryMaxDelay();
  }

  private void checkProperties(DefaultBackOffPolicyProperties properties) {

    if (properties.getRetryDelay() < 0) {
      throw new IllegalArgumentException("The property  \"retry-delay \" must be> 0.");
    }

    if (properties.getRetryDelayMultiplier() < 0.0) {
      throw new IllegalArgumentException("The property \"retry-delay-multiplier\" must be> 0.");
    }
    if (properties.getRetryMaxDelay() < 0) {
      throw new IllegalArgumentException("The property \"retry-max-delay \" must be> 0.");
    }

  }

  @Override
  public Instant getNextRetryTimestamp(long currentRetryCount, String retryUntilTimestamp) {

    long delayValue = (long) (this.retryDelay * Math.pow(this.retryDelayMultiplier, currentRetryCount));

    if (delayValue > this.retryMaxDelay) {
      delayValue = this.retryMaxDelay;
    }

    Instant result = Instant.now().plusMillis(delayValue);

    if (result.toString().compareTo(retryUntilTimestamp) > 0) {
      result = Instant.parse(retryUntilTimestamp);
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
