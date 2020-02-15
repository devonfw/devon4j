package com.devonfw.module.kafka.common.messaging.retry.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.util.Assert;

import com.devonfw.module.kafka.common.messaging.api.Message;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageRetryPolicy;
import com.devonfw.module.kafka.common.messaging.retry.api.config.DefaultRetryPolicyProperties;
import com.devonfw.module.kafka.common.messaging.retry.util.MessageRetryUtils;

/**
 * @author ravicm
 *
 */
public class DefaultRetryPolicy implements MessageRetryPolicy {

  private BinaryExceptionClassifier retryableClassifier = new BinaryExceptionClassifier(false);

  private long retryPeriod;

  /**
   * The constructor.
   *
   * @param properties
   */
  public DefaultRetryPolicy(DefaultRetryPolicyProperties properties) {

    Assert.isTrue(properties.getRetryPeriod() > 0, "The property \"retry-period\" must be> 0.");
    Assert.notNull(properties.getRetryableExceptions(), "The property \"retryable-exceptions\" must not be null.");

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
  public boolean canRetry(Message<?> message, MessageRetryContext retryContext, Exception ex) {

    Assert.notNull(message, "The \"message \" parameter cannot be null.");
    Assert.notNull(ex, "The \"ex \" parameter cannot be null.");

    if (retryContext != null && retryContext.getRetryUntil() != null) {
      return canRetry(retryContext, ex);

    }
    return this.retryableClassifier.classify(ex);
  }

  /**
   * @param retryContext
   * @param ex
   */
  private boolean canRetry(MessageRetryContext retryContext, Exception ex) {

    String now = Instant.now().toString();

    if (now.compareTo(retryContext.getRetryUntil()) >= 0) {
      return false;
    }
    return this.retryableClassifier.classify(ex);
  }

  @Override
  public String getRetryUntilTimestamp(Message<?> message, MessageRetryContext retryContext) {

    return Instant.now().plusMillis(this.retryPeriod * 1000).toString();
  }

}
