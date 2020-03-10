package com.devonfw.module.kafka.common.messaging.retry.impl;

import static com.devonfw.module.kafka.common.messaging.util.MessageUtil.addHeaderValue;

import java.time.Instant;

import org.apache.commons.codec.Charsets;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * @author ravicm
 *
 */
public class MessageRetryContext {

  /**
   *
   */
  public static final String RETRY_UNTIL = "messageRetryUntil";

  public static final String RETRY_NEXT = "messageRetryNext";

  public static final String RETRY_READ_COUNT = "messageRetryReadCount";

  public static final String RETRY_COUNT = "messageRetryCount";

  public static final String RETRY_STATE = "messageRetryState";

  public static final String RETRY_CHECKPOINT_NAME = "messageRetryCheckpoint";

  /**
   * @author ravicm
   *
   */
  public enum RetryState {

    PENDING,

    SUCCESSFUL,

    FAILED,

    EXPIRED;

  }

  private long retryReadCount;

  private Instant retryUntil;

  private Instant retryNext;

  private long retryCount;

  private RetryState retryState = RetryState.PENDING;

  private String retryCheckpoint;

  public long getRetryReadCount() {

    return this.retryReadCount;
  }

  public void setRetryReadCount(long retryReadCount) {

    this.retryReadCount = retryReadCount;
  }

  public Instant getRetryUntil() {

    return this.retryUntil;
  }

  public void setRetryUntil(Instant retryUntil) {

    this.retryUntil = retryUntil;
  }

  public Instant getRetryNext() {

    return this.retryNext;
  }

  public void setRetryNext(Instant retryNext) {

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
   * @param producerRecord
   * @return
   */
  public static MessageRetryContext from(ProducerRecord<Object, Object> producerRecord) {

    if (ObjectUtils.isEmpty(producerRecord)) {
      throw new IllegalArgumentException("The message parameter cannot be null.");
    }

    Headers headers = producerRecord.headers();

    String value = new String(headers.lastHeader(RETRY_UNTIL).value(), Charsets.UTF_8);
    if (StringUtils.isEmpty(value)) {
      return null;
    }

    MessageRetryContext result = new MessageRetryContext();
    result.setRetryUntil(Instant.parse(value));

    result.setRetryNext(Instant.parse(new String(headers.lastHeader(RETRY_NEXT).value())));

    value = new String(headers.lastHeader(RETRY_READ_COUNT).value(), Charsets.UTF_8);
    if (value != null) {
      try {
        result.setRetryReadCount(Long.parseLong(value));
      } catch (Exception e) {
        result.setRetryReadCount(0);
      }
    }

    value = new String(headers.lastHeader(RETRY_COUNT).value(), Charsets.UTF_8);
    if (value != null) {
      try {
        result.setRetryCount(Long.parseLong(value));
      } catch (Exception e) {
        result.setRetryCount(0);
      }
    }

    value = new String(headers.lastHeader(RETRY_STATE).value(), Charsets.UTF_8);
    if (value != null) {
      try {
        result.retryState = RetryState.valueOf(value);
      } catch (Exception e) {
        result.retryState = RetryState.PENDING;
      }
    }

    return result;
  }

  /**
   * @param producerRecord
   */
  public void injectInto(ProducerRecord<Object, Object> producerRecord) {

    if (ObjectUtils.isEmpty(producerRecord)) {
      throw new IllegalArgumentException("The message parameter cannot be null.");
    }

    if (StringUtils.isEmpty(this.retryUntil)) {
      throw new IllegalStateException("Retry-Until must be specified in the retry context.");
    }

    Headers headers = producerRecord.headers();

    addHeaderValue(headers, RETRY_UNTIL, this.retryUntil.toString());

    if (StringUtils.isEmpty(this.retryNext)) {
      addHeaderValue(headers, RETRY_NEXT, this.retryNext.toString());
    }

    if (this.retryReadCount > 0) {
      addHeaderValue(headers, RETRY_READ_COUNT, Long.toString(this.retryReadCount));
    }

    if (this.retryCount > 0) {
      addHeaderValue(headers, RETRY_COUNT, Long.toString(this.retryCount));
    }

    addHeaderValue(headers, RETRY_STATE, this.retryState.toString());

  }

  public void incRetryReadCount() {

    this.retryReadCount++;
  }

  public void incRetryCount() {

    this.retryCount++;
  }

}
