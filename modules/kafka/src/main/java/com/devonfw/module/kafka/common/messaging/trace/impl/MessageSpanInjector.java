package com.devonfw.module.kafka.common.messaging.trace.impl;

import static brave.internal.HexCodec.toLowerHex;

import java.util.Optional;
import java.util.UUID;

import org.springframework.util.ObjectUtils;

import com.devonfw.module.kafka.common.messaging.impl.MessageKafkaPayloadBuilder;
import com.devonfw.module.logging.common.api.DiagnosticContextFacade;
import com.devonfw.module.logging.common.impl.DiagnosticContextFacadeImpl;

import brave.propagation.TraceContext;

/**
 * @author ravicm
 *
 */
public class MessageSpanInjector implements TraceContext.Injector<MessageKafkaPayloadBuilder<?>> {

  private DiagnosticContextFacade diagnosticContextFacade = new DiagnosticContextFacadeImpl();

  @Override
  public void inject(TraceContext traceContext, MessageKafkaPayloadBuilder<?> payloadBuilder) {

    if (ObjectUtils.isEmpty(traceContext)) {
      return;
    }

    // payloadBuilder.header(MessageTraceHeaders.TRACE_ID_NAME, traceContext.traceIdString());
    if (this.diagnosticContextFacade.getCorrelationId() == null) {
      this.diagnosticContextFacade.setCorrelationId(UUID.randomUUID().toString());
    }
    payloadBuilder.header(MessageTraceHeaders.TRACE_ID_NAME, this.diagnosticContextFacade.getCorrelationId());

    payloadBuilder.header(MessageTraceHeaders.SPAN_ID_NAME, toLowerHex(traceContext.spanId()));

    Long parentId = traceContext.parentId();

    Optional.ofNullable(parentId).ifPresent(
        id -> payloadBuilder.header(MessageTraceHeaders.PARENT_ID_NAME, toLowerHex(traceContext.parentIdAsLong())));
    // if (parentId != null) {
    // payloadBuilder.header(MessageTraceHeaders.PARENT_ID_NAME, toLowerHex(traceContext.parentIdAsLong()));
    // }
  }

}
