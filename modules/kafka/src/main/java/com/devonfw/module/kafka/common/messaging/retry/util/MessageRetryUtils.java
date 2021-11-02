package com.devonfw.module.kafka.common.messaging.retry.util;

import java.util.HashSet;
import java.util.Set;

import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageRetryOperations;

/**
 * An Utility class to support {@link MessageRetryOperations}.
 *
 * @deprecated The implementation of devon4j-kafka will be abandoned. It is superseeded by Springs Kafka
 *             implementation.
 */
@Deprecated
public class MessageRetryUtils {

  private MessageRetryUtils() {

  }

  /**
   * This method is used to get the retryable exceptions
   *
   * @param exceptionNames the {@link Set} of exceptions
   * @param propertyName the exception property name.
   * @return Set<Class<? extends Throwable>>.
   */
  @SuppressWarnings("unchecked")
  public static Set<Class<? extends Throwable>> getRetryableExceptions(Set<String> exceptionNames,
      String propertyName) {

    Set<Class<? extends Throwable>> result = new HashSet<>();
    if (exceptionNames != null) {

      for (String className : exceptionNames) {

        try {
          result.add((Class<? extends Throwable>) Class.forName(className));
        } catch (Exception e) {
          throw new IllegalArgumentException(
              "The property \"" + propertyName + "\"contains the" + "invalid exception \"" + className + "\"", e);
        }

      }
    }
    return result;
  }

}
