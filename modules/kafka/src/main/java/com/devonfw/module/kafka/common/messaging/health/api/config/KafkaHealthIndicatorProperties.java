package com.devonfw.module.kafka.common.messaging.health.api.config;

import java.util.Set;

import com.devonfw.module.kafka.common.messaging.health.impl.KafkaHealthIndicator;

/**
 * A property class which is used to configure for {@link KafkaHealthIndicator}
 *
 * @deprecated The implementation of Devon4Js Kafka module will be abandoned. It is superseeded by Springs Kafka
 *             implementation.
 */
@Deprecated
public class KafkaHealthIndicatorProperties {

  private int timeout = 60;

  private Set<String> topicsToCheck;

  /**
   * @return the timeout
   */
  public int getTimeout() {

    return this.timeout;
  }

  /**
   * Set the timeout for the {@link #getTimeout()}.
   *
   * @param timeout the timeout
   */
  public void setTimeout(int timeout) {

    this.timeout = timeout;
  }

  /**
   * @return the list of unique topics to check.
   */
  public Set<String> getTopicsToCheck() {

    return this.topicsToCheck;
  }

  /**
   * Set the list of unique to do health check.
   *
   * @param topicsToCheck the topics to check health.
   */
  public void setTopicsToCheck(Set<String> topicsToCheck) {

    this.topicsToCheck = topicsToCheck;
  }

}
