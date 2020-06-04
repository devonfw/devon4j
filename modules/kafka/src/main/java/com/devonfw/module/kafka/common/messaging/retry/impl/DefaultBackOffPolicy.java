package com.devonfw.module.kafka.common.messaging.retry.impl;

import java.time.Instant;
import java.util.Map;
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

  private static final String DEFAULT_KEY = "default";

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

    long retryDelay = getRetryDelayForTopic(topic);

    double retryDelayMultiplier = getRetryDelayMultiplierForTopic(topic);

    long retryMaxDelay = getRetryMaxDelayForTopic(topic);

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

    long retryReEnqueueDelay = getRetryReEnqueueDelayForTopic(topic);

    if (retryReEnqueueDelay > 0) {
      try {
        Thread.sleep(retryReEnqueueDelay);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  private long getRetryDelayForTopic(String topic) {

    Map<String, Long> retryDelayMap = this.properties.getRetryDelay();

    if (retryDelayMap.containsKey(DEFAULT_KEY)) {
      return Optional.ofNullable(retryDelayMap.get(DEFAULT_KEY)).orElse(this.properties.getRetryDelayDefault());
    }

    return Optional.ofNullable(retryDelayMap.get(topic)).orElse(this.properties.getRetryDelayDefault());
  }

  private double getRetryDelayMultiplierForTopic(String topic) {

    Map<String, Double> retryDelayMultiplierMap = this.properties.getRetryDelayMultiplier();

    if (retryDelayMultiplierMap.containsKey(DEFAULT_KEY)) {
      return Optional.ofNullable(retryDelayMultiplierMap.get(DEFAULT_KEY))
          .orElse(this.properties.getRetryDelayMultiplierDefault());
    }

    return Optional.ofNullable(retryDelayMultiplierMap.get(topic))
        .orElse(this.properties.getRetryDelayMultiplierDefault());
  }

  private long getRetryMaxDelayForTopic(String topic) {

    Map<String, Long> retryMaxDelayMap = this.properties.getRetryMaxDelay();

    if (retryMaxDelayMap.containsKey(DEFAULT_KEY)) {
      return Optional.ofNullable(retryMaxDelayMap.get(DEFAULT_KEY)).orElse(this.properties.getRetryMaxDelayDefault());
    }

    return Optional.ofNullable(retryMaxDelayMap.get(topic)).orElse(this.properties.getRetryMaxDelayDefault());
  }

  private long getRetryReEnqueueDelayForTopic(String topic) {

    Map<String, Long> retryReEnqueueDelayMap = this.properties.getRetryReEnqueueDelay();

    if (retryReEnqueueDelayMap.containsKey(DEFAULT_KEY)) {
      return Optional.ofNullable(retryReEnqueueDelayMap.get(DEFAULT_KEY))
          .orElse(this.properties.getRetryReEnqueueDelayDefault());
    }

    return Optional.ofNullable(this.properties.getRetryReEnqueueDelay().get(topic))
        .orElse(this.properties.getRetryReEnqueueDelayDefault());
  }

}
