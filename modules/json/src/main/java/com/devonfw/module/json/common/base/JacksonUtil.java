package com.devonfw.module.json.common.base;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Utility class providing static method to help serializing and de-serializing complex object hierarchies with Jackson.
 * To get the current sub-tree of the JSON as {@link JsonNode} in a
 * {@link com.fasterxml.jackson.databind.JsonDeserializer}, you can simply use the following code:
 *
 * <pre>
 * {@link com.fasterxml.jackson.core.JsonParser jp}.{@link com.fasterxml.jackson.core.JsonParser#getCodec() getCodec()}.{@link
 * com.fasterxml.jackson.core.ObjectCodec#readTree(com.fasterxml.jackson.core.JsonParser) readTree}(jp)
 * </pre>
 *
 * @since 3.0.0
 */
public class JacksonUtil {

  /**
   * @param jgen the {@link JsonGenerator}.
   * @param value the value to write depending on its type.
   * @throws IOException in case of an I/O error.
   */
  public static void writeValue(JsonGenerator jgen, Object value) throws IOException {

    if (value == null) {
      return;
    }
    if (value instanceof CharSequence) {
      jgen.writeString(value.toString());
    } else if (value instanceof Number) {
      writeNumber(jgen, (Number) value);
    } else if (value instanceof Boolean) {
      jgen.writeBoolean(((Boolean) value).booleanValue());
    } else if (value instanceof Date) {
      Date date = (Date) value;
      jgen.writeString(date.toInstant().toString());
    } else if (value instanceof Calendar) {
      Calendar calendar = (Calendar) value;
      jgen.writeString(calendar.toInstant().toString());
    } else {
      jgen.writeString(value.toString());
    }
  }

  private static void writeNumber(JsonGenerator jgen, Number value) throws IOException {

    if (value instanceof Long) {
      jgen.writeNumber(value.longValue());
    } else if (value instanceof Integer) {
      jgen.writeNumber(value.intValue());
    } else if (value instanceof BigDecimal) {
      jgen.writeNumber((BigDecimal) value);
    } else if (value instanceof BigInteger) {
      jgen.writeNumber((BigInteger) value);
    } else {
      jgen.writeNumber(value.toString());
    }
  }

  /**
   * @param <V> the generic type of the value to read.
   * @param node the {@link JsonNode} to parse.
   * @param fieldName the name of the {@link JsonNode#get(String) JSON property to read} from the {@link JsonNode}.
   * @param type the {@link Class} reflecting the type of the requested value.
   * @param required - {@code true} if the value is required and an exception shall be thrown if missing, {@code false}
   *        otherwise (make it optional and return {@code null} if not present).
   * @return the parsed value. May be {@code null} if {@code required} is {@code false}.
   * @throws RuntimeException in case of an error.
   */
  public static <V> V readValue(JsonNode node, String fieldName, Class<V> type, boolean required) {

    JsonNode childNode = node.get(fieldName);
    try {
      return readValue(childNode, type, required);
    } catch (Exception e) {
      throw new IllegalStateException("Error deserializing field '" + fieldName + "'.", e);
    }
  }

  /**
   * @param <V> the generic type of the value to read.
   * @param node the {@link JsonNode} to parse.
   * @param type the {@link Class} reflecting the type of the requested value.
   * @param required - {@code true} if the value is required and an exception shall be thrown if missing, {@code false}
   *        otherwise (make it optional and return {@code null} if not present).
   * @return the parsed value. May be {@code null} if {@code required} is {@code false}.
   * @throws RuntimeException in case of an error.
   */
  public static <V> V readValue(JsonNode node, Class<V> type, boolean required) {

    V value = readValue(node, type);
    if ((value == null) && (required)) {
      throw new IllegalStateException("The value (" + type.getSimpleName() + ") is missing but was required!");
    }
    return value;
  }

  /**
   * @param <V> the generic type of the value to read.
   * @param node the {@link JsonNode} to parse.
   * @param type the {@link Class} reflecting the type of the requested value.
   * @return the parsed value. May be {@code null} if {@code required} is {@code false}.
   * @throws RuntimeException in case of an error.
   */
  public static <V> V readValue(JsonNode node, Class<V> type) {

    V value = null;
    if (node != null) {
      if (!node.isNull()) {
        try {
          value = readAtomicValue(node, type);
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException("Error converting to: " + type.getName(), e);
        }
      }
    }
    return value;
  }

  @SuppressWarnings("unchecked")
  private static <V> V readAtomicValue(JsonNode node, Class<V> type) {

    Object result;
    if (type == Boolean.class) {
      // types that may be primitive shall be casted explicitly as Class.cast does not work for primitive types.
      result = Boolean.valueOf(node.booleanValue());
    } else if (type == Integer.class) {
      result = Integer.valueOf(node.asText());
    } else if (type == Long.class) {
      result = Long.valueOf(node.asText());
    } else if (type == Double.class) {
      result = Double.valueOf(node.asText());
    } else if (type == Float.class) {
      result = Float.valueOf(node.asText());
    } else if (type == Short.class) {
      result = Short.valueOf(node.asText());
    } else if (type == Byte.class) {
      result = Byte.valueOf(node.asText());
    } else if (type == String.class) {
      result = node.asText();
    } else if (type == BigDecimal.class) {
      result = new BigDecimal(node.asText());
    } else if (type == BigInteger.class) {
      result = new BigInteger(node.asText());
    } else if (type == Date.class) {
      result = Date.from(Instant.parse(node.asText()));
    } else if (type == Calendar.class) {
      Date date = Date.from(Instant.parse(node.asText()));
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      result = calendar;
    } else if (type == Instant.class) {
      result = Instant.parse(node.asText());
    } else if (type == LocalDate.class) {
      result = LocalDate.parse(node.asText());
    } else if (type == LocalDateTime.class) {
      result = LocalDateTime.parse(node.asText());
    } else if (type == LocalTime.class) {
      result = LocalTime.parse(node.asText());
    } else if (type == OffsetDateTime.class) {
      result = OffsetDateTime.parse(node.asText());
    } else if (type == ZonedDateTime.class) {
      result = ZonedDateTime.parse(node.asText());
    } else if (type == Year.class) {
      result = Year.parse(node.asText());
    } else if (type == MonthDay.class) {
      result = type.cast(MonthDay.parse(node.asText()));
    } else {
      throw new IllegalArgumentException("Unsupported value type " + type.getName());
    }
    if (type.isPrimitive()) {
      return (V) result;
    } else {
      return type.cast(result);
    }
  }

}