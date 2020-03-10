package com.devonfw.module.kafka.common.messaging.retry.api.client;

import java.time.Instant;

/**
 * @author ravicm
 *
 */
public interface MessageBackOffPolicy {

  /**
   * @param retryCount
   * @param retryUntilTimestamp
   * @return
   */
  Instant getNextRetryTimestamp(long retryCount, String retryUntilTimestamp);

  /**
   *
   */
  void sleepBeforeReEnqueue();

}
