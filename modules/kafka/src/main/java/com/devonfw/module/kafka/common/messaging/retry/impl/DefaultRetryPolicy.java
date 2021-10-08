package com.devonfw.module.kafka.common.messaging.retry.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
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
 * @deprecated The implementation of Devon4Js Kafka module will be abandoned. It is superseeded by Springs Kafka
 *             implementation.
 */
@Deprecated
public class DefaultRetryPolicy<K, V> implements MessageRetryPolicy<K, V> {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultRetryPolicy.class);

  private static final String DEFAULT_KEY = "default";

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

    String messageKey = "<no message key>";

    if (consumerRecord.key() != null) {
      messageKey = consumerRecord.key().toString();
    }

    LOG.info("proceeding with retry for the message Id {} and due to {}", messageKey, ex.getMessage());

    String topic = consumerRecord.topic();

    setTypeMapAndTraverseCauseForBinaryExceptionClassifier(topic);

    long retryCount = getRetryCountForTopic(topic);

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

    long retryPeriod = getretryPeriodForTopic(consumerRecord.topic());

    return Instant.now().plusMillis(retryPeriod * 1000);
  }

  /**
   * The number of times to execute the Retry.
   *
   * @return retryCount
   */
  @Override
  public long getRetryCount(String topic) {

    return getRetryCountForTopic(topic);
  }

  private long getretryPeriodForTopic(String topic) {

    Map<String, Long> retryPeriodMap = this.properties.getRetryPeriod();

    if (retryPeriodMap.containsKey(DEFAULT_KEY)) {
      return Optional.ofNullable(retryPeriodMap.get(DEFAULT_KEY)).orElse(this.properties.getRetryPeriodDefault());
    }

    return Optional.ofNullable(retryPeriodMap.get(topic)).orElse(this.properties.getRetryPeriodDefault());
  }

  private boolean getRetryableTraverseCausesForTopic(String topic) {

    Map<String, Boolean> retryTraverseCauseMap = this.properties.getRetryableExceptionsTraverseCauses();

    if (retryTraverseCauseMap.containsKey(DEFAULT_KEY)) {
      return Optional.ofNullable(retryTraverseCauseMap.get(DEFAULT_KEY))
          .orElse(this.properties.isRetryableExceptionsTraverseCausesDefault());
    }

    return Optional.ofNullable(retryTraverseCauseMap.get(topic))
        .orElse(this.properties.isRetryableExceptionsTraverseCausesDefault());
  }

  private long getRetryCountForTopic(String topic) {

    Map<String, Long> retryCountMap = this.properties.getRetryCount();

    if (retryCountMap.containsKey(DEFAULT_KEY)) {
      return Optional.ofNullable(retryCountMap.get(DEFAULT_KEY)).orElse(this.properties.getRetryCountDefault());
    }

    return Optional.ofNullable(this.properties.getRetryCount().get(topic))
        .orElse(this.properties.getRetryCountDefault());
  }

  private void setTypeMapAndTraverseCauseForBinaryExceptionClassifier(String topic) {

    Map<String, Set<String>> retryableExceptionMap = this.properties.getRetryableExceptions();

    if (!CollectionUtils.isEmpty(retryableExceptionMap)) {

      Set<String> retryExceptionsNames = getRetryExceptionNamesFromProperties(topic, retryableExceptionMap);

      Set<Class<? extends Throwable>> retryableExceptions = MessageRetryUtils
          .getRetryableExceptions(retryExceptionsNames, "retryable-exceptions");

      Map<Class<? extends Throwable>, Boolean> retryableExceptionsMap = new HashMap<>();

      retryableExceptions.forEach(exceptionClass -> retryableExceptionsMap.put(exceptionClass, true));

      this.retryableClassifier.setTypeMap(retryableExceptionsMap);
    }

    boolean traverseCause = getRetryableTraverseCausesForTopic(topic);

    this.retryableClassifier.setTraverseCauses(traverseCause);
  }

  private Set<String> getRetryExceptionNamesFromProperties(String topic,
      Map<String, Set<String>> retryableExceptionMap) {

    if (retryableExceptionMap.containsKey(DEFAULT_KEY)) {
      return Optional.ofNullable(retryableExceptionMap.get(DEFAULT_KEY)).orElse(new HashSet<String>());
    }

    return retryableExceptionMap.get(topic);
  }

}
