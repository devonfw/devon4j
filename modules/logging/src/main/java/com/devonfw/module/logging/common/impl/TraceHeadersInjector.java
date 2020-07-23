package com.devonfw.module.logging.common.impl;

import com.devonfw.module.logging.common.api.DiagnosticContextFacade;

import brave.propagation.TraceContext;

/**
 *
 */
public class TraceHeadersInjector implements TraceContext.Injector<DiagnosticContextFacade> {

  @Override
  public void inject(TraceContext traceContext, DiagnosticContextFacade contextFacade) {

    String traceId = traceContext.traceIdString();
    contextFacade.setTraceId(traceId);

    String spanId = traceContext.spanIdString();
    contextFacade.setSpanId(spanId);

  }

}
