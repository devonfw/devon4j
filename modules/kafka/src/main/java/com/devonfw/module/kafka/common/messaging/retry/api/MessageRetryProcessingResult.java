package com.devonfw.module.kafka.common.messaging.retry.api;

/**
 * An Enum class to indicate multiple event results of retry pattern.
 *
 */
public enum MessageRetryProcessingResult {

  /**
   * PROCESSING SUCCESSFUL
   */
  PROCESSING_SUCCESSFUL,

  /**
   * RETRY AFTER PROCESSING TRIAL
   */
  RETRY_AFTER_PROCESSING_TRIAL,

  /**
   * RETRY WITHOUT PROCESSING
   */
  RETRY_WITHOUT_PROCESSING,

  /**
   * RETRY PERIOD EXPIRED
   */
  RETRY_PERIOD_EXPIRED,

  /**
   * NO PROCESSING
   */
  NO_PROCESSING,

  /**
   * FINAL FAIL HANDLER SUCCESSFUL
   */
  FINAL_FAIL_HANDLER_SUCCESSFUL;

}
