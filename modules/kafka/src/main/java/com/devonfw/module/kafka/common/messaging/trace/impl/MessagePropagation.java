package com.devonfw.module.kafka.common.messaging.trace.impl;

import java.util.Map;

import brave.propagation.Propagation.Getter;
import brave.propagation.Propagation.Setter;

/**
 * @author ravicm
 *
 */
public final class MessagePropagation {

  static final Setter<Map<String, String>, String> HEADER_SETTER = (carrier, key, value) -> {
    carrier.put(key, value);
  };

  static final Getter<Map<String, String>, String> HEADER_GETTER = (carrier, key) -> {
    String value = carrier.get(key);
    if (value == null) {
      return null;
    }
    return value;
  };

  private MessagePropagation() {

  }

}
