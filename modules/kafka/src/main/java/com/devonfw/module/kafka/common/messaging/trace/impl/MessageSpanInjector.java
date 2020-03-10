package com.devonfw.module.kafka.common.messaging.trace.impl;

import static brave.internal.HexCodec.toLowerHex;
import static com.devonfw.module.kafka.common.messaging.util.MessageUtil.addHeaderValue;

import java.util.Optional;

import org.apache.kafka.common.header.Headers;
import org.springframework.util.ObjectUtils;

import com.devonfw.module.logging.common.api.DiagnosticContextFacade;

import brave.propagation.TraceContext;

/**
 * @author ravicm
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
   * @param diagnosticContextFacade new value of {@link #getdiagnosticContextFacade}.
   */
  public void setDiagnosticContextFacade(DiagnosticContextFacade diagnosticContextFacade) {

    this.diagnosticContextFacade = diagnosticContextFacade;
  }

}
