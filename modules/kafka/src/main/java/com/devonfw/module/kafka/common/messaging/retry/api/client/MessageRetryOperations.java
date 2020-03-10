package com.devonfw.module.kafka.common.messaging.retry.api.client;

import org.apache.kafka.clients.producer.ProducerRecord;

import com.devonfw.module.kafka.common.messaging.retry.api.MessageRetryProcessingResult;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author ravicm
 *
 */
@FunctionalInterface
public interface MessageRetryOperations {

  /**
   * @param producerRecord
   * @param processor
   * @return MessageRetryProcessingResult
   * @throws JsonProcessingException
   */
  public MessageRetryProcessingResult processMessageWithRetry(ProducerRecord<Object, Object> producerRecord,
      MessageProcessor processor);

}
