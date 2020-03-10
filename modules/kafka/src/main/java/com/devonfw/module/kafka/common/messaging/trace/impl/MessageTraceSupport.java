package com.devonfw.module.kafka.common.messaging.trace.impl;

import java.util.Optional;

import org.apache.commons.codec.Charsets;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.util.ObjectUtils;

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
  public static void startSpan(ConsumerRecord<Object, Object> kafkaRecord, MessageSpanExtractor spanExtractor) {

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
  private static void getCurrentSpanAndLog(ConsumerRecord<Object, Object> kafkaRecord, Tracer tracer) {

    Span span = tracer.currentSpan();

    LOG.warn(EventKey.MESSAGE_WITHOUT_TRACEID.getMessage(),
        new String(kafkaRecord.headers().lastHeader(KafkaHeaders.MESSAGE_KEY).value(), Charsets.UTF_8),
        span.context().traceIdString());
  }

  /**
   * @param tracer
   * @param extracted
   */
  private static void checkTraceHeadersAndSetContextAsSpanInScope(Tracer tracer,
      TraceContextOrSamplingFlags extracted) {

    if (MDC.get(MessageTraceHeaders.TRACE_ID) != null && extracted.context() != null
        && !MDC.get(MessageTraceHeaders.TRACE_ID).equals(extracted.context().traceIdString())) {

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
