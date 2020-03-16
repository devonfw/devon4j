package com.devonfw.module.kafka.common.messaging.retry.api.client;

import java.time.Instant;

import com.devonfw.module.kafka.common.messaging.retry.api.config.DefaultBackOffPolicyProperties;

/**
 * This interface is used to calculate next retry time stamps.
 *
 */
public interface MessageBackOffPolicy {

  /**
   * This method is used to calculate the next retry time stamp based on given retry count and retry until time stamp.
   *
   * @param retryCount the no of retry count
   * @param retryUntilTimestamp the retry until time stamp which requires a Instant in String format.
   * @return the Instant
   */
  Instant getNextRetryTimestamp(long retryCount, String retryUntilTimestamp);

  /**
   * This method is used to make the thread to sleep for {@link DefaultBackOffPolicyProperties#getRetryReEnqueueDelay()}
   * seconds.
   */
  void sleepBeforeReEnqueue();

}
