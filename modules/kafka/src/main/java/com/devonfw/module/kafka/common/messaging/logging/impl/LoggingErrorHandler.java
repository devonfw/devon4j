package com.devonfw.module.kafka.common.messaging.logging.impl;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.util.ObjectUtils;

/**
 * This is an implementation class for the {@link ErrorHandler}. This class handles the exception by logging the error
 * message and the exception thrown.
 *
 */
public class LoggingErrorHandler implements ErrorHandler {

  private static final Logger LOG = LoggerFactory.getLogger(LoggingErrorHandler.class);

  @Override
  public void handle(Exception thrownException, ConsumerRecord<?, ?> record) {

    LOG.error("Error processing message: {}", thrownException, ObjectUtils.nullSafeToString(record));
  }
}
