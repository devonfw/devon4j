package com.devonfw.module.kafka.common.messaging.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

import com.devonfw.module.kafka.common.messaging.api.config.KafkaCommonProperties;
import com.devonfw.module.kafka.common.messaging.api.config.KafkaConsumerProperties;
import com.devonfw.module.kafka.common.messaging.api.config.KafkaProducerProperties;

/**
 * A property mapping class used to map the properties to {@link ProducerConfig} and {@link ConsumerConfig}.
 *
 */
public class KafkaPropertyMapper {

  /**
   * This method used to set properties from {@link KafkaCommonProperties} and {@link KafkaConsumerProperties} to
   * {@link ConsumerConfig}
   *
   * @param commonProperties the {@link KafkaCommonProperties}
   * @param consumerProperties the {@link KafkaConsumerProperties}
   * @return the {@link Map}
   */
  public Map<String, Object> consumerProperties(KafkaCommonProperties commonProperties,
      KafkaConsumerProperties consumerProperties) {

    Map<String, Object> props = new HashMap<>();
    setConfigValueIfAvailable(props, ConsumerConfig.GROUP_ID_CONFIG, consumerProperties.getGroupId());
    setConfigValueIfAvailable(props, ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumerProperties.getMaxPollRecords());
    setConfigValueIfAvailable(props, ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,
        consumerProperties.getMaxPollIntervalMs());
    setConfigValueIfAvailable(props, ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,
        consumerProperties.getSessionTimeoutMs());
    setConfigValueIfAvailable(props, ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG,
        consumerProperties.getHeartbeatIntervalMs());
    setConfigValueIfAvailable(props, ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, commonProperties.getBootstrapServers());
    setConfigValueIfAvailable(props, ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
        consumerProperties.getEnableAutoCommit());
    setConfigValueIfAvailable(props, ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,
        consumerProperties.getAutoCommitIntervalMs());
    setConfigValueIfAvailable(props, ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,
        consumerProperties.getPartitionAssignmentStrategy());
    setConfigValueIfAvailable(props, ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerProperties.getAutoOffsetReset());
    setConfigValueIfAvailable(props, ConsumerConfig.FETCH_MIN_BYTES_CONFIG, consumerProperties.getFetchMinBytes());
    setConfigValueIfAvailable(props, ConsumerConfig.FETCH_MAX_BYTES_CONFIG, consumerProperties.getFetchMaxBytes());
    setConfigValueIfAvailable(props, ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, consumerProperties.getFetchMaxWaitMs());
    setConfigValueIfAvailable(props, ConsumerConfig.METADATA_MAX_AGE_CONFIG, commonProperties.getMetadataMaxAgeMs());
    setConfigValueIfAvailable(props, ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,
        consumerProperties.getMaxPartitionFetchBytes());
    setConfigValueIfAvailable(props, ConsumerConfig.SEND_BUFFER_CONFIG, commonProperties.getSendBufferBytes());
    setConfigValueIfAvailable(props, ConsumerConfig.RECEIVE_BUFFER_CONFIG, commonProperties.getReceiveBufferBytes());
    setConfigValueIfAvailable(props, ConsumerConfig.CLIENT_ID_CONFIG, commonProperties.getClientId());
    setConfigValueIfAvailable(props, ConsumerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG,
        commonProperties.getReconnectBackoffMaxMs());
    setConfigValueIfAvailable(props, ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG,
        commonProperties.getReconnectBackoffMs());
    setConfigValueIfAvailable(props, ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, commonProperties.getRetryBackoffMs());
    setConfigValueIfAvailable(props, ConsumerConfig.METRICS_SAMPLE_WINDOW_MS_CONFIG,
        commonProperties.getMetricsSampleWindowMs());
    setConfigValueIfAvailable(props, ConsumerConfig.METRICS_NUM_SAMPLES_CONFIG,
        commonProperties.getMetricsNumSamples());
    setConfigValueIfAvailable(props, ConsumerConfig.METRICS_RECORDING_LEVEL_CONFIG,
        commonProperties.getMetricsRecordingLevel());
    setConfigValueIfAvailable(props, ConsumerConfig.METRIC_REPORTER_CLASSES_CONFIG,
        commonProperties.getMetricReporters());
    setConfigValueIfAvailable(props, ConsumerConfig.CHECK_CRCS_CONFIG, consumerProperties.getCheckCrcs());
    setConfigValueIfAvailable(props, ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG,
        commonProperties.getConnectionsMaxIdleMs());
    setConfigValueIfAvailable(props, ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, commonProperties.getRequestTimeoutMs());
    setConfigValueIfAvailable(props, ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG,
        consumerProperties.getInterceptorClasses());
    setConfigValueIfAvailable(props, ConsumerConfig.EXCLUDE_INTERNAL_TOPICS_CONFIG,
        consumerProperties.getExcludeInternalTopics());
    setConfigValueIfAvailable(props, ConsumerConfig.ISOLATION_LEVEL_CONFIG, consumerProperties.getIsolationLevel());

    String valueDeserializer = Optional.ofNullable(consumerProperties.getValueDeserializer())
        .orElse("org.apache.kafka.common.serialization.StringDeserializer");

    String keyDeserializer = Optional.ofNullable(consumerProperties.getKeyDeserializer())
        .orElse("org.apache.kafka.common.serialization.StringDeserializer");

    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, keyDeserializer);

    return props;
  }

  /**
   * This method is used to create Producer properties from {@link KafkaCommonProperties} and
   * {@link KafkaProducerProperties}.
   *
   * @param commonProperties the {@link KafkaCommonProperties}
   * @param producerProperties the {@link KafkaProducerProperties}
   * @return the {@link Map}
   */
  public Map<String, Object> producerProperties(KafkaCommonProperties commonProperties,
      KafkaProducerProperties producerProperties) {

    Map<String, Object> props = new HashMap<>();

    setConfigValueIfAvailable(props, ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, commonProperties.getBootstrapServers());
    setConfigValueIfAvailable(props, ProducerConfig.METADATA_MAX_AGE_CONFIG, commonProperties.getMetadataMaxAgeMs());
    setConfigValueIfAvailable(props, ProducerConfig.BATCH_SIZE_CONFIG, producerProperties.getBatchSize());
    setConfigValueIfAvailable(props, ProducerConfig.ACKS_CONFIG, producerProperties.getAcks());
    setConfigValueIfAvailable(props, ProducerConfig.LINGER_MS_CONFIG, producerProperties.getLingerMs());
    setConfigValueIfAvailable(props, ProducerConfig.CLIENT_ID_CONFIG, commonProperties.getClientId());
    setConfigValueIfAvailable(props, ProducerConfig.SEND_BUFFER_CONFIG, commonProperties.getSendBufferBytes());
    setConfigValueIfAvailable(props, ProducerConfig.RECEIVE_BUFFER_CONFIG, commonProperties.getReceiveBufferBytes());
    setConfigValueIfAvailable(props, ProducerConfig.MAX_REQUEST_SIZE_CONFIG, producerProperties.getMaxRequestSize());
    setConfigValueIfAvailable(props, ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG,
        commonProperties.getReconnectBackoffMs());
    setConfigValueIfAvailable(props, ProducerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG,
        commonProperties.getReconnectBackoffMaxMs());
    setConfigValueIfAvailable(props, ProducerConfig.MAX_BLOCK_MS_CONFIG, producerProperties.getMaxBlockMs());
    setConfigValueIfAvailable(props, ProducerConfig.BUFFER_MEMORY_CONFIG, producerProperties.getBufferMemory());
    setConfigValueIfAvailable(props, ProducerConfig.RETRY_BACKOFF_MS_CONFIG, commonProperties.getRetryBackoffMs());
    setConfigValueIfAvailable(props, ProducerConfig.COMPRESSION_TYPE_CONFIG, producerProperties.getCompressionType());
    setConfigValueIfAvailable(props, ProducerConfig.METRICS_SAMPLE_WINDOW_MS_CONFIG,
        commonProperties.getMetricsSampleWindowMs());
    setConfigValueIfAvailable(props, ProducerConfig.METRICS_NUM_SAMPLES_CONFIG,
        commonProperties.getMetricsNumSamples());
    setConfigValueIfAvailable(props, ProducerConfig.METRICS_RECORDING_LEVEL_CONFIG,
        commonProperties.getMetricsRecordingLevel());
    setConfigValueIfAvailable(props, ProducerConfig.METRIC_REPORTER_CLASSES_CONFIG,
        commonProperties.getMetricReporters());
    setConfigValueIfAvailable(props, ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,
        producerProperties.getMaxInFlightRequestsPerConnection());
    setConfigValueIfAvailable(props, ProducerConfig.RETRIES_CONFIG, producerProperties.getRetries());
    setConfigValueIfAvailable(props, ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG,
        commonProperties.getConnectionsMaxIdleMs());
    setConfigValueIfAvailable(props, ProducerConfig.PARTITIONER_CLASS_CONFIG, producerProperties.getPartitionerClass());
    setConfigValueIfAvailable(props, ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, commonProperties.getRequestTimeoutMs());
    setConfigValueIfAvailable(props, ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,
        producerProperties.getInterceptorClasses());
    setConfigValueIfAvailable(props, ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,
        producerProperties.getEnableIdempotence());
    setConfigValueIfAvailable(props, ProducerConfig.TRANSACTION_TIMEOUT_CONFIG,
        producerProperties.getTransactionTimeoutMs());
    setConfigValueIfAvailable(props, ProducerConfig.TRANSACTIONAL_ID_CONFIG, producerProperties.getTransactionalId());

    String keySerializer = Optional.ofNullable(producerProperties.getKeySerializer())
        .orElse("org.apache.kafka.common.serialization.StringSerializer");

    String valueSerializer = Optional.ofNullable(producerProperties.getValueSerializer())
        .orElse("org.apache.kafka.common.serialization.StringSerializer");

    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);

    return props;
  }

  private void setConfigValueIfAvailable(Map<String, Object> configMap, String configKey, Object configValue) {

    if (configValue != null) {
      configMap.put(configKey, configValue);
    }
  }

}
