package com.devonfw.module.logging.common.impl;

import java.io.IOException;
import java.util.UUID;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import com.devonfw.module.logging.common.api.DiagnosticContextFacade;

/**
 * Request logging filter that adds the request log message to the SLF4j mapped diagnostic context (MDC) before the
 * request is processed, removing it again after the request is processed.
 *
 */
public class DiagnosticContextFilter implements Filter {

  private static final Logger LOG = LoggerFactory.getLogger(DiagnosticContextFilter.class);

  /**
   * The name of the {@link FilterConfig#getInitParameter(String) init parameter} for
   * {@link #setCorrelationIdHttpHeaderName(String)}.
   */
  private static final String CORRELATION_ID_HEADER_NAME_PARAM = "correlationIdHeaderName";

  /** The default value for {@link #setCorrelationIdHttpHeaderName(String)}. */
  public static final String CORRELATION_ID_HEADER_NAME_DEFAULT = "X-Correlation-Id";

  /** @see #setCorrelationIdHttpHeaderName(String) */
  private String correlationIdHttpHeaderName;

  @Autowired
  private WebApplicationContext webApplicationContext;

  private DiagnosticContextFacade diagnosticContextFacade;

  /**
   * The constructor.
   */
  public DiagnosticContextFilter() {

    super();
    this.correlationIdHttpHeaderName = CORRELATION_ID_HEADER_NAME_DEFAULT;
  }

  /**
   * @param correlationIdHttpHeaderName is the name of the {@link HttpServletRequest#getHeader(String) HTTP header} for
   *        the {@link com.devonfw.module.logging.common.api.LoggingConstants#CORRELATION_ID correlation ID}.
   */
  public void setCorrelationIdHttpHeaderName(String correlationIdHttpHeaderName) {

    this.correlationIdHttpHeaderName = correlationIdHttpHeaderName;
  }

  @Override
  public void destroy() {

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
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    setCorrelationId(request);
    try {
      chain.doFilter(request, response);
    } finally {
      this.diagnosticContextFacade.removeCorrelationId();
    }
  }

  private void setCorrelationId(ServletRequest request) {

    String correlationId = null;
    if (request instanceof HttpServletRequest && this.correlationIdHttpHeaderName != null) {
      correlationId = normalizeValue(((HttpServletRequest) request).getHeader(this.correlationIdHttpHeaderName));
      if (correlationId == null) {
        LOG.debug("No correlation ID found for HTTP header {}.", this.correlationIdHttpHeaderName);
      } else {
        this.diagnosticContextFacade.setCorrelationId(correlationId);
        LOG.debug("Using correlation ID {} from HTTP header {}.", correlationId, this.correlationIdHttpHeaderName);
        return;
      }
    }
    if (correlationId == null) {
      // potential fallback if initialized before this filter...
      correlationId = normalizeValue(this.diagnosticContextFacade.getCorrelationId());
      if (correlationId != null) {
        LOG.debug("Correlation ID was already set to {} before DiagnosticContextFilter has been invoked.",
            correlationId);
      } else {
        // no correlation ID present, create a unique ID
        correlationId = UUID.randomUUID().toString();
        this.diagnosticContextFacade.setCorrelationId(correlationId);
        LOG.debug("Created unique correlation ID {}.", correlationId);
      }
    }
  }

  /**
   * @param diagnosticContextFacade the diagnosticContextFacade to set
   */
  @Inject
  public void setDiagnosticContextFacade(DiagnosticContextFacade diagnosticContextFacade) {

    this.diagnosticContextFacade = diagnosticContextFacade;
  }

  @Override
  public void init(FilterConfig config) throws ServletException {

    String headerName = config.getInitParameter(CORRELATION_ID_HEADER_NAME_PARAM);
    if (headerName == null) {
      LOG.debug("Parameter {} not configured via filter config.", CORRELATION_ID_HEADER_NAME_PARAM);
    } else {
      this.correlationIdHttpHeaderName = headerName;
    }
    LOG.info("Correlation ID header initialized to: {}", this.correlationIdHttpHeaderName);
    if (this.diagnosticContextFacade == null) {
      try {
        // ATTENTION: We do not import these classes as we keep spring as an optional dependency.
        // If spring is not available in your classpath (e.g. some real JEE context) then this will produce a
        // ClassNotFoundException and use the fallback in the catch statement.
        ServletContext servletContext = config.getServletContext();
        org.springframework.web.context.WebApplicationContext springContext;
        springContext = org.springframework.web.context.support.WebApplicationContextUtils
            .getWebApplicationContext(servletContext);
        this.diagnosticContextFacade = springContext.getBean(DiagnosticContextFacade.class);
      } catch (Throwable e) {
        LOG.warn("DiagnosticContextFacade not defined in spring. Falling back to default", e);
        this.diagnosticContextFacade = new DiagnosticContextFacadeImpl();
      }
    }
  }

}
