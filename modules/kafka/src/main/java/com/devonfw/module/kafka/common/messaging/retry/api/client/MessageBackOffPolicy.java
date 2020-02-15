package com.devonfw.module.kafka.common.messaging.retry.api.client;

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
  String getNextRetryTimestamp(long retryCount, String retryUntilTimestamp);

  /**
   *
   */
  void sleepBeforeReEnqueue();

}
