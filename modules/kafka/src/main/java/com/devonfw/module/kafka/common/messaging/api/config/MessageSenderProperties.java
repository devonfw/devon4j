package com.devonfw.module.kafka.common.messaging.api.config;

import com.devonfw.module.kafka.common.messaging.api.client.MessageSender;

/**
 * A property class used to configure for {@link MessageSender}
 *
 * @deprecated The implementation of devon4j-kafka will be abandoned. It is superseeded by Springs Kafka
 *             implementation.
 */
@Deprecated
public class MessageSenderProperties {

  private int defaultSendTimeoutSeconds = 60;

  /**
   * @return the default timeout seconds.
   */
  public int getDefaultSendTimeoutSeconds() {

    return this.defaultSendTimeoutSeconds;
  }

  /**
   * Set the default timeout seconds for {@link #getDefaultSendTimeoutSeconds()}.
   *
   * @param defaultSendTimeoutSeconds the default timeout seconds.
   */
  public void setDefaultSendTimeoutSeconds(int defaultSendTimeoutSeconds) {

    this.defaultSendTimeoutSeconds = defaultSendTimeoutSeconds;
  }

}
