package com.devonfw.module.kafka.common.messaging.retry.api;

/**
 * An Enum class to indicate current retry patter state.
 *
 * @deprecated The implementation of Devon4Js Kafka module will be abandoned. It is superseeded by Springs Kafka
 *             implementation.
 */
@Deprecated
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
