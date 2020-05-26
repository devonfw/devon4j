package com.devonfw.module.kafka.common.messaging.logging.impl;

/**
 * An Enum keys to represent Log String format.
 */
public enum EventKey {

  /**
   * MESSAGE_SENT_SUCCESSFULLY
   */
  MESSAGE_SENT_SUCCESSFULLY("The message with ID {} ​​was placed in the topic {}, partition {}, offset {}."),

  /**
   * MESSAGE_NOT_SENT
   */
  MESSAGE_NOT_SENT("The message ​​could not be placed in the topic {}, partition {}." + "Error encountered: {}"),

  /**
   * MESSAGE_RECEIVED
   */
  MESSAGE_RECEIVED("The message ​​was sent by the consumer group {} from the topic {}, partition {},"
      + "Offset {} read out. Time in the queue: {} ms."),

  /**
   * SUCCESSFULLY_PROCESSED
   */
  SUCCESSFULLY_PROCESSED("The message ​​was sent by the consumer group {} from the topic {}, partition {},"
      + "Offset {} read out and processed. Processing time: {} ms."),

  /**
   * MESSAGE_NOT_PROCESSED
   */
  MESSAGE_NOT_PROCESSED("The message ​​was sent by the consumer group {} from the topic {}, partition {},"
      + "Offset {} read out and could not be processed."),

  /**
   * MESSAGE_WITHOUT_TRACEID
   */
  MESSAGE_WITHOUT_TRACEID("The message ​​does not contain a trace ID. A new trace with the ID {}" + "is started."),

  /**
   * RETRY_MESSAGE_ALREADY_PROCESSED
   */
  RETRY_MESSAGE_ALREADY_PROCESSED("The message ​​has already been processed (status {}). There is only a commit."),

  /**
   * RETRY_PERIOD_EXPIRED
   */
  RETRY_PERIOD_EXPIRED("The period ({}) for a retry of the message ​​has expired. There is only a"
      + "Commit and the message is discarded."),

  /**
   * RETRY_TIME_NOT_REACHED
   */
  RETRY_TIME_NOT_REACHED("The time ({}) for retry of the message ​​has not yet been reached."
      + "The message is put back in the retry topic {}."),

  /**
   * RETRY_SUCCESSFUL
   */
  RETRY_SUCCESSFUL("Repeated processing of message ​​was successful after {} attempts."
      + "The message is marked as processed in the retry topic {}."),

  /**
   * RETRY_INITIATED
   */
  RETRY_INITIATED("The message ​​is set for the {}. Retry in the retry topic {}."),

  /**
   * RETRY_FINALLY_FAILED
   */
  RETRY_FINALLY_FAILED("The message with ID {} ​​could not be processed after {} retries.");

  private String message;

  private EventKey(String message) {

    this.message = message;
  }

  /**
   * @return the enum value which represents string format for Logger.
   */
  public String getMessage() {

    return this.message;
  }

}
