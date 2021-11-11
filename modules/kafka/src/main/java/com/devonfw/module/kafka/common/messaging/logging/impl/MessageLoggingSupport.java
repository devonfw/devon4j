package com.devonfw.module.kafka.common.messaging.logging.impl;

import org.slf4j.Logger;

/**
 * This class is used to log the message events by using {@link EventKey} string formats.
 *
 * @deprecated The implementation of devon4j-kafka will be abandoned. It is superseeded by Springs Kafka
 *             implementation.
 */
@Deprecated
public class MessageLoggingSupport {

  /**
   * This method is used to log event when message is sent successfully.
   *
   * @param logger the {@link Logger}
   * @param key the message key.
   * @param topic the topic of the message.
   * @param partition the partition
   * @param offset the offset
   *
   */
  public void logMessageSent(Logger logger, String key, String topic, Integer partition, Long offset) {

    logger.info(EventKey.MESSAGE_SENT_SUCCESSFULLY.getMessage(), key, topic, partition, offset);
  }

  /**
   * This method is used to log message when its not sent.
   *
   * @param logger the {@link Logger}
   * @param topic of the message.
   * @param partition partition the partition
   * @param error the error
   */
  public void logMessageNotSent(Logger logger, String topic, Integer partition, String error) {

    logger.error(EventKey.MESSAGE_NOT_SENT.getMessage(), topic, partition, error);
  }

  /**
   * This method is used to log the received message.
   *
   * @param logger the {@link Logger}
   * @param group the groupName
   * @param topic the topic of the message
   * @param partition the partition
   * @param offset the offset
   * @param retentionPeriod the retention period
   */
  public void logMessageReceived(Logger logger, String group, String topic, Integer partition, Long offset,
      long retentionPeriod) {

    logger.info(EventKey.MESSAGE_RECEIVED.getMessage(), group, topic, partition, offset, retentionPeriod);
  }

  /**
   * This method is used to log message processed.
   *
   * @param logger the {@link Logger}
   * @param group the groupName
   * @param topic the topic
   * @param partition the partition
   * @param offset the offset
   * @param processingPeriod the processing period
   */
  public void logMessageProcessed(Logger logger, String group, String topic, Integer partition, Long offset,
      long processingPeriod) {

    logger.info(EventKey.SUCCESSFULLY_PROCESSED.getMessage(), group, topic, partition, offset, processingPeriod);
  }

  /**
   * This method is used to log when message is not processed.
   *
   * @param logger the {@link Logger}
   * @param group the groupName
   * @param topic the topic
   * @param partition the partition
   * @param offset the offset
   */
  public void logMessageNotProcessed(Logger logger, String group, String topic, Integer partition, Long offset) {

    logger.error(EventKey.MESSAGE_NOT_PROCESSED.getMessage(), group, topic, partition, offset);
  }

}
