package com.devonfw.module.kafka.common.messaging.retry.impl;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.devonfw.module.kafka.common.messaging.api.Message;

/**
 * @author ravicm
 *
 */
public class MessageRetryContext {

  /**
   *
   */
  public static final String RETRY_UNTIL_NAME = "messageRetryUntil";

  public static final String RETRY_NEXT_NAME = "messageRetryNext";

  public static final String RETRY_READ_COUNT_NAME = "messageRetryReadCount";

  public static final String RETRY_COUNT_NAME = "messageRetryCount";

  public static final String RETRY_STATE_NAME = "messageRetryState";

  public static final String RETRY_CHECKPOINT_NAME = "messageRetryCheckpoint";

  public enum RetryState {

    PENDING,

    SUCCESSFUL,

    FAILED,

    EXPIRED;

  }

  private long retryReadCount;

  private String retryUntil;

  private String retryNext;

  private long retryCount;

  private RetryState retryState = RetryState.PENDING;

  private String retryCheckpoint;

  public long getRetryReadCount() {

    return this.retryReadCount;
  }

  public void setRetryReadCount(long retryReadCount) {

    this.retryReadCount = retryReadCount;
  }

  public String getRetryUntil() {

    return this.retryUntil;
  }

  public void setRetryUntil(String retryUntil) {

    this.retryUntil = retryUntil;
  }

  public String getRetryNext() {

    return this.retryNext;
  }

  public void setRetryNext(String retryNext) {

    this.retryNext = retryNext;
  }

  public long getRetryCount() {

    return this.retryCount;
  }

  public void setRetryCount(long retryCount) {

    this.retryCount = retryCount;
  }

  public RetryState getRetryState() {

    return this.retryState;
  }

  public void setRetryState(RetryState retryState) {

    this.retryState = retryState;
  }

  public String getRetryCheckpoint() {

    return this.retryCheckpoint;
  }

  public void setRetryCheckpoint(String retryCheckpoint) {

    this.retryCheckpoint = retryCheckpoint;
  }

  /**
   * @param message
   * @return
   */
  public static MessageRetryContext from(Message<?> message) {

    Assert.notNull(message, "The message parameter cannot be null.");

    String value = message.getHeaderValue(RETRY_UNTIL_NAME);
    if (StringUtils.isEmpty(value)) {
      return null;
    }

    MessageRetryContext result = new MessageRetryContext();
    result.setRetryUntil(value);

    result.setRetryNext(message.getHeaderValue(RETRY_NEXT_NAME));

    value = message.getHeaderValue(RETRY_READ_COUNT_NAME);
    if (value != null) {
      try {
        result.setRetryReadCount(Long.parseLong(value));
      } catch (Exception e) {
        result.setRetryReadCount(0);
      }
    }

    value = message.getHeaderValue(RETRY_COUNT_NAME);
    if (value != null) {
      try {
        result.setRetryCount(Long.parseLong(value));
      } catch (Exception e) {
        result.setRetryCount(0);
      }
    }

    value = message.getHeaderValue(RETRY_STATE_NAME);
    if (value != null) {
      try {
        result.retryState = RetryState.valueOf(value);
      } catch (Exception e) {
        result.retryState = RetryState.PENDING;
      }
    }

    result.setRetryCheckpoint(message.getHeaderValue(RETRY_CHECKPOINT_NAME));

    return result;
  }

  /**
   * @param message
   */
  public void injectInto(Message<?> message) {

    Assert.notNull(message, "Parameter message cannot be null.");

    if (StringUtils.isEmpty(this.retryUntil)) {
      throw new IllegalStateException("Retry-Until must be specified in the retry context.");
    }

    message.setHeader(RETRY_UNTIL_NAME, this.retryUntil);

    if (StringUtils.isEmpty(this.retryNext)) {
      message.setHeader(RETRY_NEXT_NAME, this.retryNext);
    }

    if (this.retryReadCount > 0) {
      message.setHeader(RETRY_READ_COUNT_NAME, Long.toString(this.retryReadCount));
    }

    if (this.retryCount > 0) {
      message.setHeader(RETRY_COUNT_NAME, Long.toString(this.retryCount));
    }

    message.setHeader(RETRY_STATE_NAME, this.retryState.toString());

    if (this.retryCheckpoint != null) {
      message.setHeader(RETRY_CHECKPOINT_NAME, this.retryCheckpoint);
    }
  }

  public void incRetryReadCount() {

    this.retryReadCount++;
  }

  public void incRetryCount() {

    this.retryCount++;
  }

}
