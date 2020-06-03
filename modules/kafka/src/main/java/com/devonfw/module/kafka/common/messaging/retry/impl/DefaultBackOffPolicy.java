package com.devonfw.module.kafka.common.messaging.retry.impl;

import java.time.Instant;
import java.util.Optional;

import org.springframework.util.CollectionUtils;

import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageBackOffPolicy;
import com.devonfw.module.kafka.common.messaging.retry.api.config.DefaultBackOffPolicyProperties;

/**
 * This is an implementation class for the {@link MessageBackOffPolicy}.
 *
 */
public class DefaultBackOffPolicy implements MessageBackOffPolicy {

  private DefaultBackOffPolicyProperties properties;

  /**
   * The constructor.
   *
   * @param properties the {@link DefaultBackOffPolicyProperties}
   */
  public DefaultBackOffPolicy(DefaultBackOffPolicyProperties properties) {

    Optional.ofNullable(properties).ifPresent(this::checkProperties);

    this.properties = properties;
  }

  private void checkProperties(DefaultBackOffPolicyProperties backOffPolicyProperties) {

    if (!CollectionUtils.isEmpty(backOffPolicyProperties.getRetryDelay())) {
      backOffPolicyProperties.getRetryDelay().forEach((key, value) -> {
        if (value < 0) {
          throw new IllegalArgumentException("The property  \"retry-delay \" must be> 0.");
        }
      });
    }

    if (!CollectionUtils.isEmpty(backOffPolicyProperties.getRetryDelayMultiplier())) {
      backOffPolicyProperties.getRetryDelayMultiplier().forEach((key, value) -> {
        if (value < 0.0) {
          throw new IllegalArgumentException("The property \"retry-delay-multiplier\" must be> 0.");
        }
      });
    }

    if (!CollectionUtils.isEmpty(backOffPolicyProperties.getRetryMaxDelay())) {
      backOffPolicyProperties.getRetryDelay().forEach((key, value) -> {
        if (value < 0) {
          throw new IllegalArgumentException("The property \"retry-max-delay \" must be> 0.");
        }
      });
    }
  }

  @Override
  public Instant getNextRetryTimestamp(long currentRetryCount, String retryUntilTimestamp, String topic) {

    long retryDelay = getRetryDelayForTopic(topic, this.properties);

    double retryDelayMultiplier = getRetryDelayMultiplierForTopic(topic, this.properties);

    long retryMaxDelay = getRetryMaxDelayForTopic(topic, this.properties);

    long delayValue = (long) (retryDelay * Math.pow(retryDelayMultiplier, currentRetryCount));

    if (delayValue > retryMaxDelay) {
      delayValue = retryMaxDelay;
    }

    Instant result = Instant.now().plusMillis(delayValue);

    if (result.toString().compareTo(retryUntilTimestamp) > 0) {
      result = Instant.parse(retryUntilTimestamp);
    }

    return result;
  }

  @Override
  public void sleepBeforeReEnqueue(String topic) {

    long retryReEnqueueDelay = getRetryReEnqueueDelayForTopic(topic, this.properties);

    if (retryReEnqueueDelay > 0) {
      try {
        Thread.sleep(retryReEnqueueDelay);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  private long getRetryDelayForTopic(String topic, DefaultBackOffPolicyProperties backOffProperties) {

    return Optional.ofNullable(backOffProperties.getRetryDelay().get(topic))
        .orElse(backOffProperties.getRetryDelayDefault());
  }

  private double getRetryDelayMultiplierForTopic(String topic, DefaultBackOffPolicyProperties backOffProperties) {

    return Optional.ofNullable(backOffProperties.getRetryDelayMultiplier().get(topic))
        .orElse(backOffProperties.getRetryDelayMultiplierDefault());
  }

  private long getRetryMaxDelayForTopic(String topic, DefaultBackOffPolicyProperties backOffProperties) {

    return Optional.ofNullable(backOffProperties.getRetryMaxDelay().get(topic))
        .orElse(backOffProperties.getRetryMaxDelayDefault());
  }

  private long getRetryReEnqueueDelayForTopic(String topic, DefaultBackOffPolicyProperties backOffProperties) {

    return Optional.ofNullable(backOffProperties.getRetryReEnqueueDelay().get(topic))
        .orElse(backOffProperties.getRetryReEnqueueDelayDefault());
  }

}
