package com.devonfw.module.kafka.common.messaging.retry.api.client;

import com.devonfw.module.kafka.common.messaging.api.Message;
import com.devonfw.module.kafka.common.messaging.retry.impl.MessageRetryContext;

/**
 * @author ravicm
 *
 */
public interface MessageRetryPolicy {

  /**
   * @param message
   * @param retryContext
   * @param ex
   * @return
   */
  boolean canRetry(Message<?> message, MessageRetryContext retryContext, Exception ex);

  /**
   * @param message
   * @param retryContext
   * @return
   */
  String getRetryUntilTimestamp(Message<?> message, MessageRetryContext retryContext);

}
