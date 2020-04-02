package com.devonfw.module.kafka.common.messaging.api.config;

import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

/**
 * This is a property class used to create configuration for {@link MessageReceiverConfig}.
 */
public class KafkaListenerContainerProperties {

  private Integer ackCount;

  private Long ackTime;

  private String ackMode = "MANUAL_IMMEDIATE";

  private Boolean ackOnError;

  private Integer concurrency;

  private Long idleEventInterval;

  private Long pollTimeout;

  private Long shutdownTimeout;

  private Boolean syncCommits;

  /**
   * The maximum number of concurrent {@link KafkaMessageListenerContainer}s running. Messages from within the same
   * partition will be processed sequentially.
   *
   * @return the concurrency
   *
   */
  public Integer getConcurrency() {

    return this.concurrency;
  }

  /**
   * The maximum number of concurrent {@link KafkaMessageListenerContainer}s running. Messages from within the same
   * partition will be processed sequentially.
   *
   * @param concurrency the concurrency.
   */
  public void setConcurrency(Integer concurrency) {

    this.concurrency = concurrency;
  }

  /**
   * The ack mode to use when auto ack (in the configuration properties) is false.
   * <ul>
   * <li>RECORD: Ack after each record has been passed to the listener.</li>
   * <li>BATCH: Ack after each batch of records received from the consumer has been passed to the listener</li>
   * <li>TIME: Ack after this number of milliseconds; (should be greater than
   * {@code #setPollTimeout(long) pollTimeout}.</li>
   * <li>COUNT: Ack after at least this number of records have been received</li>
   * <li>MANUAL: Listener is responsible for acking - use a
   * {@link org.springframework.kafka.listener.AcknowledgingMessageListener}.
   * </ul>
   *
   * @return the ack mode.
   */
  public String getAckMode() {

    return this.ackMode;
  }

  /**
   * The ack mode to use when auto ack (in the configuration properties) is false.
   * <ul>
   * <li>RECORD: Ack after each record has been passed to the listener.</li>
   * <li>BATCH: Ack after each batch of records received from the consumer has been passed to the listener</li>
   * <li>TIME: Ack after this number of milliseconds; (should be greater than
   * {@code #setPollTimeout(long) pollTimeout}.</li>
   * <li>COUNT: Ack after at least this number of records have been received</li>
   * <li>MANUAL: Listener is responsible for acking - use a
   * {@link org.springframework.kafka.listener.AcknowledgingMessageListener}.
   * </ul>
   *
   * @param ackMode the ack mode.
   */
  public void setAckMode(String ackMode) {

    this.ackMode = ackMode;
  }

  /**
   * The number of outstanding record count after which offsets should be committed when {@link AckMode#COUNT} or
   * {@link AckMode#COUNT_TIME} is being used.
   *
   * @return the ack count.
   */
  public Integer getAckCount() {

    return this.ackCount;
  }

  /**
   * The number of outstanding record count after which offsets should be committed when {@link AckMode#COUNT} or
   * {@link AckMode#COUNT_TIME} is being used.
   *
   * @param ackCount the ack count
   */
  public void setAckCount(Integer ackCount) {

    this.ackCount = ackCount;
  }

  /**
   * The time (ms) after which outstanding offsets should be committed when {@link AckMode#TIME} or
   * {@link AckMode#COUNT_TIME} is being used. Should be larger than
   *
   * @return the ack time in milliseconds.
   */
  public Long getAckTime() {

    return this.ackTime;
  }

  /**
   * * The time (ms) after which outstanding offsets should be committed when {@link AckMode#TIME} or
   * {@link AckMode#COUNT_TIME} is being used. Should be larger than
   *
   * @param ackTime the ack time in milliseconds.
   */
  public void setAckTime(Long ackTime) {

    this.ackTime = ackTime;
  }

  /**
   * The max time to block in the consumer waiting for records.
   *
   * @return the max poll timeout
   */
  public Long getPollTimeout() {

    return this.pollTimeout;
  }

  /**
   * The max time to block in the consumer waiting for records.
   *
   * @param pollTimeout the max poll timeout
   */
  public void setPollTimeout(Long pollTimeout) {

    this.pollTimeout = pollTimeout;
  }

  /**
   * The timeout for shutting down the container. This is the maximum amount of time that the invocation to
   * {@code #stop(Runnable)} will block for, before returning.
   *
   * @return the shutdown timeout
   */
  public Long getShutdownTimeout() {

    return this.shutdownTimeout;
  }

  /**
   * The timeout for shutting down the container. This is the maximum amount of time that the invocation to
   * {@code #stop(Runnable)} will block for, before returning.
   *
   * @param shutdownTimeout the shutdown timeout
   */
  public void setShutdownTimeout(Long shutdownTimeout) {

    this.shutdownTimeout = shutdownTimeout;
  }

  /**
   * Whether or not to call consumer.commitSync() or commitAsync() when the container is responsible for commits.
   * Default true.
   *
   * @return the boolean value
   */
  public Boolean getSyncCommits() {

    return this.syncCommits;
  }

  /**
   * Whether or not to call consumer.commitSync() or commitAsync() when the container is responsible for commits.
   * Default true.
   *
   * @param syncCommits a boolean value
   */
  public void setSyncCommits(Boolean syncCommits) {

    this.syncCommits = syncCommits;
  }

  /**
   * @return the boolean value
   */
  public Boolean getAckOnError() {

    return this.ackOnError;
  }

  /**
   * Set whether or not the container should commit offsets (ack messages) where the listener throws exceptions. This
   * works in conjunction with {@link #getAckMode} and is effective only when the kafka property
   * {@code enable.auto.commit} is {@code false}; it is not applicable to manual ack modes. When this property is set to
   * {@code true} (the default), all messages handled will have their offset committed. When set to {@code false},
   * offsets will be committed only for successfully handled messages. Manual acks will always be applied. Bear in mind
   * that, if the next message is successfully handled, its offset will be committed, effectively committing the offset
   * of the failed message anyway, so this option has limited applicability. Perhaps useful for a component that starts
   * throwing exceptions consistently; allowing it to resume when restarted from the last successfully processed
   * message.
   * <p>
   * Does not apply when transactions are used - in that case, whether or not the offsets are sent to the transaction
   * depends on whether the transaction is committed or rolled back. If a listener throws an exception, the transaction
   * will normally be rolled back unless an error handler is provided that handles the error and exits normally; in
   * which case the offsets are sent to the transaction and the transaction is committed.
   *
   * @param ackOnError whether the container should acknowledge messages that throw exceptions.
   */
  public void setAckOnError(Boolean ackOnError) {

    this.ackOnError = ackOnError;
  }

  /**
   * an event is emitted if a poll returns no records and this interval has elapsed since a record was returned.
   *
   * @return the interval.
   */
  public Long getIdleEventInterval() {

    return this.idleEventInterval;
  }

  /**
   * /** Set the idle event interval; when set, an event is emitted if a poll returns no records and this interval has
   * elapsed since a record was returned.
   *
   * @param idleEventInterval the interval.
   */
  public void setIdleEventInterval(Long idleEventInterval) {

    this.idleEventInterval = idleEventInterval;
  }

}
