package com.devonfw.module.kafka.common.messaging.health.impl;

/**
 * An Enum class to specify the Health status.
 *
 * @deprecated The implementation of devon4j-kafka will be abandoned. It is superseeded by Springs Kafka
 *             implementation.
 */
@Deprecated
public enum HealthStatus {

  /**
   * This indicates the the health of the kafka is UP.
   */
  UP,

  /**
   * This indicates the the health of the kafka is DOWN.
   */
  DOWN;
}
