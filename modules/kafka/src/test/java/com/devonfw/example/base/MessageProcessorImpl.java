package com.devonfw.example.base;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageProcessor;

/**
 * THis is an implementation class for the {@link MessageProcessor}. The purpose of this class is to do nothing as this
 * is just used to create a bean for thr MesageProcessor.
 */
public class MessageProcessorImpl implements MessageProcessor<String, String> {

  @Override
  public void processMessage(ConsumerRecord<String, String> message) {

    return;
  }

}
