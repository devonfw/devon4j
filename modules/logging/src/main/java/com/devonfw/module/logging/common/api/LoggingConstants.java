package com.devonfw.module.logging.common.api;

/**
 * Central constants for logging.
 *
 */
public final class LoggingConstants {

  /**
   * The key for the correlation id used as unique identifier to correlate log entries of a processing task. It allows
   * to track down all related log messages for that task across the entire application landscape (e.g. in case of a
   * problem).
   *
   * @see DiagnosticContextFacade#setCorrelationId(String)
   */
  public static final String CORRELATION_ID = "correlationId";

  /**
   * SPAN ID
   */
  public static final String SPAN_ID = "spanId";

  /**
   * TRACEID
   */
  public static final String TRACE_ID = "traceId";

  /**
   * SPANNAME
   */
  public static final String SPAN_NAME = "spanName";

  /**
   * PARENT_ID
   */
  public static final String PARENT_ID = "parentSpanId";

  /**
   * Construction prohibited.
   */
  private LoggingConstants() {

    super();
  }

}
