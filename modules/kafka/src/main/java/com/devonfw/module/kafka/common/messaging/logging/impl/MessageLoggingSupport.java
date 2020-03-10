package com.devonfw.module.kafka.common.messaging.logging.impl;

import org.slf4j.Logger;

/**
 * @author ravicm
 *
 */
public class MessageLoggingSupport {

  /**
   * @param logger
   * @param messageId
   * @param topic
   * @param value
   * @param string
   * @param partition
   * @param offset
   */
  public void logMessageSent(Logger logger, String value, String topic, Integer partition, Long offset) {

    logger.info(EventKey.MESSAGE_SENT_SUCCESSFULLY.getMessage(), value, topic, partition, offset);
  }

  /**
   * @param logger
   * @param messageId
   * @param topic
   * @param partition
   * @param error
   */
  public void logMessageNotSent(Logger logger, String topic, Integer partition, String error) {

    logger.error(EventKey.MESSAGE_NOT_SENT.getMessage(), topic, partition, error);
  }

  /**
   * @param logger
   * @param messageId
   * @param group
   * @param topic
   * @param partition
   * @param offset
   * @param retentionPeriod
   */
  public void logMessageReceived(Logger logger, String group, String topic, Integer partition, Long offset,
      long retentionPeriod) {

    logger.info(EventKey.MESSAGE_RECEIVED.getMessage(), group, topic, partition, offset, retentionPeriod);
  }

  /**
   * @param logger
   * @param messageId
   * @param group
   * @param topic
   * @param partition
   * @param offset
   * @param processingPeriod
   */
  public void logMessageProcessed(Logger logger, String group, String topic, Integer partition, Long offset,
      long processingPeriod) {

    logger.info(EventKey.SUCCESSFULLY_PROCESSED.getMessage(), group, topic, partition, offset, processingPeriod);
  }

  /**
   * @param logger
   * @param messageId
   * @param group
   * @param topic
   * @param partition
   * @param offset
   */
  public void logMessageNotProcessed(Logger logger, String group, String topic, Integer partition, Long offset) {

    logger.error(EventKey.MESSAGE_NOT_PROCESSED.getMessage(), group, topic, partition, offset);
  }

}
