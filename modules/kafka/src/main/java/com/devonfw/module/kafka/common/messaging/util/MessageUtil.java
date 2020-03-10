package com.devonfw.module.kafka.common.messaging.util;

import java.nio.charset.Charset;

import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

/**
 * @author ravicm
 *
 */
public class MessageUtil {

  private static final Charset UTF_8 = Charset.forName("UTF-8");

  private MessageUtil() {

  }

  /**
   * @param headers
   * @param key
   * @param value
   */
  public static void addHeaderValue(Headers headers, String key, String value) {

    if (key != null && value != null && !value.isEmpty()) {
      headers.add(key, value.getBytes(UTF_8));
    }
  }

  /**
   * @param headers
   * @param traceIdName
   * @return
   */
  public static String getHeaderValue(Headers headers, String traceIdName) {

    Header header = headers.lastHeader(traceIdName);

    return new String(header.value(), UTF_8);
  }

}
