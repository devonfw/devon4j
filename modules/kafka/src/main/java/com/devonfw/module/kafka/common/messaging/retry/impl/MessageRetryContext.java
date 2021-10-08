package com.devonfw.module.kafka.common.messaging.retry.impl;

import static com.devonfw.module.kafka.common.messaging.util.MessageUtil.addHeaderValue;

import java.nio.charset.Charset;
import java.time.Instant;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.devonfw.module.kafka.common.messaging.retry.api.RetryState;

/**
 * The MessageRetryContext is a class used to create retry pattern with the custom headers carries values for the retry
 * to proceed.
 *
 * @deprecated The implementation of Devon4Js Kafka module will be abandoned. It is superseeded by Springs Kafka
 *             implementation.
 */
@Deprecated
public class MessageRetryContext {

  /**
   * The RETRY_UNTIL header.
   */
  public static final String RETRY_UNTIL = "messageRetryUntil";

  /**
   * RETRY_NEXT header.
   */
  public static final String RETRY_NEXT = "messageRetryNext";

  /**
   * RETRY_READ_COUNT header.
   */
  public static final String RETRY_READ_COUNT = "messageRetryReadCount";

  /**
   * RETRY_COUNT header.
   */
  public static final String RETRY_COUNT = "messageRetryCount";

  /**
   * RETRY_STATE header.
   */
  public static final String RETRY_STATE = "messageRetryState";

  private long retryReadCount;

  private Instant retryUntil;

  private Instant retryNext;

  private long currentRetryCount;

  private RetryState retryState = RetryState.PENDING;

  /**
   * The retry read count.
   *
   * @return long
   */
  public long getRetryReadCount() {

    return this.retryReadCount;
  }

  /**
   * Set the retry read count for {@link #getRetryReadCount()}
   *
   * @param retryReadCount the retry read count.
   */
  public void setRetryReadCount(long retryReadCount) {

    this.retryReadCount = retryReadCount;
  }

  /**
   * The retry until in {@link Instant} format.
   *
   * @return {@link Instant}
   */
  public Instant getRetryUntil() {

    return this.retryUntil;
  }

  /**
   * Set the retry until in {@link Instant}format for {@link #getRetryUntil()}
   *
   * @param retryUntil the retry until.
   */
  public void setRetryUntil(Instant retryUntil) {

    this.retryUntil = retryUntil;
  }

  /**
   * The next retry {@link Instant}
   *
   * @return {@link Instant}
   */
  public Instant getRetryNext() {

    return this.retryNext;
  }

  /**
   * Set the next retry {@link Instant} for {@link #getRetryNext()}
   *
   * @param retryNext the next retry.
   */
  public void setRetryNext(Instant retryNext) {

    this.retryNext = retryNext;
  }

  /**
   * The retry count
   *
   * @return long.
   */
  public long getCurrentRetryCount() {

    return this.currentRetryCount;
  }

  /**
   * Set the retry count for {@link #getCurrentRetryCount()}
   *
   * @param retryCount the retry count.
   */
  public void setCurrentRetryCount(long retryCount) {

    this.currentRetryCount = retryCount;
  }

  /**
   * The {@link RetryState}
   *
   * @return the {@link RetryState}
   */
  public RetryState getRetryState() {

    return this.retryState;
  }

  /**
   * Set the {@link RetryState} for {@link #getRetryState()}. By Default 'PENDING'.
   *
   * @param retryState the {@link RetryState}
   */
  public void setRetryState(RetryState retryState) {

    this.retryState = retryState;
  }

  /**
   * This method is used to create {@link MessageRetryContext} from its custom headers in
   * {@link ProducerRecord#headers()}.
   *
   * @param consumerRecord the {@link ConsumerRecord}
   * @return {@link MessageRetryContext} or {@code null} if Retry-Headers aren't present
   */
  public static MessageRetryContext from(ConsumerRecord<?, ?> consumerRecord) {

    if (ObjectUtils.isEmpty(consumerRecord)) {
      throw new IllegalArgumentException("The ConsumerRecord parameter cannot be null.");
    }

    MessageRetryContext result = new MessageRetryContext();

    Headers headers = consumerRecord.headers();

    Header retryStateHeader = headers.lastHeader(RETRY_STATE);
    if (retryStateHeader != null && retryStateHeader.value() != null) {
      String value = new String(headers.lastHeader(RETRY_STATE).value(), Charset.forName("UTF-8"));
      try {
        result.retryState = RetryState.valueOf(value);
      } catch (Exception e) {
        result.retryState = RetryState.PENDING;
      }
    } else {
      return null;
    }

    Header retryUntilHeader = headers.lastHeader(RETRY_UNTIL);
    if (retryUntilHeader != null && retryUntilHeader.value() != null) {
      String value = new String(retryUntilHeader.value(), Charset.forName("UTF-8"));
      result.setRetryUntil(Instant.parse(value));
    }

    Header retryNextHeader = headers.lastHeader(RETRY_NEXT);
    if (retryNextHeader != null && retryNextHeader.value() != null) {
      String value = new String(retryNextHeader.value(), Charset.forName("UTF-8"));
      result.setRetryNext(Instant.parse(value));
    }

    Header retryReadCount = headers.lastHeader(RETRY_READ_COUNT);
    if (retryReadCount != null && retryReadCount.value() != null) {
      String value = new String(retryReadCount.value(), Charset.forName("UTF-8"));
      try {
        result.setRetryReadCount(Long.parseLong(value));
      } catch (Exception e) {
        result.setRetryReadCount(0);
      }
    }

    Header retryCount = headers.lastHeader(RETRY_COUNT);
    if (retryCount != null && retryCount.value() != null) {
      String value = new String(retryCount.value(), Charset.forName("UTF-8"));
      try {
        result.setCurrentRetryCount(Long.parseLong(value));
      } catch (Exception e) {
        result.setRetryReadCount(0);
      }
    }

    return result;
  }

  /**
   * This method is used to inject the custom retry headers to the {@link ProducerRecord#headers()}
   *
   * @param producerRecord the {@link ProducerRecord}
   */
  public void injectInto(ProducerRecord<?, ?> producerRecord) {

    if (ObjectUtils.isEmpty(producerRecord)) {
      throw new IllegalArgumentException("The message parameter cannot be null.");
    }

    if (StringUtils.isEmpty(this.retryUntil)) {
      throw new IllegalStateException("Retry-Until must be specified in the retry context.");
    }

    Headers headers = producerRecord.headers();

    addHeaderValue(headers, RETRY_UNTIL, this.retryUntil.toString());

    if (!StringUtils.isEmpty(this.retryNext)) {
      addHeaderValue(headers, RETRY_NEXT, this.retryNext.toString());
    }

    if (this.retryReadCount > 0) {
      addHeaderValue(headers, RETRY_READ_COUNT, Long.toString(this.retryReadCount));
    }

    if (this.currentRetryCount > 0) {
      addHeaderValue(headers, RETRY_COUNT, Long.toString(this.currentRetryCount));
    }

    addHeaderValue(headers, RETRY_STATE, this.retryState.toString());

  }

  /**
   * This method is used to increment the readReadyCount by 1.
   */
  public void incRetryReadCount() {

    this.retryReadCount++;
  }

  /**
   * This method is used to increment the current retryCount by 1.
   */
  public void incCurrentRetryCount() {

    this.currentRetryCount++;
  }

}
