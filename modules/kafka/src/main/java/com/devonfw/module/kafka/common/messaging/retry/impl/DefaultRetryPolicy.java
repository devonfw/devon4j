package com.devonfw.module.kafka.common.messaging.retry.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.devonfw.module.kafka.common.messaging.logging.impl.EventKey;
import com.devonfw.module.kafka.common.messaging.retry.api.RetryState;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageBackOffPolicy;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageRetryHandler;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageRetryPolicy;
import com.devonfw.module.kafka.common.messaging.retry.api.config.DefaultRetryPolicyProperties;
import com.devonfw.module.kafka.common.messaging.retry.util.MessageRetryUtils;

/**
 * This is an implementation class for the {@link MessageRetryPolicy}
 *
 */
public class DefaultRetryPolicy implements MessageRetryPolicy<Object, Object> {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultRetryPolicy.class);

  private BinaryExceptionClassifier retryableClassifier = new BinaryExceptionClassifier(false);

  private long retryPeriod;

  private long retryCount;

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

    this.retryCount = properties.getRetryCount();
  }

  @Override
  public boolean canRetry(ConsumerRecord<Object, Object> consumerRecord, MessageRetryContext retryContext,
      MessageRetryHandler<Object, Object> retryHandler, MessageBackOffPolicy backOffPolicy, Exception ex) {

    if (ObjectUtils.isEmpty(consumerRecord)) {
      throw new IllegalArgumentException("The \"consumerRecord \" parameter cannot be null.");
    }

    if (ObjectUtils.isEmpty(ex)) {
      throw new IllegalArgumentException("The \"ex \" parameter cannot be null.");
    }

    if (ObjectUtils.isEmpty(retryContext)) {
      throw new IllegalArgumentException("The \"retryContext \" parameter cannot be null.");
    }

    if (ObjectUtils.isEmpty(backOffPolicy)) {
      throw new IllegalArgumentException("The \"backOffPolicy \" parameter cannot be null.");
    }

    Instant now = Instant.now();

    if (retryContext.getRetryState() != RetryState.PENDING && retryContext.getCurrentRetryCount() < this.retryCount) {
      LOG.info(EventKey.RETRY_MESSAGE_ALREADY_PROCESSED.getMessage(), retryContext.getRetryState());
      return false;
    }

    if (now.compareTo(retryContext.getRetryUntil()) > 0) {

      LOG.info(EventKey.RETRY_PERIOD_EXPIRED.getMessage(), retryContext.getRetryUntil());
      retryContext.setRetryState(RetryState.EXPIRED);

      if (retryHandler != null) {
        retryHandler.retryTimeout(consumerRecord, retryContext);
      }
      // return false;
    }

    retryContext.incRetryReadCount();

    if (retryContext.getRetryNext() != null && now.compareTo(retryContext.getRetryNext()) < 0) {

      backOffPolicy.sleepBeforeReEnqueue();

      LOG.info(EventKey.RETRY_TIME_NOT_REACHED.getMessage(), retryContext.getRetryNext(),
          retryContext.getCurrentRetryCount() + 1, consumerRecord.topic());

      // return false;
    }

    retryContext.incCurrentRetryCount();

    return this.retryableClassifier.classify(ex);
  }

  @Override
  public Instant getRetryUntilTimestamp(ConsumerRecord<Object, Object> consumerRecord,
      MessageRetryContext retryContext) {

    return Instant.now().plusMillis(this.retryPeriod * 1000);
  }

  /**
   * The number of times to execute the Retry.
   *
   * @return retryCount
   */
  @Override
  public long getRetryCount() {

    return this.retryCount;
  }

}
