package com.devonfw.module.kafka.common.messaging.trace.impl;

import static com.devonfw.module.kafka.common.messaging.util.MessageUtil.getHeaderValue;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.ObjectUtils;

import com.devonfw.module.kafka.common.messaging.impl.MessageMetaData;
import com.devonfw.module.kafka.common.messaging.logging.impl.EventKey;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.TraceContextOrSamplingFlags;

/**
 * @author ravicm
 *
 */
public final class MessageTraceSupport {

  private static final Logger LOG = LoggerFactory.getLogger(MessageTraceSupport.class);

  private MessageTraceSupport() {

  }

  /**
   * @param kafkaRecord
   * @param spanExtractor
   */
  public static void startSpan(ConsumerRecord<String, String> kafkaRecord, MessageSpanExtractor spanExtractor) {

    Tracer tracer = Tracing.currentTracer();

    if (ObjectUtils.isEmpty(tracer)) {
      return;
    }

    TraceContextOrSamplingFlags extracted = spanExtractor.extract(kafkaRecord);

    Optional.ofNullable(extracted).ifPresentOrElse(
        nonNull -> checkTraceHeadersAndSetContextAsSpanInScope(tracer, extracted),
        () -> getCurrentSpanAndLog(kafkaRecord, tracer));

  }

  /**
   * @param kafkaRecord
   * @param tracer
   */
  private static void getCurrentSpanAndLog(ConsumerRecord<String, String> kafkaRecord, Tracer tracer) {

    Span span = tracer.currentSpan();

    LOG.warn(EventKey.MESSAGE_WITHOUT_TRACEID.getMessage(),
        getHeaderValue(kafkaRecord.headers(), MessageMetaData.SYSTEM_HEADER_KEY_MESSAGE_ID),
        span.context().traceIdString());
  }

  /**
   * @param tracer
   * @param extracted
   */
  private static void checkTraceHeadersAndSetContextAsSpanInScope(Tracer tracer,
      TraceContextOrSamplingFlags extracted) {

    if (MDC.get(MessageTraceHeaders.TRACE_ID_NAME) != null && extracted.context() != null
        && !MDC.get(MessageTraceHeaders.TRACE_ID_NAME).equals(extracted.context().traceIdString())) {

      tracer.withSpanInScope(tracer.joinSpan(extracted.context()));
    }
  }

  /**
   *
   */
  public static void finishSpan() {

    Tracer tracer = Tracing.currentTracer();

    if (tracer == null || tracer.currentSpan() == null) {
      return;
    }

    Span currentSpan = tracer.currentSpan();

    LOG.debug("Current span is closed: ID={}, trace-ID={}, parent-ID={}", currentSpan.context().spanId(),
        currentSpan.context().traceId(), currentSpan.context().parentIdAsLong(), currentSpan.context());

    currentSpan.finish();
  }
}
