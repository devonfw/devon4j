package com.devonfw.module.kafka.common.messaging.retry.api.config;

import java.util.HashMap;
import java.util.Map;

import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageBackOffPolicy;

/**
 * This is a property class to define the retry pattern for {@link MessageBackOffPolicy}
 *
 * @deprecated The implementation of Devon4Js Kafka module will be abandoned. It is superseeded by Springs Kafka
 *             implementation.
 */
@Deprecated
public class DefaultBackOffPolicyProperties {

  private Map<String, Long> retryDelay = new HashMap<>();

  private Map<String, Double> retryDelayMultiplier = new HashMap<>();

  private Map<String, Long> retryMaxDelay = new HashMap<>();

  private Map<String, Long> retryReEnqueueDelay = new HashMap<>();

  private long retryDelayDefault = 60000;

  private double retryDelayMultiplierDefault = 1.0;

  private long retryMaxDelayDefault = 60000;

  private long retryReEnqueueDelayDefault = 1000;

  /**
   * The retry enque delay to send again the message to kafka. By default 1000.
   *
   * @return long
   */
  public long getRetryReEnqueueDelayDefault() {

    return this.retryReEnqueueDelayDefault;
  }

  /**
   * The retry delay.
   *
   * @return long.
   */
  public long getRetryDelayDefault() {

    return this.retryDelayDefault;
  }

  /**
   * The retry delay multiplier
   *
   * @return double.
   */
  public double getRetryDelayMultiplierDefault() {

    return this.retryDelayMultiplierDefault;
  }

  /**
   * The retry max delay.
   *
   * @return the long.
   */
  public long getRetryMaxDelayDefault() {

    return this.retryMaxDelayDefault;
  }

  /**
   * @return retryDelay
   */
  public Map<String, Long> getRetryDelay() {

    return this.retryDelay;
  }

  /**
   * @param retryDelay new value of {@link #getRetryDelay}.
   */
  public void setRetryDelay(Map<String, Long> retryDelay) {

    this.retryDelay = retryDelay;
  }

  /**
   * @return retryDelayMultiplier
   */
  public Map<String, Double> getRetryDelayMultiplier() {

    return this.retryDelayMultiplier;
  }

  /**
   * @param retryDelayMultiplier new value of {@link #getRetryDelayMultiplier}.
   */
  public void setRetryDelayMultiplier(Map<String, Double> retryDelayMultiplier) {

    this.retryDelayMultiplier = retryDelayMultiplier;
  }

  /**
   * @return retryMaxDelay
   */
  public Map<String, Long> getRetryMaxDelay() {

    return this.retryMaxDelay;
  }

  /**
   * @param retryMaxDelay new value of {@link #getRetryMaxDelay}.
   */
  public void setRetryMaxDelay(Map<String, Long> retryMaxDelay) {

    this.retryMaxDelay = retryMaxDelay;
  }

  /**
   * @return retryReEnqueueDelay
   */
  public Map<String, Long> getRetryReEnqueueDelay() {

    return this.retryReEnqueueDelay;
  }

  /**
   * @param retryReEnqueueDelay new value of {@link #getRetryReEnqueueDelay}.
   */
  public void setRetryReEnqueueDelay(Map<String, Long> retryReEnqueueDelay) {

    this.retryReEnqueueDelay = retryReEnqueueDelay;
  }

}
