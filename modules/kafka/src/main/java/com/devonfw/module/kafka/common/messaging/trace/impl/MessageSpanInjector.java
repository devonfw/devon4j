package com.devonfw.module.kafka.common.messaging.trace.impl;

import static brave.internal.HexCodec.toLowerHex;
import static com.devonfw.module.kafka.common.messaging.util.MessageUtil.addHeaderValue;

import java.util.Optional;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.util.ObjectUtils;

import com.devonfw.module.logging.common.api.DiagnosticContextFacade;
import com.devonfw.module.logging.common.api.LoggingConstants;

import brave.propagation.TraceContext;

/**
 * This is an implementation class for the {@link brave.propagation.TraceContext.Injector} used to inject the
 * {@link MessageTraceHeaders} to the {@link ProducerRecord#headers()}
 *
 */
public class MessageSpanInjector implements TraceContext.Injector<Headers> {

  private DiagnosticContextFacade diagnosticContextFacade;

  @Override
  public void inject(TraceContext traceContext, Headers headers) {

    if (ObjectUtils.isEmpty(traceContext)) {
      return;
    }

    addHeaderValue(headers, MessageTraceHeaders.TRACE_ID, this.diagnosticContextFacade.getCorrelationId());
    addHeaderValue(headers, MessageTraceHeaders.SPAN_ID, toLowerHex(traceContext.spanId()));

    Long parentId = traceContext.parentId();

    Optional.ofNullable(parentId).ifPresent(
        id -> addHeaderValue(headers, MessageTraceHeaders.PARENT_ID, toLowerHex(traceContext.parentIdAsLong())));
  }

  /**
   * Set the {@link DiagnosticContextFacade} to retrieve the {@link LoggingConstants#CORRELATION_ID}
   *
   * @param diagnosticContextFacade the {@link DiagnosticContextFacade}.
   */
  public void setDiagnosticContextFacade(DiagnosticContextFacade diagnosticContextFacade) {

    this.diagnosticContextFacade = diagnosticContextFacade;
  }

}
