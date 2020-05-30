package com.defonfw.starters.kafka.receiver.app;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageProcessor;

/**
 * THis is an implementation class for the {@link MessageProcessor}. The purpose of this class is to throw NPE as it is
 * a runtime exception.
 */
@Named
public class MessageTestProcessor implements MessageProcessor<String, String> {
  List<ConsumerRecord<String, String>> receivedMessages = new ArrayList<>();

  /**
   * @return receivedMessages
   */
  public List<ConsumerRecord<String, String>> getReceivedMessages() {

    return this.receivedMessages;
  }

  /**
   * @param receivedMessages new value of {@link #getReceivedMessages}.
   */
  public void setReceivedMessages(List<ConsumerRecord<String, String>> receivedMessages) {

    this.receivedMessages = receivedMessages;
  }

  @Override
  public void processMessage(ConsumerRecord<String, String> message) {

    this.receivedMessages.add(message);

  }

}
