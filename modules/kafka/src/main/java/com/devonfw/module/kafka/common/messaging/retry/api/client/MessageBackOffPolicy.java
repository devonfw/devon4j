package com.devonfw.module.kafka.common.messaging.retry.api.client;

import java.time.Instant;

import com.devonfw.module.kafka.common.messaging.retry.api.config.DefaultBackOffPolicyProperties;

/**
 * This interface is used to calculate next retry time stamps.
 *
 * @deprecated The implementation of devon4j-kafka will be abandoned. It is superseeded by Springs Kafka
 *             implementation.
 */
@Deprecated
public interface MessageBackOffPolicy {

  /**
   * This method is used to calculate the next retry time stamp based on given retry count and retry until time stamp.
   *
   * @param currentRetryCount the count of attempted retry count.
   *
   * @param retryUntilTimestamp the retry until time stamp which requires a Instant in String format.
   * @param topic the topic
   * @return the Instant
   */
  Instant getNextRetryTimestamp(long currentRetryCount, String retryUntilTimestamp, String topic);

  /**
   * This method is used to make the thread to sleep for {@link DefaultBackOffPolicyProperties#getRetryReEnqueueDelay()}
   * seconds.
   *
   * @param topic the topic
   */
  void sleepBeforeReEnqueue(String topic);

}
