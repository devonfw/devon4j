package com.devonfw.module.kafka.common.messaging.retry.api.client;

import org.apache.kafka.clients.producer.ProducerRecord;

import com.devonfw.module.kafka.common.messaging.retry.api.MessageRetryProcessingResult;

/**
 * This interface is used to process the given {@link ProducerRecord} with the retry pattern. The processor can be
 * implemented by {@link MessageProcessor}.
 *
 */
@FunctionalInterface
public interface MessageRetryOperations {

  /**
   * This method is used to process the given {@link ProducerRecord} using
   * {@link MessageProcessor#processMessage(Object)} with retry pattern.
   *
   * @param producerRecord the {@link ProducerRecord}
   * @param processor the {@link MessageProcessor}
   * @return MessageRetryProcessingResult the {@link MessageRetryProcessingResult}
   */
  public MessageRetryProcessingResult processMessageWithRetry(ProducerRecord<Object, Object> producerRecord,
      MessageProcessor processor);

}
