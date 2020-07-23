package com.devonfw.module.logging.common.impl;

import org.slf4j.MDC;

import com.devonfw.module.logging.common.api.DiagnosticContextFacade;
import com.devonfw.module.logging.common.api.LoggingConstants;

/**
 * This is the simple and straight forward implementation of {@link DiagnosticContextFacade}.
 *
 */
public class DiagnosticContextFacadeImpl implements DiagnosticContextFacade {

  /**
   * The constructor.
   */
  public DiagnosticContextFacadeImpl() {

    super();
  }

  @Override
  public String getCorrelationId() {

    return MDC.get(LoggingConstants.CORRELATION_ID);
  }

  @Override
  public void setCorrelationId(String correlationId) {

    MDC.put(LoggingConstants.CORRELATION_ID, correlationId);
  }

  @Override
  public void removeCorrelationId() {

    MDC.remove(LoggingConstants.CORRELATION_ID);
  }

  @Override
  public void setTraceId(String traceId) {

    MDC.put(LoggingConstants.TRACE_ID, traceId);

  }

  @Override
  public void setSpanId(String spanId) {

    MDC.put(LoggingConstants.SPAN_ID, spanId);
  }

  @Override
  public void removeTraceId() {

    MDC.remove(LoggingConstants.TRACE_ID);

  }

  @Override
  public void removeSpanId() {

    MDC.remove(LoggingConstants.SPAN_ID);

  }

  @Override
  public String getTraceId() {

    return MDC.get(LoggingConstants.TRACE_ID);
  }

  @Override
  public String getSpanId() {

    return MDC.get(LoggingConstants.SPAN_ID);
  }

}
