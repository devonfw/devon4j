package com.devonfw.module.kafka.common.messaging.api.config;

/**
 * @author ravicm
 *
 */
public class KafkaProducerProperties {

  /**
   * The acks
   *
   * @see #getAcks()
   * @see #setAcks(String)
   */
  private String acks;

  /**
   * The size of the batch.
   *
   * @see #getBatchSize()
   * @see #setBatchSize(Integer)
   */
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
   * Set the number of acknowledgments the producer requires the leader to have received before considering a request
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
   * @param acks the acks
   */
  public void setAcks(String acks) {

    this.acks = acks;
  }

  public Integer getLingerMs() {

    return this.lingerMs;
  }

  public void setLingerMs(Integer lingerMs) {

    this.lingerMs = lingerMs;
  }

  public Integer getMaxRequestSize() {

    return this.maxRequestSize;
  }

  public void setMaxRequestSize(Integer maxRequestSize) {

    this.maxRequestSize = maxRequestSize;
  }

  public Integer getMaxBlockMs() {

    return this.maxBlockMs;
  }

  public void setMaxBlockMs(Integer maxBlockMs) {

    this.maxBlockMs = maxBlockMs;
  }

  public Integer getBufferMemory() {

    return this.bufferMemory;
  }

  public void setBufferMemory(Integer bufferMemory) {

    this.bufferMemory = bufferMemory;
  }

  public String getCompressionType() {

    return this.compressionType;
  }

  public void setCompressionType(String compressionType) {

    this.compressionType = compressionType;
  }

  public Integer getMaxInFlightRequestsPerConnection() {

    return this.maxInFlightRequestsPerConnection;
  }

  public void setMaxInFlightRequestsPerConnection(Integer maxInFlightRequestsPerConnection) {

    this.maxInFlightRequestsPerConnection = maxInFlightRequestsPerConnection;
  }

  public Integer getRetries() {

    return this.retries;
  }

  public void setRetries(Integer retries) {

    this.retries = retries;
  }

  public String getKeySerializer() {

    return this.keySerializer;
  }

  public void setKeySerializer(String keySerializer) {

    this.keySerializer = keySerializer;
  }

  public String getValueSerializer() {

    return this.valueSerializer;
  }

  public void setValueSerializer(String valueSerializer) {

    this.valueSerializer = valueSerializer;
  }

  public String getPartitionerClass() {

    return this.partitionerClass;
  }

  public void setPartitionerClass(String partitionerClass) {

    this.partitionerClass = partitionerClass;
  }

  public String getInterceptorClasses() {

    return this.interceptorClasses;
  }

  public void setInterceptorClasses(String interceptorClasses) {

    this.interceptorClasses = interceptorClasses;
  }

  public Boolean getEnableIdempotence() {

    return this.enableIdempotence;
  }

  public void setEnableIdempotence(Boolean enableIdempotence) {

    this.enableIdempotence = enableIdempotence;
  }

  public int getTransactionTimeoutMs() {

    return this.transactionTimeoutMs;
  }

  public void setTransactionTimeoutMs(int transactionTimeoutMs) {

    this.transactionTimeoutMs = transactionTimeoutMs;
  }

  public String getTransactionalId() {

    return this.transactionalId;
  }

  public void setTransactionalId(String transactionalId) {

    this.transactionalId = transactionalId;
  }

}
