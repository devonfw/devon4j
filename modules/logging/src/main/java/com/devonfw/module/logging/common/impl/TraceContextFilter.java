package com.devonfw.module.logging.common.impl;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devonfw.module.logging.common.api.DiagnosticContextFacade;
import com.devonfw.module.logging.common.api.LoggingConstants;

import brave.Tracer;
import brave.propagation.TraceContext;

/**
 *
 */
public class TraceContextFilter implements Filter {

  private static final Logger LOG = LoggerFactory.getLogger(TraceContextFilter.class);

  /**
  *
  */
  public static final String TRACE_ID_HEADER_NAME_PARAM = "traceIdHeaderName";

  /**
  *
  */
  public static final String TRACE_ID_HEADER_NAME_DEFAULT = "X-Trace-Id";

  /**
  *
  */
  public static final String SPAN_ID_HEADER_NAME_PARAM = "spanIdHeaderName";

  /**
  *
  */
  public static final String SPAN_ID_HEADER_NAME_DEFAULT = "X-Span-Id";

  /** @see #setTraceIdHttpHeaderName(String) */
  private String traceIdHttpHeaderName;

  /** @see #setSpanIdHttpHeaderName(String) */
  private String spanIdHttpHeaderName;

  private DiagnosticContextFacade diagnosticContextFacade;

  private TraceHeadersInjector traceHeadersInjector;

  @Inject
  private Tracer tracer;

  /**
   * The constructor.
   */
  public TraceContextFilter() {

    super();
    this.traceIdHttpHeaderName = TRACE_ID_HEADER_NAME_DEFAULT;
    this.spanIdHttpHeaderName = SPAN_ID_HEADER_NAME_DEFAULT;
  }

  /**
   * @param traceIdHttpHeaderName
   */
  public void setTraceIdHttpHeaderName(String traceIdHttpHeaderName) {

    this.traceIdHttpHeaderName = traceIdHttpHeaderName;
  }

  /**
   * @param spanIdHeaderName
   */
  public void setSpanIdHeaderName(String spanIdHeaderName) {

    this.spanIdHttpHeaderName = spanIdHeaderName;
  }

  private static String normalizeValue(String value) {

    if (value != null) {
      String result = value.trim();
      if (!result.isEmpty()) {
        return result;
      }
    }
    return null;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

    String traceHeaderName = filterConfig.getInitParameter(TRACE_ID_HEADER_NAME_PARAM);
    String spanHeaderName = filterConfig.getInitParameter(SPAN_ID_HEADER_NAME_PARAM);

    if (traceHeaderName == null) {
      LOG.debug("Parameter {} not configured via filter config.", TRACE_ID_HEADER_NAME_PARAM);
    } else {
      this.traceIdHttpHeaderName = traceHeaderName;
    }

    if (spanHeaderName == null) {
      LOG.debug("Parameter {} not configured via filter config.", SPAN_ID_HEADER_NAME_PARAM);
    } else {
      this.spanIdHttpHeaderName = spanHeaderName;
    }

    LOG.info("trace ID header initialized to: {}", this.traceIdHttpHeaderName);
    LOG.info("span ID header initialized to: {}", this.spanIdHttpHeaderName);

    if (this.diagnosticContextFacade == null) {
      try {
        // ATTENTION: We do not import these classes as we keep spring as an optional dependency.
        // If spring is not available in your classpath (e.g. some real JEE context) then this will produce a
        // ClassNotFoundException and use the fallback in the catch statement.
        ServletContext servletContext = filterConfig.getServletContext();
        org.springframework.web.context.WebApplicationContext springContext;
        springContext = org.springframework.web.context.support.WebApplicationContextUtils
            .getWebApplicationContext(servletContext);
        this.diagnosticContextFacade = springContext.getBean(DiagnosticContextFacade.class);
      } catch (Throwable e) {
        LOG.warn("DiagnosticContextFacade not defined in spring. Falling back to default", e);
        this.diagnosticContextFacade = new DiagnosticContextFacadeImpl();
      }

    }

    if (this.traceHeadersInjector == null) {
      try {
        // ATTENTION: We do not import these classes as we keep spring as an optional dependency.
        // If spring is not available in your classpath (e.g. some real JEE context) then this will produce a
        // ClassNotFoundException and use the fallback in the catch statement.
        ServletContext servletContext = filterConfig.getServletContext();
        org.springframework.web.context.WebApplicationContext springContext;
        springContext = org.springframework.web.context.support.WebApplicationContextUtils
            .getWebApplicationContext(servletContext);
        this.traceHeadersInjector = springContext.getBean(TraceHeadersInjector.class);
      } catch (Throwable e) {
        LOG.warn("TraceHeadersInjector not defined in spring. Falling back to default", e);
        this.traceHeadersInjector = new TraceHeadersInjector();
      }

    }

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    setTraceAndSpanId(request);
    try {
      chain.doFilter(request, response);
    } finally {
      this.diagnosticContextFacade.removeSpanId();
      this.diagnosticContextFacade.removeTraceId();
    }

  }

  private void setTraceAndSpanId(ServletRequest request) {

    String traceId = null;
    String spanId = null;

    if (request instanceof HttpServletRequest && this.traceIdHttpHeaderName != null
        && this.spanIdHttpHeaderName != null) {

      traceId = normalizeValue(((HttpServletRequest) request).getHeader(this.traceIdHttpHeaderName));

      if (traceId == null) {
        LOG.debug("No trace ID found for HTTP header {}.", this.traceIdHttpHeaderName);
      } else {
        this.diagnosticContextFacade.setTraceId(traceId);
        LOG.debug("Using traceId ID {} from HTTP header {}.", traceId, this.traceIdHttpHeaderName);
        return;
      }

      spanId = normalizeValue(((HttpServletRequest) request).getHeader(this.spanIdHttpHeaderName));

      if (spanId == null) {
        LOG.debug("No spanId ID found for HTTP header {}.", this.spanIdHttpHeaderName);
      } else {
        this.diagnosticContextFacade.setSpanId(spanId);
        LOG.debug("Using spanId ID {} from HTTP header {}.", spanId, this.spanIdHttpHeaderName);
        return;
      }

    }

    if (traceId == null) {
      // potential fallback if initialized before this filter...
      traceId = normalizeValue(this.diagnosticContextFacade.getTraceId());
      spanId = normalizeValue(this.diagnosticContextFacade.getSpanId());

      if (traceId != null && spanId != null) {
        LOG.debug("Trace ID and Span ID was already set to {} and {} before TraceContextFilter has been invoked.",
            traceId, spanId);
      } else {
        // no traceId ID and span ID present, inject from trace context
        TraceContext context = getActiveTraceContext();
        this.traceHeadersInjector.inject(context, this.diagnosticContextFacade);
        LOG.debug("Injected trace ID {} and span ID {} .", context.traceIdString(), context.spanIdString());
      }
    }

  }

  private TraceContext getActiveTraceContext() {

    if (this.tracer.currentSpan() == null) {
      this.tracer.nextSpan().name(LoggingConstants.SPAN_NAME).start();
      return this.tracer.nextSpan().context();
    }

    return this.tracer.currentSpan().context();
  }

  @Override
  public void destroy() {

  }

}
