package com.devonfw.module.kafka.common.messaging.logging.impl;

/**
 * @author ravicm
 *
 */
public enum EventKey {

  /**
   *
   */
  MESSAGE_SENT_SUCCESSFULLY("The message with the ID {} ​​was placed in the topic {}, partition {}, offset {}."),

  /**
   *
   */
  MESSAGE_NOT_SENT(
      "The message with the ID {} ​​could not be placed in the topic {}, partition {}." + "Error encountered: {}"),

  /**
   *
   */
  MESSAGE_RECEIVED("The message with the ID {} ​​was sent by the consumer group {} from the topic {}, partition {},"
      + "Offset {} read out. Time in the queue: {} ms."),

  /**
   *
   */
  SUCCESSFULLY_PROCESSED(
      "The message with the ID {} ​​was sent by the consumer group {} from the topic {}, partition {},"
          + "Offset {} read out and processed. Processing time: {} ms."),
  /**
  *
  */
  MESSAGE_NOT_PROCESSED(
      "The message with the ID {} ​​was sent by the consumer group {} from the topic {}, partition {},"
          + "Offset {} read out and could not be processed."),
  /**
  *
  */
  MESSAGE_WITHOUT_TRACEID(
      "The message with the ID {} ​​does not contain a trace ID. A new trace with the ID {}" + "is started."),

  /**
   *
   */
  RETRY_MESSAGE_ALREADY_PROCESSED(
      "The message with ID {} ​​has already been processed (status {}). There is only a commit."),
  /**
  *
  */
  RETRY_PERIOD_EXPIRED("The period ({}) for a retry of the message with the ID {} ​​has expired. There is only a"
      + "Commit and the message is discarded."),

  /**
   *
   */
  RETRY_TIME_NOT_REACHED("The time ({}) for retry of the message with the ID {} ​​has not yet been reached."
      + "The message is put back in the retry topic {}."),
  /**
  *
  */
  RETRY_SUCCESSFUL("Repeated processing of message with ID {} ​​was successful after {} attempts."
      + "The message is marked as processed in the retry topic {}."),
  /**
  *
  */
  RETRY_INITIATED("The message with the ID {} ​​is set for the {}. Retry in the retry topic {}."),

  /**
  *
  */
  RETRY_FINALLY_FAILED("The message with ID {} ​​could not be processed after {} retries.");

  private String message;

  private EventKey(String message) {

    this.message = message;
  }

  /**
   * @return value
   */
  public String getMessage() {

    return this.message;
  }

}
