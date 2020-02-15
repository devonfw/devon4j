package com.devonfw.module.kafka.common.messaging.retry.impl;

public enum MessageRetryProcessingResult {

  PROCESSING_SUCCESSFUL,

  RETRY_AFTER_PROCESSING_TRIAL,

  RETRY_WITHOUT_PROCESSING,

  RETRY_PERIOD_EXPIRED,

  NO_PROCESSING,

  FINAL_FAIL_HANDLER_SUCCESSFUL;

}
