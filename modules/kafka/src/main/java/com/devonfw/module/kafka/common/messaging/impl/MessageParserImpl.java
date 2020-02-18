package com.devonfw.module.kafka.common.messaging.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.devonfw.module.kafka.common.messaging.api.Message;
import com.devonfw.module.kafka.common.messaging.api.client.parser.MessageParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * An implementation class for the interface {@link MessageParser}
 *
 */
public class MessageParserImpl implements MessageParser {

  private ObjectMapper jacksonMapper;

  /**
   * The constructor.
   */
  public MessageParserImpl() {

    this(new ObjectMapper());
  }

  /**
   * The constructor.
   *
   * @param jacksonMapper
   */
  public MessageParserImpl(ObjectMapper jacksonMapper) {

    this.jacksonMapper = jacksonMapper;
  }

  /**
   * @param jacksonMapper
   */
  public void setJacksonMapper(ObjectMapper jacksonMapper) {

    this.jacksonMapper = jacksonMapper;
  }

  @Override
  public <T> Message<T> parseMessage(ConsumerRecord<String, String> consumerRecord, Class<T> payloadClass)
      throws Exception {

    if (ObjectUtils.isEmpty(consumerRecord) || StringUtils.isEmpty(consumerRecord.value())) {
      return null;
    }

    return parse(consumerRecord, payloadClass);
  }

  /**
   * @param <T>
   * @param payloadClass
   * @param pclValue
   * @param valueType
   * @return
   * @throws ClassNotFoundException
   */
  private <T> Class<?> getClassTypeOrThrowError(Class<T> payloadClass, String pclValue) throws ClassNotFoundException {

    Class<?> valueType;
    try {
      valueType = Class.forName(pclValue);
    } catch (ClassNotFoundException e) {
      throw new ClassNotFoundException("Invalid class name: " + pclValue, e);
    }
    if (!payloadClass.isAssignableFrom(valueType)) {
      throw new IllegalStateException("Illegal payload class: " + payloadClass.getName() + "and value: " + pclValue);
    }
    return valueType;
  }

  /**
   * @param <T>
   * @param consumerRecord
   * @param payloadClass
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  @SuppressWarnings("unchecked")
  protected <T> Message<T> parse(ConsumerRecord<String, String> consumerRecord, Class<T> payloadClass)
      throws IOException, ClassNotFoundException {

    if (ObjectUtils.isEmpty(consumerRecord) || StringUtils.isEmpty(consumerRecord.value())) {
      return null;
    }

    LineReader lineReader = new LineReader(consumerRecord.value());
    String markerRow = lineReader.readLine();

    if (StringUtils.isEmpty(markerRow) || !checkMessageMarker(markerRow)) {
      throw new IllegalArgumentException(consumerRecord.value());
    }

    Map<String, String> headers = new HashMap<>();
    headers.put(MessageMetaData.SYSTEM_HEADER_KEY_MESSAGE_ID, consumerRecord.key());

    parseHeaders(lineReader, headers);

    String pclValue = headers.get(MessageMetaData.SYSTEM_HEADER_KEY_PAYLOAD_CLASS);

    Class<?> valueType = payloadClass;

    if (!StringUtils.isEmpty(pclValue)) {
      valueType = getClassTypeOrThrowError(payloadClass, pclValue);
    }

    if (lineReader.isReadComplete()) {
      return null;
    }

    T payload = null;
    String remaining = lineReader.getRemaining();
    try {
      payload = (T) this.jacksonMapper.readValue(remaining, valueType);
    } catch (IOException e) {
      throw new IOException("incorrect remaining value: " + remaining, e);
    }

    parseKafkaMetaData(consumerRecord, headers);

    return new Message<>(payload, headers);
  }

  /**
   * @param lineReader
   * @param headers
   */
  public static void parseHeaders(LineReader lineReader, Map<String, String> headers) {

    String key = null;
    String value = null;
    int delimiterIndex;

    String headerRow = lineReader.readLine();

    while (headerRow.length() > 0) {
      delimiterIndex = headerRow.indexOf(':');

      if (delimiterIndex > 0) {

        key = headerRow.substring(0, delimiterIndex);
        value = headerRow.substring(delimiterIndex + 1);

        if (!value.isEmpty()) {
          headers.put(key, value);
        }
      }
      headerRow = lineReader.readLine();
    }
  }

  /**
   * @param consumerRecord
   * @param kafkaMetaData
   */
  protected void parseKafkaMetaData(ConsumerRecord<String, String> consumerRecord, Map<String, String> kafkaMetaData) {

    kafkaMetaData.put(MessageMetaData.KAFKA_HEADER_KEY_OFFSET, Long.toString(consumerRecord.offset()));
    kafkaMetaData.put(MessageMetaData.KAFKA_HEADER_KEY_PARTITION, Integer.toString(consumerRecord.partition()));
    kafkaMetaData.put(MessageMetaData.KAFKA_HEADER_KEY_TOPIC, consumerRecord.topic());
    kafkaMetaData.put(MessageMetaData.KAFKA_HEADER_KEY_KEY, consumerRecord.key());
  }

  /**
   * @param input
   * @return
   */
  protected boolean checkMessageMarker(String input) {

    return input.startsWith(MessageMetaData.MESSAGE_MARKER);
  }

  /**
   *
   */
  @PostConstruct
  public void validateBeanProperties() {

    Assert.notNull(this.jacksonMapper, "the Property 'jacksonMapper' cannot be null.");
  }

}
