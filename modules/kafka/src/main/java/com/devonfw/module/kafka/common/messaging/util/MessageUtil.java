package com.devonfw.module.kafka.common.messaging.util;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

/**
 * An utility class with support methods.
 *
 */
public class MessageUtil {

  private static final Charset UTF_8 = Charset.forName("UTF-8");

  private MessageUtil() {

  }

  /**
   * This method is used to add header value to the {@link Headers} with the value as bytes in the UTF_8 charset.
   *
   * @param headers the {@link Headers}
   * @param key the key
   * @param value the value.
   */
  public static void addHeaderValue(Headers headers, String key, String value) {

    if (key != null && value != null && !value.isEmpty()) {
      headers.add(key, value.getBytes(UTF_8));
    }
  }

  /**
   * This method is used to get the last header for the given traceIdName from {@link Headers}
   *
   * @param headers the {@link Headers}
   * @param traceIdName the traceId Name
   * @return the String.
   */
  public static String getHeaderValue(Headers headers, String traceIdName) {

    Header header = headers.lastHeader(traceIdName);

    return Optional.ofNullable(header).map(value -> new String(header.value(), UTF_8)).orElse(null);
  }

}
