package com.devonfw.module.kafka.common.messaging.retry.api;

/**
 * An Enum class to indicate current retry patter state.
 *
 */
public enum RetryState {

  /**
   * PENDING
   */
  PENDING,

  /**
   * SUCESSFUL
   */
  SUCCESSFUL,

  /**
   * FAILED
   */
  FAILED,

  /**
   * EXPRIED
   */
  EXPIRED;

}
