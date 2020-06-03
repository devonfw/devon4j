package com.devonfw.module.kafka.common.messaging.retry.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageRetryPolicy;
import com.devonfw.module.kafka.common.messaging.retry.api.config.DefaultRetryPolicyProperties;
import com.devonfw.module.kafka.common.messaging.retry.util.MessageRetryUtils;

/**
 * This is an implementation class for the {@link MessageRetryPolicy}
 *
 * @param <K> the key type
 * @param <V> the value type
 *
 */
public class DefaultRetryPolicy<K, V> implements MessageRetryPolicy<K, V> {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultRetryPolicy.class);

  private BinaryExceptionClassifier retryableClassifier = new BinaryExceptionClassifier(false);

  private DefaultRetryPolicyProperties properties;

  /**
   * The constructor.
   *
   * @param properties the {@link DefaultRetryPolicyProperties}
   */
  public DefaultRetryPolicy(DefaultRetryPolicyProperties properties) {

    Optional.ofNullable(properties).ifPresent(this::checkProperties);

    this.properties = properties;

  }

  private void checkProperties(DefaultRetryPolicyProperties retryProperties) {

    retryProperties.getRetryPeriod().forEach((key, value) -> {
      if (value < 0) {
        throw new IllegalArgumentException("The property \\\"retry-period\\\" must be> 0.");
      }
    });

    if (CollectionUtils.isEmpty(retryProperties.getRetryableExceptions())) {
      LOG.info("The property \\\\\\\"retryable-exceptions\\\\\\\" is empty. No retries will be performed.");
    }
  }

  @Override
  public boolean canRetry(ConsumerRecord<K, V> consumerRecord, MessageRetryContext retryContext, Exception ex) {

    if (ObjectUtils.isEmpty(consumerRecord)) {
      throw new IllegalArgumentException("The \"consumerRecord \" parameter cannot be null.");
    }

    if (ObjectUtils.isEmpty(ex)) {
      throw new IllegalArgumentException("The \"ex \" parameter cannot be null.");
    }

    LOG.info("proceeding with retry for the message {} and due to {}", consumerRecord.value().toString(),
        ex.getMessage());

    String topic = consumerRecord.topic();

    setTypeMapAndTraverseCauseForBinaryExceptionClassifier(topic, this.properties);

    long retryCount = getRetryCountForTopic(topic, this.properties);

    if (retryContext != null && retryContext.getRetryUntil() != null
        && retryContext.getCurrentRetryCount() < retryCount) {
      return canRetry(retryContext, ex);
    }

    return this.retryableClassifier.classify(ex);
  }

  private boolean canRetry(MessageRetryContext retryContext, Exception ex) {

    String now = Instant.now().toString();

    if (now.compareTo(retryContext.getRetryUntil().toString()) >= 0) {
      return false;
    }
    return this.retryableClassifier.classify(ex);
  }

  @Override
  public Instant getRetryUntilTimestamp(ConsumerRecord<K, V> consumerRecord, MessageRetryContext retryContext) {

    long retryPeriod = getretryPeriodForTopic(consumerRecord.topic(), this.properties);

    return Instant.now().plusMillis(retryPeriod * 1000);
  }

  /**
   * The number of times to execute the Retry.
   *
   * @return retryCount
   */
  @Override
  public long getRetryCount(String topic) {

    return getRetryCountForTopic(topic, this.properties);
  }

  private long getretryPeriodForTopic(String topic, DefaultRetryPolicyProperties retryProperties) {

    return Optional.ofNullable(retryProperties.getRetryPeriod().get(topic))
        .orElse(retryProperties.getRetryPeriodDefault());
  }

  private boolean getRetryableTraverseCausesForTopic(String topic, DefaultRetryPolicyProperties retryProperties) {

    return Optional.ofNullable(retryProperties.getRetryableExceptionsTraverseCauses().get(topic))
        .orElse(retryProperties.isRetryableExceptionsTraverseCausesDefault());
  }

  private long getRetryCountForTopic(String topic, DefaultRetryPolicyProperties retryProperties) {

    return Optional.ofNullable(retryProperties.getRetryCount().get(topic))
        .orElse(retryProperties.getRetryCountDefault());
  }

  private void setTypeMapAndTraverseCauseForBinaryExceptionClassifier(String topic,
      DefaultRetryPolicyProperties retryProperties) {

    if (!CollectionUtils.isEmpty(this.properties.getRetryableExceptions())) {

      Set<Class<? extends Throwable>> retryableExceptions = MessageRetryUtils
          .getRetryableExceptions(this.properties.getRetryableExceptions().get(topic), "retryable-exceptions");

      Map<Class<? extends Throwable>, Boolean> retryableExceptionsMap = new HashMap<>();

      retryableExceptions.forEach(exceptionClass -> retryableExceptionsMap.put(exceptionClass, true));

      this.retryableClassifier.setTypeMap(retryableExceptionsMap);
    }

    boolean traverseCause = getRetryableTraverseCausesForTopic(topic, retryProperties);

    this.retryableClassifier.setTraverseCauses(traverseCause);
  }

}
