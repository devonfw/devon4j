package com.devonfw.module.kafka.common.messaging.trace.impl;

import static brave.internal.HexCodec.lowerHexToUnsignedLong;
import static com.devonfw.module.kafka.common.messaging.util.MessageUtil.getHeaderValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.devonfw.module.kafka.common.messaging.impl.LineReader;
import com.devonfw.module.kafka.common.messaging.impl.MessageParserImpl;

import brave.propagation.TraceContext;
import brave.propagation.TraceContext.Builder;
import brave.propagation.TraceContext.Extractor;
import brave.propagation.TraceContextOrSamplingFlags;

/**
 * @author ravicm
 *
 */
public class MessageSpanExtractor implements Extractor<ConsumerRecord<String, String>> {

  @Override
  public TraceContextOrSamplingFlags extract(ConsumerRecord<String, String> consumerRecord) {

    Builder tcBuilder = TraceContext.newBuilder();
    boolean traceIdExists = false;

    if (consumerRecord != null) {

      // MessageVersion messageVersion = getMessageVersion(consumerRecord);
      // if (messageVersion == MessageVersion.V1) {
      // traceIdExists = extractV1(consumerRecord.value(), tcBuilder);
      // } else {
      String traceId = getHeaderValue(consumerRecord.headers(), MessageTraceHeaders.TRACE_ID_NAME);
      if (traceId != null && !traceId.isEmpty()) {
        traceIdExists = true;
        tcBuilder.traceId(lowerHexToUnsignedLong(traceId));
        String spanId = getHeaderValue(consumerRecord.headers(), MessageTraceHeaders.SPAN_ID_NAME);

        Optional.ofNullable(spanId).ifPresent(id -> tcBuilder.spanId(lowerHexToUnsignedLong(id)));

        // if (spanId != null) {
        // tcBuilder.spanId(lowerHexToUnsignedLong(spanId));
        // }
        String parentId = getHeaderValue(consumerRecord.headers(), MessageTraceHeaders.PARENT_ID_NAME);

        Optional.ofNullable(parentId).ifPresent(id -> tcBuilder.parentId(lowerHexToUnsignedLong(id)));
        // if (parentId != null) {
        // tcBuilder.parentId(lowerHexToUnsignedLong(parentId));
        // }
      }
      // }
    }

    if (traceIdExists) {
      return TraceContextOrSamplingFlags.create(tcBuilder.build());
    }

    return null;
  }

  private boolean extractV1(String payload, Builder tcBuilder) {

    if (payload == null) {
      return false;
    }

    Map<String, String> headers = parseHeadersV1(payload);
    String traceId = headers.get(MessageTraceHeaders.TRACE_ID_NAME);
    if (traceId == null || traceId.isEmpty()) {
      return false;
    }

    tcBuilder.traceId(lowerHexToUnsignedLong(headers.get(MessageTraceHeaders.TRACE_ID_NAME)));
    if (headers.containsKey(MessageTraceHeaders.SPAN_ID_NAME)) {
      tcBuilder.spanId(lowerHexToUnsignedLong(headers.get(MessageTraceHeaders.SPAN_ID_NAME)));
    }
    if (headers.containsKey(MessageTraceHeaders.PARENT_ID_NAME)) {
      tcBuilder.parentId(lowerHexToUnsignedLong(headers.get(MessageTraceHeaders.PARENT_ID_NAME)));
    }

    return true;
  }

  private Map<String, String> parseHeadersV1(String payload) {

    LineReader lineReader = new LineReader(payload);
    Map<String, String> headers = new HashMap<>();
    MessageParserImpl.parseHeaders(lineReader, headers);
    return headers;
  }

}
