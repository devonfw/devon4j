package com.devonfw.module.basic.common.api.config;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;

/**
 * Utility that helps to deal with configuration values.
 */
public final class ConfigValueUtil {

  private ConfigValueUtil() {
  }

  /**
   * @param <T> the generic {@code type}.
   * @param object the {@link Object} to convert. Will not be {@code null}.
   * @param type the {@link Class} reflecting the requested type.
   * @return the given {@link Object} converted to the given {@code type}.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static <T> T convertValue(Object object, Class<T> type) {

    try {
      Object result;
      if (type.isInstance(object)) {
        result = object;
      } else if (type.isEnum()) {
        result = Enum.valueOf((Class<? extends Enum>) type, object.toString());
      } else if (type.isAssignableFrom(String.class)) {
        result = object.toString();
      } else if ((type == boolean.class) || (type == Boolean.class)) {
        result = Boolean.valueOf(object.toString());
      } else if ((type == int.class) || (type == Integer.class)) {
        result = Integer.valueOf(object.toString());
      } else if ((type == long.class) || (type == Long.class)) {
        result = Long.valueOf(object.toString());
      } else if ((type == double.class) || (type == Double.class)) {
        result = Double.valueOf(object.toString());
      } else if (type == Class.class) {
        result = Class.forName(object.toString());
      } else if ((type == float.class) || (type == Float.class)) {
        result = Float.valueOf(object.toString());
      } else if ((type == short.class) || (type == Short.class)) {
        result = Short.valueOf(object.toString());
      } else if ((type == byte.class) || (type == Byte.class)) {
        result = Byte.valueOf(object.toString());
      } else if (type == BigDecimal.class) {
        result = new BigDecimal(object.toString());
      } else if (type == BigInteger.class) {
        result = new BigInteger(object.toString());
      } else if (type == Number.class) {
        result = Double.parseDouble(object.toString());
      } else if ((type == Character.class) || ((type == char.class))) {
        String value = object.toString();
        if (value.length() == 1) {
          result = Character.valueOf(value.charAt(0));
        } else {
          throw new IllegalArgumentException(value);
        }
      } else if (type == Currency.class) {
        result = Currency.getInstance(object.toString());
      } else {
        throw new IllegalArgumentException(object.toString());
      }
      return (T) result;
    } catch (NumberFormatException | ClassNotFoundException e) {
      throw new IllegalArgumentException(object.toString(), e);
    }
  }

}
