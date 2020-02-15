package com.devonfw.module.kafka.common.messaging.impl;

import static com.devonfw.module.kafka.common.messaging.util.MessageUtil.addHeaderValue;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.devonfw.module.kafka.common.messaging.api.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ravicm
 * @param <T>
 *
 */
public class MessageKafkaPayloadBuilder<T> {

  private Map<String, String> headers = new HashMap<>();

  private T payload;

  private Integer partition;

  private String topic;

  private String key;

  private ObjectMapper jacksonMapper;

  /**
   * The constructor.
   *
   * @param jacksonMapper
   */
  public MessageKafkaPayloadBuilder(ObjectMapper jacksonMapper) {

    if (jacksonMapper == null) {
      throw new IllegalArgumentException("The Jackson Mapper cannot be null");
    }
    this.jacksonMapper = jacksonMapper;
  }

  /**
   * @param <T>
   * @param jacksonMapper
   * @return
   */
  public static <T> MessageKafkaPayloadBuilder<T> with(ObjectMapper jacksonMapper) {

    return new MessageKafkaPayloadBuilder<>(jacksonMapper);
  }

  /**
   * @param message
   * @return
   */
  public MessageKafkaPayloadBuilder<T> from(Message<T> message) {

    if (ObjectUtils.isEmpty(message)) {
      return this;
    }

    return this.messageId(message.getMessageId()).topic(message.getTopic()).key(message.getMessageId())
        .headers(message.getHeaders()).payload(message.getPayload());
  }

  /**
   * @param headers
   * @return
   */
  @SuppressWarnings("hiding")
  public MessageKafkaPayloadBuilder<T> headers(Map<String, String> headers) {

    if (!CollectionUtils.isEmpty(headers)) {

      headers.forEach((key, value) -> header(key, value));
    }

    return this;
  }

  /**
   * @param name
   * @param value
   * @return
   */
  public MessageKafkaPayloadBuilder<T> header(String name, String value) {

    if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(value) && !value.isEmpty()) {
      this.headers.put(name, value);
    }
    return this;
  }

  /**
   * @param value
   * @return
   */
  public MessageKafkaPayloadBuilder<T> messageId(String value) {

    if (!StringUtils.isEmpty(value)) {
      this.headers.put(MessageMetaData.SYSTEM_HEADER_KEY_MESSAGE_ID, value);
    }
    return this;
  }

  @SuppressWarnings("hiding")
  public MessageKafkaPayloadBuilder<T> payload(T payload) {

    this.payload = payload;
    return this;
  }

  @SuppressWarnings("hiding")
  public MessageKafkaPayloadBuilder<T> topic(String topic) {

    this.topic = topic;
    return this;
  }

  @SuppressWarnings("hiding")
  public MessageKafkaPayloadBuilder<T> partition(Integer partition) {

    this.partition = partition;
    return this;
  }

  @SuppressWarnings("hiding")
  public MessageKafkaPayloadBuilder<T> key(String key) {

    this.key = key;
    return this;
  }

  /**
   * @return
   * @throws KafkaPayloadBuilderException
   */
  // public ProducerRecord<String, String> buildV1() throws JsonProcessingException {
  //
  // if (ObjectUtils.isEmpty(this.payload)) {
  // return null;
  // }
  //
  // header(MessageMetaData.SYSTEM_HEADER_KEY_PAYLOAD_CLASS, this.payload.getClass().getName());
  // header(MessageMetaData.SYSTEM_HEADER_KEY_PAYLOAD_TYPE, "application/json");
  //
  // StringBuilder payloadBuilder = new StringBuilder();
  // payloadBuilder.append(MessageMetaData.MESSAGE_MARKER_V1).append('\n');
  //
  // this.headers.forEach((k, value) -> payloadBuilder.append(k).append(':').append(value).append('\n'));
  //
  // payloadBuilder.append('\n');
  //
  // try {
  // payloadBuilder.append(this.jacksonMapper.writer().writeValueAsString(this.payload));
  // } catch (JsonProcessingException e) {
  // throw e;
  // }
  //
  // return new ProducerRecord<>(this.topic, this.partition, this.key, payloadBuilder.toString());
  // }

  /**
   * @return
   * @throws JsonProcessingException
   * @throws KafkaPayloadBuilderException
   */
  public ProducerRecord<String, String> build() throws JsonProcessingException {

    if (ObjectUtils.isEmpty(this.payload)) {
      return null;
    }

    String value;
    try {
      value = this.jacksonMapper.writer().writeValueAsString(this.payload);
    } catch (JsonProcessingException e) {
      throw e;
    }

    Headers kafkaHeaders = new RecordHeaders();

    // addHeaderValue(kafkaHeaders, MessageMetaData.SYSTEM_HEADER_KEY_MESSAGE_VERSION, MessageVersion.V2.toString());
    addHeaderValue(kafkaHeaders, MessageMetaData.SYSTEM_HEADER_KEY_PAYLOAD_CLASS, this.payload.getClass().getName());
    addHeaderValue(kafkaHeaders, MessageMetaData.SYSTEM_HEADER_KEY_PAYLOAD_TYPE, "application/json");

    this.headers.forEach((k, v) -> addHeaderValue(kafkaHeaders, k, v));

    return new ProducerRecord<>(this.topic, this.partition, this.key, value, kafkaHeaders);
  }

}
