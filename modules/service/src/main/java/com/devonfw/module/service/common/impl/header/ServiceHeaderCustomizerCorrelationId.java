package com.devonfw.module.service.common.impl.header;

import org.slf4j.MDC;

import com.devonfw.module.logging.common.api.LoggingConstants;
import com.devonfw.module.logging.common.impl.DiagnosticContextFilter;
import com.devonfw.module.service.common.api.header.ServiceHeaderContext;
import com.devonfw.module.service.common.api.header.ServiceHeaderCustomizer;

/**
 * Implementation of {@link ServiceHeaderCustomizer} that passes the {@link LoggingConstants#CORRELATION_ID} to a
 * subsequent {@link com.devonfw.module.service.common.api.Service} invocation.
 *
 * @since 3.0.0
 */
public class ServiceHeaderCustomizerCorrelationId implements ServiceHeaderCustomizer {

  private final String headerName;

  /**
   * The constructor.
   */
  public ServiceHeaderCustomizerCorrelationId() {

    this(DiagnosticContextFilter.CORRELATION_ID_HEADER_NAME_DEFAULT);
  }

  /**
   * The constructor.
   *
   * @param headerName the name of the header for the correlation ID.
   */
  public ServiceHeaderCustomizerCorrelationId(String headerName) {

    super();
    this.headerName = headerName;
  }

  @Override
  public void addHeaders(ServiceHeaderContext<?> context) {

    String correlationId = MDC.get(LoggingConstants.CORRELATION_ID);
    if ((correlationId != null) && (!correlationId.isEmpty())) {
      context.setHeader(this.headerName, correlationId);
    }
  }

}
