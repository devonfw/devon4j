package com.devonfw.module.json.common.base;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import net.sf.mmm.util.date.base.Iso8601UtilImpl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Helper class to simplify implementation of {@link JsonDeserializer}.
 *
 * @param <T> the class to be deserialized
 *
 * @since 3.0.0
 */
public abstract class AbstractJsonDeserializer<T> extends JsonDeserializer<T> {

  /**
   * @param <V> the type to convert the requested value to.
   * @param node parent node to deserialize
   * @param fieldName the name of the JSON property to get the value from.
   * @param type the {@link Class} reflecting the type to convert the requested value to.
   * @return requested value converted to the given {@code type}. May not be {@code null}.
   * @throws RuntimeException if the requested value is not present or if the conversion failed.
   */
  protected <V> V getRequiredValue(JsonNode node, String fieldName, Class<V> type) throws RuntimeException {

    return getValue(node, fieldName, type, true);
  }

  /**
   * @param <V> the type to convert the requested value to.
   * @param node parent node to deserialize
   * @param fieldName the name of the JSON property to get the value from.
   * @param type the {@link Class} reflecting the type to convert the requested value to.
   * @param fallback is the default returned if the requested value is undefined.
   * @return requested value converted to the given {@code type} or {@code fallback} if undefined.
   * @throws RuntimeException if the conversion failed.
   */
  protected <V> V getOptionalValue(JsonNode node, String fieldName, Class<V> type, V fallback) throws RuntimeException {

    V result = getValue(node, fieldName, type, false);
    if (result == null) {
      result = fallback;
    }
    return result;
  }

  /**
   * @param <V> the type to convert the requested value to.
   * @param node parent node to deserialize
   * @param fieldName the name of the JSON property to get the value from.
   * @param type the {@link Class} reflecting the type to convert the requested value to.
   * @param required {@code true} if the requested value has to be present, {@code false} otherwise (if optional).
   * @return requested value converted to the given {@code type} or {@code null} if undefined and {@code required} is
   *         {@code true}.
   * @throws RuntimeException if {@code required} is {@code true} and the requested value is not present or if the
   *         conversion failed.
   */
  @SuppressWarnings("unchecked")
  protected <V> V getValue(JsonNode node, String fieldName, Class<V> type, boolean required) throws RuntimeException {

    V value = null;
    JsonNode childNode = node.get(fieldName);
    if (childNode != null) {
      if (!childNode.isNull()) {
        try {
          if (type == String.class) {
            value = type.cast(childNode.asText());
          } else if (type == BigDecimal.class) {
            value = type.cast(new BigDecimal(childNode.asText()));
          } else if (type == BigInteger.class) {
            value = type.cast(new BigInteger(childNode.asText()));
          } else if (type == Date.class) {
            value = type.cast(Iso8601UtilImpl.getInstance().parseDate(childNode.asText()));
          } else if (type == Calendar.class) {
            value = type.cast(Iso8601UtilImpl.getInstance().parseCalendar(childNode.asText()));
          } else if (type == Boolean.class) {
            // types that may be primitive shall be casted explicitly as Class.cast does not work for primitive types.
            value = (V) Boolean.valueOf(childNode.booleanValue());
          } else if (type == Integer.class) {
            value = (V) Integer.valueOf(childNode.asText());
          } else if (type == Long.class) {
            value = (V) Long.valueOf(childNode.asText());
          } else if (type == Double.class) {
            value = (V) Double.valueOf(childNode.asText());
          } else if (type == Float.class) {
            value = (V) Float.valueOf(childNode.asText());
          } else if (type == Short.class) {
            value = (V) Short.valueOf(childNode.asText());
          } else if (type == Byte.class) {
            value = (V) Byte.valueOf(childNode.asText());
          } else {
            throw new IllegalArgumentException("Unsupported value type " + type.getName());
          }
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException("Failed to convert value to type " + type.getName(), e);
        }
      }
    }
    if ((value == null) && (required)) {
      throw new IllegalStateException(
          "Deserialization failed due to missing " + type.getSimpleName() + " field " + fieldName + "!");
    }
    return value;
  }

  @Override
  public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

    JsonNode node = jp.getCodec().readTree(jp);
    return deserializeNode(node);
  }

  /**
   * @param node is the {@link JsonNode} with the value content to be deserialized
   * @return the deserialized java object
   */
  protected abstract T deserializeNode(JsonNode node);
}
