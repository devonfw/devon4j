package com.devonfw.module.kafka.common.messaging.trace.impl;

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
 * A support class for {@link Tracer} to start and finish the span.
 *
 */
public final class MessageTraceSupport {

  private static final Logger LOG = LoggerFactory.getLogger(MessageTraceSupport.class);

  private MessageTraceSupport() {

  }

  /**
   * This method is used to start the span of {@link Tracer}.
   *
   * @param <K> the key type
   * @param <V> the value type
   *
   * @param kafkaRecord the {@link ConsumerRecord}
   * @param spanExtractor {@link MessageSpanExtractor}
   */
  public static <K, V> void startSpan(ConsumerRecord<K, V> kafkaRecord, MessageSpanExtractor<K, V> spanExtractor) {

    Tracer tracer = Tracing.currentTracer();

    if (ObjectUtils.isEmpty(tracer)) {
      return;
    }

    TraceContextOrSamplingFlags extracted = spanExtractor.extract(kafkaRecord);
    if (extracted != null) {
      checkTraceHeadersAndSetContextAsSpanInScope(tracer, extracted);
    } else {
      getCurrentSpanAndLog(kafkaRecord, tracer);
    }

  }

  private static <K, V> void getCurrentSpanAndLog(ConsumerRecord<K, V> kafkaRecord, Tracer tracer) {

    Span span = tracer.currentSpan();

    LOG.warn(EventKey.MESSAGE_WITHOUT_TRACEID.getMessage(),
        new String(kafkaRecord.headers().lastHeader(KafkaHeaders.MESSAGE_KEY).value()),
        span.context().traceIdString());
  }

  private static void checkTraceHeadersAndSetContextAsSpanInScope(Tracer tracer,
      TraceContextOrSamplingFlags extracted) {

    if (MDC.get(MessageTraceHeaders.TRACE_ID) != null && extracted.context() != null
        && !MDC.get(MessageTraceHeaders.TRACE_ID).equals(extracted.context().traceIdString())) {

      tracer.withSpanInScope(tracer.joinSpan(extracted.context()));
    }
  }

  /**
   * This method is used to finish the current span of {@link Tracer}
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
