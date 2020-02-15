package com.devonfw.module.kafka.common.messaging.retry.api.client;

import com.devonfw.module.kafka.common.messaging.api.Message;
import com.devonfw.module.kafka.common.messaging.retry.impl.MessageRetryContext;

/**
 * @author ravicm
 *
 */
public interface MessageRetryHandler {

  /**
   * @param message
   * @param retryContext
   */
  void retryTimeout(Message<?> message, MessageRetryContext retryContext);

  /**
   * @param message
   * @param retryContext
   * @param ex
   * @return
   */
  boolean retryFailedFinal(Message<?> message, MessageRetryContext retryContext, Exception ex);

}
