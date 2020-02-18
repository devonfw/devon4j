package com.devonfw.module.kafka.common.messaging.api.config;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;

/**
 * Kafka Common Properties class which contains properties for the {@link KafkaProducer}} and {@link KafkaConsumer}}.
 */
public class KafkaCommonProperties {

  /**
   * The Bootstrap servers.
   *
   * @see #getBootstrapServers()
   * @see #setBootstrapServers(String)
   */
  private String bootstrapServers;

  /**
   * The client id.
   *
   * @see #getClientId()
   * @see #setClientId(String)
   */
  private String clientId;

  /**
   * The connection max idle time in milliseconds.
   *
   * @see #setConnectionsMaxIdleMs(Integer)
   * @see #getConnectionsMaxIdleMs()
   */
  private Integer connectionsMaxIdleMs;

  /**
   * The meta data max age time in milliseconds.
   *
   * @see #getMetadataMaxAgeMs()
   * @see #setMetadataMaxAgeMs(Integer)
   */
  private Integer metadataMaxAgeMs;

  /**
   * The metrics sample window time in milliseconds.
   *
   * @see #getMetricsSampleWindowMs()
   * @see #setMetricsSampleWindowMs(Integer)
   */
  private Integer metricsSampleWindowMs;

  /**
   * The number of samples maintained to compute metrics.
   *
   * @see #getMetricsNumSamples()
   * @see #setMetricsNumSamples(Integer)
   */
  private Integer metricsNumSamples;

  /**
   * The metrics recording level.
   *
   * @see #getMetricsRecordingLevel()
   * @see #setMetricsRecordingLevel(String)
   */
  private String metricsRecordingLevel;

  /**
   * The metric Reporters.
   *
   * @see #getMetricReporters()
   * @see #setMetricReporters(String)
   */
  private String metricReporters;

  // private String password;

  /**
   * The size of receive buffer bytes.
   *
   * @see #getReceiveBufferBytes()
   * @see #setReceiveBufferBytes(Integer)
   */
  private Integer receiveBufferBytes;

  /**
   * The reconnect back off time in milliseconds.
   *
   * @see #getReconnectBackoffMs()
   * @see #setReconnectBackoffMs(Long)
   */
  private Long reconnectBackoffMs;

  /**
   * The reconnect back off max time i milliseconds.
   *
   * @see #getReconnectBackoffMaxMs()
   * @see #setReconnectBackoffMaxMs(Long)
   */
  private Long reconnectBackoffMaxMs;

  /**
   * The retry back off time in milliseconds.
   *
   * @see #getRetryBackoffMs()
   * @see #setRetryBackoffMs(Long)
   */
  private Long retryBackoffMs;

  /**
   * The request timeout in milliseconds.
   *
   * @see #getRequestTimeoutMs()
   * @see #setRequestTimeoutMs(Integer)
   */
  private Integer requestTimeoutMs;

  /**
   * The security protocol.
   *
   * @see #getSecurityProtocol()
   * @see #setSecurityProtocol(String)
   */
  private String securityProtocol = "PLAINTEXT";

  /**
   * The size of the TCp send buffer.
   *
   * @see #getSendBufferBytes()
   * @see #setSendBufferBytes(Integer)
   */
  private Integer sendBufferBytes;

  // private String sslKeystoreLocation;
  //
  // private String sslKeystorePassword;
  //
  // private String sslKeyPassword;
  //
  // private String sslTruststoreLocation;
  //
  // private String sslTruststorePassword;
  //
  // private String saslMechanism = "PLAIN";

  // private String username;

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
   * Bootstrap Servers are a list of host/port pairs to use for establishing the initial connection to the Kafka
   * cluster.
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
   * The period of time in milliseconds after which we force a refresh of metadata even if we haven't seen any partition
   * leadership changes to proactively discover any new brokers or partitions.
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
   * The size of the TCP send buffer (SO_SNDBUF) to use when sending data. If the value is -1, the OS default will be
   * used.
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
   * The size of the TCP receive buffer (SO_RCVBUF) to use when reading data. If the value is -1, the OS default will be
   * used.
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
   * The client id is the property is used to passed with every request to the kafka. The sole purpose of this is to be
   * able to track the source of requests beyond just ip and port by allowing a logical application name to be included
   * in Kafka logs and monitoring aggregates.
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
   * The base amount of time to wait before attempting to reconnect to a given host. This avoids repeatedly connecting
   * to a host in a tight loop. This backoff applies to all connection attempts by the client to a broker.
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
   * The maximum amount of time in milliseconds to wait when reconnecting to a broker that has repeatedly failed to
   * connect. If provided, the backoff per host will increase exponentially for each consecutive connection failure, up
   * to this maximum. After calculating the backoff increase, 20% random jitter is added to avoid connection storms.
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
   * The amount of time to wait before attempting to retry a failed request to a given topic partition. This avoids
   * repeatedly sending requests in a tight loop under some failure scenarios.
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
   * The window of time a metrics sample is computed over.
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
   * The number of samples maintained to compute metrics.
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
   * The highest recording level for metrics.
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
   * A list of classes to use as metrics reporters. Implementing the
   * <code>org.apache.kafka.common.metrics.MetricsReporter</code> interface allows plugging in classes that will be
   * notified of new metric creation. The JmxReporter is always included to register JMX statistics.
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
   * Protocol used to communicate with brokers. Valid values are: Utils.join(SecurityProtocol.names(), ", ").
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
   * Close idle connections after the number of milliseconds specified by this config.
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
   * The configuration controls the maximum amount of time the client will wait for the response of a request. If the
   * response is not received before the timeout elapses the client will resend the request if necessary or fail the
   * request if retries are exhausted.
   *
   * @param requestTimeoutMs the request timeout in milliseconds.
   */
  public void setRequestTimeoutMs(Integer requestTimeoutMs) {

    this.requestTimeoutMs = requestTimeoutMs;
  }

}
