package com.devonfw.module.kafka.common.messaging.retry.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageRetryPolicy;
import com.devonfw.module.kafka.common.messaging.retry.api.config.DefaultRetryPolicyProperties;
import com.devonfw.module.kafka.common.messaging.retry.util.MessageRetryUtils;

/**
 * This is an implementation class for the {@link MessageRetryPolicy}
 *
 */
public class DefaultRetryPolicy implements MessageRetryPolicy {

  private BinaryExceptionClassifier retryableClassifier = new BinaryExceptionClassifier(false);

  private long retryPeriod;

  /**
   * The constructor.
   *
   * @param properties the {@link DefaultRetryPolicyProperties}
   */
  public DefaultRetryPolicy(DefaultRetryPolicyProperties properties) {

    if (properties.getRetryPeriod() < 0) {
      throw new IllegalArgumentException("The property \\\"retry-period\\\" must be> 0.");
    }

    if (CollectionUtils.isEmpty(properties.getRetryableExceptions())) {
      throw new IllegalArgumentException("The property \\\"retryable-exceptions\\\" must not be null.");
    }

    this.retryPeriod = properties.getRetryPeriod();

    if (!properties.getRetryableExceptions().isEmpty()) {

      Set<Class<? extends Throwable>> retryableExceptions = MessageRetryUtils
          .getRetryableExceptions(properties.getRetryableExceptions(), "retryable-exceptions");

      Map<Class<? extends Throwable>, Boolean> retryableExceptionsMap = new HashMap<>();

      retryableExceptions.forEach(exceptionClass -> retryableExceptionsMap.put(exceptionClass, true));

      this.retryableClassifier.setTypeMap(retryableExceptionsMap);
    }
    this.retryableClassifier.setTraverseCauses(properties.isRetryableExceptionsTraverseCauses());
  }

  @Override
  public boolean canRetry(ConsumerRecord<Object, Object> consumerRecord, MessageRetryContext retryContext,
      Exception ex) {

    if (ObjectUtils.isEmpty(consumerRecord)) {
      throw new IllegalArgumentException("The \"consumerRecord \" parameter cannot be null.");
    }

    if (ObjectUtils.isEmpty(ex)) {
      throw new IllegalArgumentException("The \\\"ex \\\" parameter cannot be null.");
    }

    if (retryContext != null && retryContext.getRetryUntil() != null) {
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
  public Instant getRetryUntilTimestamp(ConsumerRecord<Object, Object> consumerRecord,
      MessageRetryContext retryContext) {

    return Instant.now().plusMillis(this.retryPeriod * 1000);
  }

}
