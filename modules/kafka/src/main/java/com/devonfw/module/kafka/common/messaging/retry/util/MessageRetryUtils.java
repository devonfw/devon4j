package com.devonfw.module.kafka.common.messaging.retry.util;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ravicm
 *
 */
public class MessageRetryUtils {

  private MessageRetryUtils() {

  }

  /**
   * @param exceptionNames
   * @param propertyName
   * @return
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
