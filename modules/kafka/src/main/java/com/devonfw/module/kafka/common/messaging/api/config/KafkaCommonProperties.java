package com.devonfw.module.kafka.common.messaging.api.config;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;

/**
 * Kafka Common Properties class which contains properties for the {@link KafkaProducer}} and {@link KafkaConsumer}}.
 *
 * @deprecated The implementation of devon4j-kafka will be abandoned. It is superseeded by Springs Kafka
 *             implementation.
 */
@Deprecated
public class KafkaCommonProperties {

  private String bootstrapServers;

  private String clientId;

  private Integer connectionsMaxIdleMs;

  private Integer metadataMaxAgeMs;

  private Integer metricsSampleWindowMs;

  private Integer metricsNumSamples;

  private String metricsRecordingLevel;

  private String metricReporters;

  private Integer receiveBufferBytes;

  private Long reconnectBackoffMs;

  private Long reconnectBackoffMaxMs;

  private Long retryBackoffMs;

  private Integer requestTimeoutMs;

  private String securityProtocol = "PLAINTEXT";

  private Integer sendBufferBytes;

  /**
   * Bootstrap Servers are a list of host/port pairs to use for establishing the initial connection to the Kafka
   * cluster.
   *
   * @return the bootstrap servers as String
   */
  public String getBootstrapServers() {

    return this.bootstrapServers;
  }

  /**
   * Set the bootstrapServers for {@link #getBootstrapServers()}.
   *
   * @param bootstrapServers the bootstrap servers needs to added as property.
   */
  public void setBootstrapServers(String bootstrapServers) {

    this.bootstrapServers = bootstrapServers;
  }

  /**
   * The period of time in milliseconds after which we force a refresh of metadata even if we haven't seen any partition
   * leadership changes to proactively discover any new brokers or partitions.
   *
   * @return the meta data max age time in milliseconds.
   */
  public Integer getMetadataMaxAgeMs() {

    return this.metadataMaxAgeMs;
  }

  /**
   * Set the metadataMaxAgeMs for {@link #getMetadataMaxAgeMs()}
   *
   * @param metadataMaxAgeMs the meta data max age time in milliseconds.
   */
  public void setMetadataMaxAgeMs(Integer metadataMaxAgeMs) {

    this.metadataMaxAgeMs = metadataMaxAgeMs;
  }

  /**
   * The size of the TCP send buffer (SO_SNDBUF) to use when sending data. If the value is -1, the OS default will be
   * used.
   *
   * @return the size of TCP send buffer.
   */
  public Integer getSendBufferBytes() {

    return this.sendBufferBytes;
  }

  /**
   *
   * Set the sendBufferBytes for {@link #getSendBufferBytes()}
   *
   * @param sendBufferBytes the size of TCP send buffer.
   */
  public void setSendBufferBytes(Integer sendBufferBytes) {

    this.sendBufferBytes = sendBufferBytes;
  }

  /**
   * The size of the TCP receive buffer (SO_RCVBUF) to use when reading data. If the value is -1, the OS default will be
   * used.
   *
   * @return the size of the receive buffer.
   */
  public Integer getReceiveBufferBytes() {

    return this.receiveBufferBytes;
  }

  /**
   * Set the receiveBufferBytes for {@link #getReceiveBufferBytes()}
   *
   * @param receiveBufferBytes the size of the receive buffer.
   */
  public void setReceiveBufferBytes(Integer receiveBufferBytes) {

    this.receiveBufferBytes = receiveBufferBytes;
  }

  /**
   * The client id is the property is used to passed with every request to the kafka. The sole purpose of this is to be
   * able to track the source of requests beyond just ip and port by allowing a logical application name to be included
   * in Kafka logs and monitoring aggregates.
   *
   * @return the client id.
   */
  public String getClientId() {

    return this.clientId;
  }

  /**
   * Set the clientId for {@link #getClientId()}.
   *
   * @param clientId the client id needs to be added as parameter.
   */
  public void setClientId(String clientId) {

    this.clientId = clientId;
  }

  /**
   * The base amount of time to wait before attempting to reconnect to a given host. This avoids repeatedly connecting
   * to a host in a tight loop. This backoff applies to all connection attempts by the client to a broker.
   *
   * @return the back off time in milliseconds.
   */
  public Long getReconnectBackoffMs() {

    return this.reconnectBackoffMs;
  }

  /**
   * Set the reconnectBackoffMs for {@link #getReconnectBackoffMs()}.
   *
   * @param reconnectBackoffMs the back off time in milliseconds.
   */
  public void setReconnectBackoffMs(Long reconnectBackoffMs) {

    this.reconnectBackoffMs = reconnectBackoffMs;
  }

  /**
   * The maximum amount of time in milliseconds to wait when reconnecting to a broker that has repeatedly failed to
   * connect. If provided, the backoff per host will increase exponentially for each consecutive connection failure, up
   * to this maximum. After calculating the backoff increase, 20% random jitter is added to avoid connection storms.
   *
   * @return the back off max time in milliseconds.
   */
  public Long getReconnectBackoffMaxMs() {

    return this.reconnectBackoffMaxMs;
  }

  /**
   * Set the reconnectBackoffMaxMs for {@link #getReconnectBackoffMaxMs()}
   *
   * @param reconnectBackoffMaxMs the back off max time in milliseconds.
   */
  public void setReconnectBackoffMaxMs(Long reconnectBackoffMaxMs) {

    this.reconnectBackoffMaxMs = reconnectBackoffMaxMs;
  }

  /**
   * The amount of time to wait before attempting to retry a failed request to a given topic partition. This avoids
   * repeatedly sending requests in a tight loop under some failure scenarios.
   *
   * @return the retry backoff time in milliseconds.
   */
  public Long getRetryBackoffMs() {

    return this.retryBackoffMs;
  }

  /**
   * Set the retryBackoffMs for {@link #getRetryBackoffMs()}.
   *
   * @param retryBackoffMs the retry backoff time in milliseconds.
   */
  public void setRetryBackoffMs(Long retryBackoffMs) {

    this.retryBackoffMs = retryBackoffMs;
  }

  /**
   * The window of time a metrics sample is computed over.
   *
   * @return the metrics sample window in milliseconds.
   */
  public Integer getMetricsSampleWindowMs() {

    return this.metricsSampleWindowMs;
  }

  /**
   * Set the metricsSampleWindowMs for {@link #getMetricsSampleWindowMs()}
   *
   * @param metricsSampleWindowMs the metrics sample window in milliseconds.
   */
  public void setMetricsSampleWindowMs(Integer metricsSampleWindowMs) {

    this.metricsSampleWindowMs = metricsSampleWindowMs;
  }

  /**
   * The number of samples maintained to compute metrics.
   *
   * @return the number of samples.
   */
  public Integer getMetricsNumSamples() {

    return this.metricsNumSamples;
  }

  /**
   * Set the metricsNumSamples for {@link #getMetricsNumSamples()}
   *
   * @param metricsNumSamples the number of samples.
   */
  public void setMetricsNumSamples(Integer metricsNumSamples) {

    this.metricsNumSamples = metricsNumSamples;
  }

  /**
   * The highest recording level for metrics.
   *
   * @return the recording level of metrics.
   */
  public String getMetricsRecordingLevel() {

    return this.metricsRecordingLevel;
  }

  /**
   * Set the metricsRecordingLevel for {@link #getMetricsRecordingLevel()}.
   *
   * @param metricsRecordingLevel the recording level of metrics.
   */
  public void setMetricsRecordingLevel(String metricsRecordingLevel) {

    this.metricsRecordingLevel = metricsRecordingLevel;
  }

  /**
   * A list of classes to use as metrics reporters. Implementing the
   * <code>org.apache.kafka.common.metrics.MetricsReporter</code> interface allows plugging in classes that will be
   * notified of new metric creation. The JmxReporter is always included to register JMX statistics.
   *
   * @return the list of reporter classes.
   */
  public String getMetricReporters() {

    return this.metricReporters;
  }

  /**
   * Set the metricReporters for {@link #getMetricReporters()}
   *
   * @param metricReporters the list of reporter classes.
   */
  public void setMetricReporters(String metricReporters) {

    this.metricReporters = metricReporters;
  }

  /**
   * Protocol used to communicate with brokers. Valid values are: Utils.join(SecurityProtocol.names(), ", ").
   *
   * @return the security protocol.
   */
  public String getSecurityProtocol() {

    return this.securityProtocol;
  }

  /**
   * Set the securityProtocol for {@link #getSecurityProtocol()}
   *
   * @param securityProtocol the security protocol.
   */
  public void setSecurityProtocol(String securityProtocol) {

    this.securityProtocol = securityProtocol;
  }

  /**
   * Close idle connections after the number of milliseconds specified by this config.
   *
   * @return the maximum idle time in milliseconds.
   */
  public Integer getConnectionsMaxIdleMs() {

    return this.connectionsMaxIdleMs;
  }

  /**
   *
   * Set the connectionsMaxIdleMs for {@link #getConnectionsMaxIdleMs()}
   *
   * @param connectionsMaxIdleMs the max idle time in milliseconds.
   */
  public void setConnectionsMaxIdleMs(Integer connectionsMaxIdleMs) {

    this.connectionsMaxIdleMs = connectionsMaxIdleMs;
  }

  /**
   * The configuration controls the maximum amount of time the client will wait for the response of a request. If the
   * response is not received before the timeout elapses the client will resend the request if necessary or fail the
   * request if retries are exhausted.
   *
   * @return the request timeout in milliseconds.
   */
  public Integer getRequestTimeoutMs() {

    return this.requestTimeoutMs;
  }

  /**
   * Set the requestTimeoutMs for {@link #getRequestTimeoutMs()}
   *
   * @param requestTimeoutMs the request timeout in milliseconds.
   */
  public void setRequestTimeoutMs(Integer requestTimeoutMs) {

    this.requestTimeoutMs = requestTimeoutMs;
  }

}
