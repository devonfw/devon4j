package com.devonfw.module.kafka.common.messaging.trace.api.config;

/**
 * @author ravicm
 *
 */
public class MessageTraceProperties {

  private String name = "messaging-application";

  public String getName() {

    return this.name;
  }

  public void setName(String name) {

    this.name = name;
  }

}
