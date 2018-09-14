package com.devonfw.module.logging.common.api;

/**
 * This is the interface for a simple facade to write data into the {@link org.slf4j.MDC mapped diagnostic context}. As
 * additional value you can easily hook in custom extensions without interfering the logger implementation. A use case
 * may be to provide diagnostic informations also to additional components such as a performance monitoring module.
 * Therefore setting diagnostic information from Devon4j code is always indirected via this interface so the
 * implementation can be extended or replaced (what is not as easy for {@link org.slf4j.MDC#put(String, String) static
 * methods}).
 *
 */
public interface DiagnosticContextFacade {

  /**
   * @return the current {@link LoggingConstants#CORRELATION_ID correlation ID} or {@code null} if not
   *         {@link #setCorrelationId(String) set}.
   */
  String getCorrelationId();

  /**
   * Sets the {@link LoggingConstants#CORRELATION_ID correlation ID} for the current processing and thread.
   *
   * @param correlationId is the {@link LoggingConstants#CORRELATION_ID correlation ID} as unique identifier for the
   *        current processing task.
   */
  void setCorrelationId(String correlationId);

  /**
   * Removes the {@link #setCorrelationId(String) correlation ID} from the diagnostic context.
   */
  void removeCorrelationId();

}
