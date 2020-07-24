package com.devonfw.module.service.common.impl.header;

import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import com.devonfw.module.logging.common.api.LoggingConstants;
import com.devonfw.module.logging.common.impl.TraceContextFilter;
import com.devonfw.module.service.common.api.header.ServiceHeaderContext;
import com.devonfw.module.service.common.api.header.ServiceHeaderCustomizer;

/**
 *
 */
public class ServiceHeaderCustomizerTraceSpanId implements ServiceHeaderCustomizer {

  private final String traceIdHeaderName;

  private final String spanIdHeaderName;

  /**
   * The constructor.
   */
  public ServiceHeaderCustomizerTraceSpanId() {

    this(TraceContextFilter.TRACE_ID_HEADER_NAME_DEFAULT, TraceContextFilter.SPAN_ID_HEADER_NAME_DEFAULT);
  }

  /**
   * The constructor.
   *
   * @param traceIdHeaderName
   * @param spanIdHeaderName
   */
  public ServiceHeaderCustomizerTraceSpanId(String traceIdHeaderName, String spanIdHeaderName) {

    super();
    this.traceIdHeaderName = traceIdHeaderName;
    this.spanIdHeaderName = spanIdHeaderName;
  }

  @Override
  public void addHeaders(ServiceHeaderContext<?> context) {

    String traceId = MDC.get(LoggingConstants.TRACE_ID);
    String spanId = MDC.get(LoggingConstants.SPAN_ID);

    if (!StringUtils.isEmpty(traceId)) {
      context.setHeader(this.traceIdHeaderName, traceId);
    }

    if (!StringUtils.isEmpty(spanId)) {
      context.setHeader(this.spanIdHeaderName, spanId);
    }

  }

}
