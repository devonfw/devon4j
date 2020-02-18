package com.devonfw.module.kafka.common.messaging.impl;

import java.util.HashSet;
import java.util.Set;

import com.devonfw.module.kafka.common.messaging.trace.impl.MessageTraceHeaders;

/**
 * @author ravicm
 *
 */
public class MessageMetaData {

  public static final String MESSAGE_MARKER = "message:";

  public static final String SYSTEM_HEADER_KEY_MESSAGE_VERSION = "imv";

  public static final String SYSTEM_HEADER_KEY_MESSAGE_ID = "messageId";

  public static final String SYSTEM_HEADER_KEY_PAYLOAD_CLASS = "payloadClass";

  public static final String SYSTEM_HEADER_KEY_PAYLOAD_TYPE = "payloadType";

  public static final String KAFKA_HEADER_KEY_TOPIC = "topic";

  public static final String KAFKA_HEADER_KEY_PARTITION = "partition";

  public static final String KAFKA_HEADER_KEY_OFFSET = "offset";

  public static final String KAFKA_HEADER_KEY_KEY = "key";

  private static final Set<String> kafkaHeaderKeys = new HashSet<>();

  private static final Set<String> systemHeaderKeys = new HashSet<>();

  private static final Set<String> traceHeaderKeys = new HashSet<>();

  static {
    traceHeaderKeys.add(MessageTraceHeaders.SPAN_ID_NAME);
    traceHeaderKeys.add(MessageTraceHeaders.SAMPLED_NAME);
    traceHeaderKeys.add(MessageTraceHeaders.PROCESS_ID_NAME);
    traceHeaderKeys.add(MessageTraceHeaders.PARENT_ID_NAME);
    traceHeaderKeys.add(MessageTraceHeaders.TRACE_ID_NAME);
    traceHeaderKeys.add(MessageTraceHeaders.SPAN_NAME_NAME);

    systemHeaderKeys.add(SYSTEM_HEADER_KEY_MESSAGE_VERSION);
    systemHeaderKeys.add(SYSTEM_HEADER_KEY_MESSAGE_ID);
    systemHeaderKeys.add(SYSTEM_HEADER_KEY_PAYLOAD_CLASS);
    systemHeaderKeys.add(SYSTEM_HEADER_KEY_PAYLOAD_TYPE);

    kafkaHeaderKeys.add(KAFKA_HEADER_KEY_TOPIC);
    kafkaHeaderKeys.add(KAFKA_HEADER_KEY_PARTITION);
    kafkaHeaderKeys.add(KAFKA_HEADER_KEY_OFFSET);
    kafkaHeaderKeys.add(KAFKA_HEADER_KEY_KEY);

  }

  public static final boolean isReservedKey(String key) {

    return (isSystemHeaderKey(key) || isTraceHeaderKey(key) || isKafkaHeaderKey(key));
  }

  public static final boolean isTraceHeaderKey(String key) {

    return traceHeaderKeys.contains(key);
  }

  public static final boolean isSystemHeaderKey(String key) {

    return systemHeaderKeys.contains(key);
  }

  public static final boolean isKafkaHeaderKey(String key) {

    return kafkaHeaderKeys.contains(key);
  }
}
