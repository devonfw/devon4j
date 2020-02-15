package com.devonfw.module.kafka.common.messaging.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.devonfw.module.kafka.common.messaging.impl.MessageMetaData;

/**
 * This class is a message which needs to be send to kafkaBroker along with headers.
 *
 * @param <T> the type of Message.
 */
public class Message<T> {

  private T payload;

  private Map<String, String> customHeaders = new HashMap<>();

  private Map<String, String> systemHeaders = new HashMap<>();

  private Map<String, String> kafkaMetaData = new HashMap<>();

  /**
   * The constructor.
   *
   * @param payload the payload is a message and type of {@linkplain Message}
   * @param headers refers to the kafkaMetaData and system headers.
   */
  public Message(T payload, Map<String, String> headers) {

    this(payload);

    if (!CollectionUtils.isEmpty(headers)) {

      headers.forEach((key, value) -> evaluateKeyAndAssignHeaders(key, value));
    }
  }

  private void evaluateKeyAndAssignHeaders(String key, String value) {

    if (MessageMetaData.isKafkaHeaderKey(key)) {
      this.kafkaMetaData.put(key, value);
    }

    if (MessageMetaData.isSystemHeaderKey(key)) {
      this.systemHeaders.put(key, value);
    } else {
      setHeader(key, value);
    }
  }

  /**
   * The constructor.
   *
   * @param payload the message type. The value can be any class type.
   */
  public Message(T payload) {

    this();
    setPayload(payload);
  }

  /**
   * The constructor is used to initialize object for {@link Message} and adds to the systemHeaders for the key
   * {@link MessageMetaData#SYSTEM_HEADER_KEY_MESSAGE_ID} and the value is {@link UUID#randomUUID()}
   */
  public Message() {

    this.systemHeaders.put(MessageMetaData.SYSTEM_HEADER_KEY_MESSAGE_ID, UUID.randomUUID().toString());
  }

  /**
   * Set the value for the payload for the message type.
   *
   * @param payload the message type. The value can be any Class type.
   */
  public void setPayload(T payload) {

    this.payload = payload;
  }

  /**
   * @return the payload.
   */
  public T getPayload() {

    return this.payload;
  }

  /**
   * This method is used to set custom headers for the provided key and value.
   *
   * @param key the key needs to set
   * @param value the value for the key
   */
  public void setHeader(String key, String value) {

    if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value) && value.indexOf('\n') >= 0) {

      throw new IllegalArgumentException("Invalid key and value: " + key + " & " + value);
    }
    this.customHeaders.put(key, value);
  }

  /**
   * @return the key from KafkaMeta data with the key {@link MessageMetaData#KAFKA_HEADER_KEY_TOPIC}
   */
  public String getTopic() {

    return this.kafkaMetaData.get(MessageMetaData.KAFKA_HEADER_KEY_TOPIC);
  }

  /**
   * Set the topic in KafkaMeta data for the key {@link MessageMetaData#KAFKA_HEADER_KEY_TOPIC}.
   *
   * @param topic the topic name for the key {@link MessageMetaData#KAFKA_HEADER_KEY_TOPIC}
   */
  public void setTopic(String topic) {

    this.kafkaMetaData.put(MessageMetaData.KAFKA_HEADER_KEY_TOPIC, topic);
  }

  /**
   * @return the partition value from the kafkaMetaData with the key {@link MessageMetaData#KAFKA_HEADER_KEY_PARTITION}.
   */
  public String getPartition() {

    return this.kafkaMetaData.get(MessageMetaData.KAFKA_HEADER_KEY_PARTITION);
  }

  /**
   * @return the offset value from the kafkaMetaData with the key {@link MessageMetaData#KAFKA_HEADER_KEY_OFFSET}.
   */
  public String getOffset() {

    return this.kafkaMetaData.get(MessageMetaData.KAFKA_HEADER_KEY_OFFSET);
  }

  /**
   * @return the key from the kafkaMetaData with the key {@link MessageMetaData#KAFKA_HEADER_KEY_KEY}.
   */
  public String getKey() {

    return this.kafkaMetaData.get(MessageMetaData.KAFKA_HEADER_KEY_KEY);
  }

  /**
   * @param key the key value to fetch headerValue.
   * @return the headerValue for the given key from the CustomHeaders.
   */
  public String getHeaderValue(String key) {

    return this.customHeaders.get(key);
  }

  /**
   * @return the unique keySet from the CustomHeaders.
   */
  public Set<String> getHeaderKeys() {

    return this.customHeaders.keySet();
  }

  /**
   * @return the CustomHeaders.
   */
  public Map<String, String> getHeaders() {

    return Collections.unmodifiableMap(this.customHeaders);
  }

  /**
   * @return the messageId from systemHeaders with the key {@link MessageMetaData#SYSTEM_HEADER_KEY_MESSAGE_ID}
   */
  public String getMessageId() {

    return this.systemHeaders.get(MessageMetaData.SYSTEM_HEADER_KEY_MESSAGE_ID);
  }

  /**
   * @param key the key to fetch value from systemHeaders.
   * @return the system headerValue for the given key.
   */
  public String getSystemHeaderValue(String key) {

    return this.systemHeaders.get(key);
  }

}
