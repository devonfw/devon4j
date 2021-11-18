package com.devonfw.module.kafka.common.messaging.api.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.kafka.core.ProducerFactory;

/**
 * This is a property class to create configuration for for {@link MessageSenderConfig} by setting parameter for
 * {@link ProducerConfig} to create {@link ProducerFactory}.
 *
 * @deprecated The implementation of devon4j-kafka will be abandoned. It is superseeded by Springs Kafka
 *             implementation.
 */
@Deprecated
public class KafkaProducerProperties {

  private String acks;

  private Integer batchSize;

  private Integer bufferMemory;

  private String compressionType;

  private Boolean enableIdempotence;

  private String interceptorClasses;

  private String keySerializer;

  private Integer lingerMs;

  private Integer maxRequestSize;

  private Integer maxBlockMs;

  private Integer maxInFlightRequestsPerConnection;

  private String partitionerClass;

  private Integer retries;

  private int transactionTimeoutMs;

  private String transactionalId;

  private String valueSerializer;

  /**
   * The producer will attempt to batch records together into fewer requests whenever multiple records are being sent to
   * the same partition. This helps performance on both the client and the server. This configuration controls the
   * default batch size in bytes.
   * <p>
   * No attempt will be made to batch records larger than this size.
   * <p>
   * Requests sent to brokers will contain multiple batches, one for each partition with data available to be sent.
   *
   * <p>
   * A small batch size will make batching less common and may reduce throughput (a batch size of zero will disable
   * batching entirely). A very large batch size may use memory a bit more wastefully as we will always allocate a
   * buffer of the specified batch size in anticipation of additional records.
   *
   * @return the batch size
   */
  public Integer getBatchSize() {

    return this.batchSize;
  }

  /**
   * Set the batch size for {@link #getBatchSize()}.
   *
   * @param batchSize the batch size
   */
  public void setBatchSize(Integer batchSize) {

    this.batchSize = batchSize;
  }

  /**
   * The number of acknowledgments the producer requires the leader to have received before considering a request
   * complete. This controls the durability of records that are sent. The following settings are allowed:
   * <ul>
   *
   * <li><code>acks=0</code> If set to zero then the producer will not wait for any acknowledgment from the server at
   * all. The record will be immediately added to the socket buffer and considered sent. No guarantee can be made that
   * the server has received the record in this case, and the <code>retries</code> configuration will not take effect
   * (as the client won't generally know of any failures). The offset given back for each record will always be set to
   * -1.
   * <li><code>acks=1</code> This will mean the leader will write the record to its local log but will respond without
   * awaiting full acknowledgement from all followers. In this case should the leader fail immediately after
   * acknowledging the record but before the followers have replicated it then the record will be lost.
   * <li><code>acks=all</code> This means the leader will wait for the full set of in-sync replicas to acknowledge the
   * record. This guarantees that the record will not be lost as long as at least one in-sync replica remains alive.
   * This is the strongest available guarantee. This is equivalent to the acks=-1 setting.
   *
   * @return the acks
   */
  public String getAcks() {

    return this.acks;
  }

  /**
   * Set the acks for {@link #getAcks()}.
   *
   * @param acks the acks
   */
  public void setAcks(String acks) {

    this.acks = acks;
  }

  /**
   * The producer groups together any records that arrive in between request transmissions into a single batched
   * request. Normally this occurs only under load when records arrive faster than they can be sent out. However in some
   * circumstances the client may want to reduce the number of requests even under moderate load. This setting
   * accomplishes this by adding a small amount of artificial delay&mdash;that is, rather than immediately sending out a
   * record the producer will wait for up to the given delay to allow other records to be sent so that the sends can be
   * batched together. This can be thought of as analogous to Nagle's algorithm in TCP. This setting gives the upper
   * bound on the delay for batching: once we get <code> BATCH_SIZE_CONFIG </code> worth of records for a partition it
   * will be sent immediately regardless of this setting, however if we have fewer than this many bytes accumulated for
   * this partition we will 'linger' for the specified time waiting for more records to show up. This setting defaults
   * to 0 (i.e. no delay). Setting <code> LINGER_MS_CONFIG =5</code>, for example, would have the effect of reducing the
   * number of requests sent but would add up to 5ms of latency to records sent in the absence of load.
   *
   * @return the linger.ms
   */
  public Integer getLingerMs() {

    return this.lingerMs;
  }

  /**
   * Set the linger.ms for {@link #getLingerMs()}.
   *
   * @param lingerMs the Integer linger.ms
   */
  public void setLingerMs(Integer lingerMs) {

    this.lingerMs = lingerMs;
  }

  /**
   * The maximum size of a request in bytes. This setting will limit the number of record batches the producer will send
   * in a single request to avoid sending huge requests. This is also effectively a cap on the maximum record batch
   * size. Note that the server has its own cap on record batch size which may be different from this.
   *
   * @return the max request size.
   */
  public Integer getMaxRequestSize() {

    return this.maxRequestSize;
  }

  /**
   * Set the max request size for {@link #getMaxRequestSize()}
   *
   * @param maxRequestSize the max request size
   */
  public void setMaxRequestSize(Integer maxRequestSize) {

    this.maxRequestSize = maxRequestSize;
  }

  /**
   * The configuration controls how long <code>KafkaProducer.send()</code> and
   * <code>KafkaProducer.partitionsFor()</code> will block. These methods can be blocked either because the buffer is
   * full or metadata unavailable. Blocking in the user-supplied serializers or partitioner will not be counted against
   * this timeout.
   *
   * @return the maxBlockMs.
   */
  public Integer getMaxBlockMs() {

    return this.maxBlockMs;
  }

  /**
   * Set the maxBlockMs for {@link #getMaxBlockMs()}
   *
   * @param maxBlockMs the maxBlockMs.
   */
  public void setMaxBlockMs(Integer maxBlockMs) {

    this.maxBlockMs = maxBlockMs;
  }

  /**
   * The total bytes of memory the producer can use to buffer records waiting to be sent to the server. If records are
   * sent faster than they can be delivered to the server the producer will block for <code> MAX_BLOCK_MS_CONFIG </code>
   * after which it will throw an exception.
   * <p>
   * This setting should correspond roughly to the total memory the producer will use, but is not a hard bound since not
   * all memory the producer uses is used for buffering. Some additional memory will be used for compression (if
   * compression is enabled) as well as for maintaining in-flight requests.
   *
   * @return the bufferMemory.
   */
  public Integer getBufferMemory() {

    return this.bufferMemory;
  }

  /**
   * Set the bufferMemory for {@link #getBufferMemory()}
   *
   * @param bufferMemory the bufferMemory.
   */
  public void setBufferMemory(Integer bufferMemory) {

    this.bufferMemory = bufferMemory;
  }

  /**
   * The compression type for all data generated by the producer. The default is none (i.e. no compression). Valid
   * values are <code>none</code>, <code>gzip</code>, <code>snappy</code>, or <code>lz4</code>. Compression is of full
   * batches of data, so the efficacy of batching will also impact the compression ratio (more batching means better
   * compression).
   *
   * @return the compressionType
   */
  public String getCompressionType() {

    return this.compressionType;
  }

  /**
   * Set the compressionType for {@link #getCompressionType()}.
   *
   * @param compressionType the compressionType.
   */
  public void setCompressionType(String compressionType) {

    this.compressionType = compressionType;
  }

  /**
   * The maximum number of unacknowledged requests the client will send on a single connection before blocking. Note
   * that if this setting is set to be greater than 1 and there are failed sends, there is a risk of message re-ordering
   * due to retries (i.e., if retries are enabled).
   *
   * @return the maxInFlightRequestsPerConnection
   */
  public Integer getMaxInFlightRequestsPerConnection() {

    return this.maxInFlightRequestsPerConnection;
  }

  /**
   * Set the maxInFlightRequestsPerConnection for {@link #getMaxInFlightRequestsPerConnection()}.
   *
   * @param maxInFlightRequestsPerConnection the maxInFlightRequestsPerConnection.
   */
  public void setMaxInFlightRequestsPerConnection(Integer maxInFlightRequestsPerConnection) {

    this.maxInFlightRequestsPerConnection = maxInFlightRequestsPerConnection;
  }

  /**
   * Setting a value greater than zero will cause the client to resend any record whose send fails with a potentially
   * transient error. Note that this retry is no different than if the client resent the record upon receiving the
   * error. Allowing retries without setting <code> MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION </code> to 1 will potentially
   * change the ordering of records because if two batches are sent to a single partition, and the first fails and is
   * retried but the second succeeds, then the records in the second batch may appear first.
   *
   * @return retries
   */
  public Integer getRetries() {

    return this.retries;
  }

  /**
   * Set the retries for {@link #getRetries()}.
   *
   * @param retries the retries.
   */
  public void setRetries(Integer retries) {

    this.retries = retries;
  }

  /**
   * Serializer class for key that implements the <code>org.apache.kafka.common.serialization.Serializer</code>
   * interface.
   *
   * @return the keySerializer.
   */
  public String getKeySerializer() {

    return this.keySerializer;
  }

  /**
   * set the keySerializer for {@link #getKeySerializer()}
   *
   * @param keySerializer the keySerializer.
   */
  public void setKeySerializer(String keySerializer) {

    this.keySerializer = keySerializer;
  }

  /**
   * Serializer class for value that implements the <code>org.apache.kafka.common.serialization.Serializer</code>
   * interface..
   *
   * @return the valueSerializer.
   */
  public String getValueSerializer() {

    return this.valueSerializer;
  }

  /**
   * set the valueSerializer for {@link #getValueSerializer()}.
   *
   * @param valueSerializer the valueSerializer.
   */
  public void setValueSerializer(String valueSerializer) {

    this.valueSerializer = valueSerializer;
  }

  /**
   * Partitioner class that implements the <code>org.apache.kafka.clients.producer.Partitioner</code> interface.
   *
   * @return partitionerClass.
   */
  public String getPartitionerClass() {

    return this.partitionerClass;
  }

  /**
   * Set the partitionerClass for {@link #getPartitionerClass()}.
   *
   * @param partitionerClass the partitionerClass
   */
  public void setPartitionerClass(String partitionerClass) {

    this.partitionerClass = partitionerClass;
  }

  /**
   * A list of classes to use as interceptors. Implementing the
   * <code>org.apache.kafka.clients.producer.ProducerInterceptor</code> interface allows you to intercept (and possibly
   * mutate) the records received by the producer before they are published to the Kafka cluster. By default, there are
   * no interceptors.
   *
   * @return the interceptorClasses
   */
  public String getInterceptorClasses() {

    return this.interceptorClasses;
  }

  /**
   * Set the interceptorClasses for {@link #getInterceptorClasses()}
   *
   * @param interceptorClasses the interceptorClasses.
   */
  public void setInterceptorClasses(String interceptorClasses) {

    this.interceptorClasses = interceptorClasses;
  }

  /**
   * When set to 'true', the producer will ensure that exactly one copy of each message is written in the stream. If
   * 'false', producer retries due to broker failures, etc., may write duplicates of the retried message in the stream.
   * Note that enabling idempotence requires <code> MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION </code> to be less than or
   * equal to 5, <code> RETRIES_CONFIG </code> to be greater than 0 and ACKS_CONFIG must be 'all'. If these values are
   * not explicitly set by the user, suitable values will be chosen. If incompatible values are set, a ConfigException
   * will be thrown.
   *
   * @return the enableIdempotence.
   */
  public Boolean getEnableIdempotence() {

    return this.enableIdempotence;
  }

  /**
   * Set the enableIdempotence for {@link #getEnableIdempotence()}.
   *
   * @param enableIdempotence the enableIdempotence.
   */
  public void setEnableIdempotence(Boolean enableIdempotence) {

    this.enableIdempotence = enableIdempotence;
  }

  /**
   * The maximum amount of time in ms that the transaction coordinator will wait for a transaction status update from
   * the producer before proactively aborting the ongoing transaction. If this value is larger than the
   * transaction.max.timeout.ms setting in the broker, the request will fail with a `InvalidTransactionTimeout` error.
   *
   * @return the transactionTimeoutMs.
   */
  public int getTransactionTimeoutMs() {

    return this.transactionTimeoutMs;
  }

  /**
   * Set the transactionTimeoutMs for {@link #getTransactionTimeoutMs()}
   *
   * @param transactionTimeoutMs the transactionTimeoutMs.
   */
  public void setTransactionTimeoutMs(int transactionTimeoutMs) {

    this.transactionTimeoutMs = transactionTimeoutMs;
  }

  /**
   * The TransactionalId to use for transactional delivery. This enables reliability semantics which span multiple
   * producer sessions since it allows the client to guarantee that transactions using the same TransactionalId have
   * been completed prior to starting any new transactions. If no TransactionalId is provided, then the producer is
   * limited to idempotent delivery. Note that enable.idempotence must be enabled if a TransactionalId is configured.
   * The default is <code>null</code>, which means transactions cannot be used. Note that transactions requires a
   * cluster of at least three brokers by default what is the recommended setting for production; for development you
   * can change this, by adjusting broker setting `transaction.state.log.replication.factor`.
   *
   * @return the transactionalId.
   */
  public String getTransactionalId() {

    return this.transactionalId;
  }

  /**
   * Set the transactionalId for {@link #getTransactionalId()}.
   *
   * @param transactionalId the transactionalId.
   */
  public void setTransactionalId(String transactionalId) {

    this.transactionalId = transactionalId;
  }

}
