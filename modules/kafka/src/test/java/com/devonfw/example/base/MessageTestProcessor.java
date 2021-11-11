package com.devonfw.example.base;

import javax.inject.Named;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageProcessor;

/**
 * THis is an implementation class for the {@link MessageProcessor}. The purpose of this class is to throw NPE as it is
 * a runtime exception.
 */
@Named
public class MessageTestProcessor implements MessageProcessor<String, String> {

  @Override
  public void processMessage(ConsumerRecord<String, String> message) {

    // will throw NPE as we are performing toString to the null value.
    message.headers().lastHeader("invalidKey").value().toString();

  }

}
