package com.devonfw.module.kafka.common.messaging.api.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.core.ConsumerFactory;

/**
 * This is a property class to create configuration for the {@link MessageReceiverConfig} by setting parameter for
 * {@link ConsumerConfig} to create {@link ConsumerFactory}.
 *
 * @deprecated The implementation of devon4j-kafka will be abandoned. It is superseeded by Springs Kafka
 *             implementation.
 */
@Deprecated
public class KafkaConsumerProperties {

  private Integer autoCommitIntervalMs;

  private String autoOffsetReset = "earliest";

  private Boolean checkCrcs;

  private Boolean enableAutoCommit = false;

  private Boolean excludeInternalTopics;

  private Integer fetchMinBytes;

  private Integer fetchMaxBytes;

  private Integer fetchMaxWaitMs;

  private String groupId;

  private Integer heartbeatIntervalMs;

  private Integer maxPartitionFetchBytes;

  private String interceptorClasses;

  private String isolationLevel;

  private String keyDeserializer;

  private Integer maxPollRecords;

  private Integer maxPollIntervalMs;

  private String partitionAssignmentStrategy;

  private Integer sessionTimeoutMs;

  private String valueDeserializer;

  /**
   * A unique string that identifies the consumer group this consumer belongs to. This property is required if the
   * consumer uses either the group management functionality by using <code>subscribe(topic)</code> or the Kafka-based
   * offset management strategy.
   *
   * @return the group id.
   */
  public String getGroupId() {

    return this.groupId;
  }

  /**
   * Set the groupId for {@link #getGroupId()}
   *
   * @param groupId the group id.
   */
  public void setGroupId(String groupId) {

    this.groupId = groupId;
  }

  /**
   * The maximum number of records returned in a single call to poll().
   *
   * @return the max number
   */
  public Integer getMaxPollRecords() {

    return this.maxPollRecords;
  }

  /**
   * Set the maxPollRecords for {@link #getMaxPollRecords()}
   *
   * @param maxPollRecords the maximum to poll records
   */
  public void setMaxPollRecords(Integer maxPollRecords) {

    this.maxPollRecords = maxPollRecords;
  }

  /**
   * The maximum delay between invocations of poll() when using consumer group management. This places an upper bound on
   * the amount of time that the consumer can be idle before fetching more records. If poll() is not called before
   * expiration of this timeout, then the consumer is considered failed and the group will rebalance in order to
   * reassign the partitions to another member.
   *
   * @return the max poll interval time in milliseconds.
   */
  public Integer getMaxPollIntervalMs() {

    return this.maxPollIntervalMs;
  }

  /**
   * Set the maxPollIntervalMs for {@link #getMaxPollIntervalMs()}
   *
   * @param maxPollIntervalMs the max poll interval time in milliseconds.
   */
  public void setMaxPollIntervalMs(Integer maxPollIntervalMs) {

    this.maxPollIntervalMs = maxPollIntervalMs;
  }

  /**
   * The timeout used to detect consumer failures when using Kafka's group management facility. The consumer sends
   * periodic heartbeats to indicate its liveness to the broker. If no heartbeats are received by the broker before the
   * expiration of this session timeout, then the broker will remove this consumer from the group and initiate a
   * rebalance. Note that the value must be in the allowable range as configured in the broker configuration by
   * <code>group.min.session.timeout.ms</code> and <code>group.max.session.timeout.ms</code>.
   *
   * @return the session timeout in milliseconds
   */
  public Integer getSessionTimeoutMs() {

    return this.sessionTimeoutMs;
  }

  /**
   * Set the sessionTimeoutMs for {@link #getSessionTimeoutMs()}
   *
   * @param sessionTimeoutMs the session timeout in milliseconds
   */
  public void setSessionTimeoutMs(Integer sessionTimeoutMs) {

    this.sessionTimeoutMs = sessionTimeoutMs;
  }

  /**
   * The expected time between heartbeats to the consumer coordinator when using Kafka's group management facilities.
   * Heartbeats are used to ensure that the consumer's session stays active and to facilitate rebalancing when new
   * consumers join or leave the group. The value must be set lower than <code>session.timeout.ms</code>, but typically
   * should be set no higher than 1/3 of that value. It can be adjusted even lower to control the expected time for
   * normal rebalances.
   *
   * @return the heartbeat interval time in milliseconds.
   */
  public Integer getHeartbeatIntervalMs() {

    return this.heartbeatIntervalMs;
  }

  /**
   * Set the heartbeatIntervalMs for {@link #getHeartbeatIntervalMs()}
   *
   * @param heartbeatIntervalMs the heart beat interval time in milliseconds
   */
  public void setHeartbeatIntervalMs(Integer heartbeatIntervalMs) {

    this.heartbeatIntervalMs = heartbeatIntervalMs;
  }

  /**
   * If true the consumer's offset will be periodically committed in the background.
   *
   * @return the boolean value
   */
  public Boolean getEnableAutoCommit() {

    return this.enableAutoCommit;
  }

  /**
   * Set the enableAutoCommit for {@link #getEnableAutoCommit()}
   *
   * @param enableAutoCommit a boolean value
   */
  public void setEnableAutoCommit(Boolean enableAutoCommit) {

    this.enableAutoCommit = enableAutoCommit;
  }

  /**
   * The frequency in milliseconds that the consumer offsets are auto-committed to Kafka if
   * <code>enable.auto.commit</code> is set to <code>true</code>.
   *
   * @return the auto commit interval time in milliseconds.
   */
  public Integer getAutoCommitIntervalMs() {

    return this.autoCommitIntervalMs;
  }

  /**
   * Set the autoCommitIntervalMs for {@link #getAutoCommitIntervalMs()}
   *
   * @param autoCommitIntervalMs the auto commit interval time in milliseconds.
   */
  public void setAutoCommitIntervalMs(Integer autoCommitIntervalMs) {

    this.autoCommitIntervalMs = autoCommitIntervalMs;
  }

  /**
   * The class name of the partition assignment strategy that the client will use to distribute partition ownership
   * amongst consumer instances when group management is used
   *
   * @return the partition assignment strategy
   */
  public String getPartitionAssignmentStrategy() {

    return this.partitionAssignmentStrategy;
  }

  /**
   * Set the partitionAssignmentStrategy for {@link #getPartitionAssignmentStrategy()}
   *
   * @param partitionAssignmentStrategy the partition assignment strategy.
   */
  public void setPartitionAssignmentStrategy(String partitionAssignmentStrategy) {

    this.partitionAssignmentStrategy = partitionAssignmentStrategy;
  }

  /**
   * What to do when there is no initial offset in Kafka or if the current offset does not exist any more on the server
   * (e.g. because that data has been deleted):
   * <ul>
   * <li>earliest: automatically reset the offset to the earliest offset
   * <li>latest: automatically reset the offset to the latest offset</li>
   * <li>none: throw exception to the consumer if no previous offset is found for the consumer's group</li>
   * <li>anything else: throw exception to the consumer.</li>
   * </ul>
   *
   * @return the auto offset reset
   */
  public String getAutoOffsetReset() {

    return this.autoOffsetReset;
  }

  /**
   * Set the autoOffsetReset for {@link #getAutoOffsetReset()}
   *
   * @param autoOffsetReset the auto offset reset
   */
  public void setAutoOffsetReset(String autoOffsetReset) {

    this.autoOffsetReset = autoOffsetReset;
  }

  /**
   * The minimum amount of data the server should return for a fetch request. If insufficient data is available the
   * request will wait for that much data to accumulate before answering the request. The default setting of 1 byte
   * means that fetch requests are answered as soon as a single byte of data is available or the fetch request times out
   * waiting for data to arrive. Setting this to something greater than 1 will cause the server to wait for larger
   * amounts of data to accumulate which can improve server throughput a bit at the cost of some additional latency.
   *
   * @return the minimum bytes
   */
  public Integer getFetchMinBytes() {

    return this.fetchMinBytes;
  }

  /**
   * Set the fetchMinBytes for {@link #getFetchMinBytes()}
   *
   * @param fetchMinBytes the minimum bytes
   */
  public void setFetchMinBytes(Integer fetchMinBytes) {

    this.fetchMinBytes = fetchMinBytes;
  }

  /**
   * The maximum amount of data the server should return for a fetch request. Records are fetched in batches by the
   * consumer, and if the first record batch in the first non-empty partition of the fetch is larger than this value,
   * the record batch will still be returned to ensure that the consumer can make progress. As such, this is not a
   * absolute maximum. The maximum record batch size accepted by the broker is defined via
   * <code>message.max.bytes</code> (broker config) or <code>max.message.bytes</code> (topic config). Note that the
   * consumer performs multiple fetches in parallel.
   *
   * @return the size of fetch bytes
   */
  public Integer getFetchMaxBytes() {

    return this.fetchMaxBytes;
  }

  /**
   * Set the fetchMaxBytes for {@link #getFetchMaxBytes()}
   *
   * @param fetchMaxBytes the maximum bytes to fetch.
   */
  public void setFetchMaxBytes(Integer fetchMaxBytes) {

    this.fetchMaxBytes = fetchMaxBytes;
  }

  /**
   * The maximum amount of time the server will block before answering the fetch request if there isn't sufficient data
   * to immediately satisfy the requirement given by fetch.min.bytes.
   *
   * @return the wait time in milliseconds
   */
  public Integer getFetchMaxWaitMs() {

    return this.fetchMaxWaitMs;
  }

  /**
   * Set the fetchMaxWaitMs for {@link #getFetchMaxWaitMs()}
   *
   * @param fetchMaxWaitMs the max time to wait in milliseconds
   */
  public void setFetchMaxWaitMs(Integer fetchMaxWaitMs) {

    this.fetchMaxWaitMs = fetchMaxWaitMs;
  }

  /**
   * The maximum amount of data per-partition the server will return. Records are fetched in batches by the consumer. If
   * the first record batch in the first non-empty partition of the fetch is larger than this limit, the batch will
   * still be returned to ensure that the consumer can make progress. The maximum record batch size accepted by the
   * broker is defined via <code>message.max.bytes</code> (broker config) or <code>max.message.bytes</code> (topic
   * config). See @see {@link #getFetchMaxBytes()} for limiting the consumer request size.
   *
   * @return the max partition fetch bytes.
   */
  public Integer getMaxPartitionFetchBytes() {

    return this.maxPartitionFetchBytes;
  }

  /**
   * Set the maxPartitionFetchBytes for {@link #getMaxPartitionFetchBytes()}
   *
   * @param maxPartitionFetchBytes the max partition fetch bytes.
   */
  public void setMaxPartitionFetchBytes(Integer maxPartitionFetchBytes) {

    this.maxPartitionFetchBytes = maxPartitionFetchBytes;
  }

  /**
   * Automatically check the CRC32 of the records consumed. This ensures no on-the-wire or on-disk corruption to the
   * messages occurred. This check adds some overhead, so it may be disabled in cases seeking extreme performance.
   *
   * @return the boolean
   */
  public Boolean getCheckCrcs() {

    return this.checkCrcs;
  }

  /**
   * Set the checkCrcs for {@link #getCheckCrcs()}
   *
   * @param checkCrcs a boolean value
   */
  public void setCheckCrcs(Boolean checkCrcs) {

    this.checkCrcs = checkCrcs;
  }

  /**
   * Deserializer class for key that implements the <code>org.apache.kafka.common.serialization.Deserializer</code>
   * interface.
   *
   * @return the key deserializer
   */
  public String getKeyDeserializer() {

    return this.keyDeserializer;
  }

  /**
   * Set the keyDeserializer for {@link #getKeyDeserializer()}
   *
   * @param keyDeserializer the key deserializer
   */
  public void setKeyDeserializer(String keyDeserializer) {

    this.keyDeserializer = keyDeserializer;
  }

  /**
   * Deserializer class for value that implements the <code>org.apache.kafka.common.serialization.Deserializer</code>
   * interface.
   *
   * @return the value deserializer.
   */
  public String getValueDeserializer() {

    return this.valueDeserializer;
  }

  /**
   * Set the valueDeserializer for {@link #getValueDeserializer()}
   *
   * @param valueDeserializer the value deserializer
   */
  public void setValueDeserializer(String valueDeserializer) {

    this.valueDeserializer = valueDeserializer;
  }

  /**
   * A list of classes to use as interceptors.Implementing the
   * <code>org.apache.kafka.clients.consumer.ConsumerInterceptor</code> interface allows you to intercept (and possibly
   * mutate) records received by the consumer. By default, there are no interceptors.
   *
   * @return the interceptor classes
   */
  public String getInterceptorClasses() {

    return this.interceptorClasses;
  }

  /**
   * Set the interceptorClasses for {@link #getInterceptorClasses()}
   *
   * @param interceptorClasses the interceptor classes.
   */
  public void setInterceptorClasses(String interceptorClasses) {

    this.interceptorClasses = interceptorClasses;
  }

  /**
   * Whether records from internal topics (such as offsets) should be exposed to the consumer. If set to
   * <code>true</code> the only way to receive records from an internal topic is subscribing to it.
   *
   * @return the boolean value
   */
  public Boolean getExcludeInternalTopics() {

    return this.excludeInternalTopics;
  }

  /**
   * Set the excludeInternalTopics for {@link #getExcludeInternalTopics()}
   *
   * @param excludeInternalTopics a boolean value
   */
  public void setExcludeInternalTopics(Boolean excludeInternalTopics) {

    this.excludeInternalTopics = excludeInternalTopics;
  }

  /**
   *
   * <p>
   * Controls how to read messages written transactionally. If set to <code>read_committed</code>, consumer.poll() will
   * only return transactional messages which have been committed. If set to <code>read_uncommitted</code>' (the
   * default), consumer.poll() will return all messages, even transactional messages which have been aborted.
   * Non-transactional messages will be returned unconditionally in either mode.
   * </p>
   * <p>
   * Messages will always be returned in offset order. Hence, in <code>read_committed</code> mode, consumer.poll() will
   * only return messages up to the last stable offset (LSO), which is the one less than the offset of the first open
   * transaction. In particular any messages appearing after messages belonging to ongoing transactions will be withheld
   * until the relevant transaction has been completed. As a result, <code>read_committed</code> consumers will not be
   * able to read up to the high watermark when there are in flight transactions.
   * </p>
   * <p>
   * Further, when in <code>read_committed</mode> the seekToEnd method will return the LSO"
   * </p>
   *
   * @return the isolation level.
   */
  public String getIsolationLevel() {

    return this.isolationLevel;
  }

  /**
   * Set the isolationLevel for {@link #getIsolationLevel()}.
   *
   * @param isolationLevel the isolation level
   */
  public void setIsolationLevel(String isolationLevel) {

    this.isolationLevel = isolationLevel;
  }

}
