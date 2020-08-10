package com.devonfw.module.service.common.api.client;

import java.util.UUID;

/**
 * Factory to {@link #create(Throwable, String, String, UUID, String)} {@link #unmarshall(String, String, int, String)
 * unmarshall} a server-side {@link Exception} from its payload data.
 *
 * @since 2020.08.001
 */
public interface ServiceClientErrorFactory {

  /**
   * @param cause the {@link RuntimeException#getCause() cause} of the exception. May be {@code null} if no initial
   *        cause is available.
   * @param message the raw error message (without context). May be the {@link Throwable#getMessage() message} of the
   *        {@link RuntimeException#getCause() cause}.
   * @param code the {@link net.sf.mmm.util.exception.api.NlsRuntimeException#getCode() error code}. May be {@code null}
   *        if not available.
   * @param uuid the {@link UUID} of an existing cause (e.g. if received from the server with error data).
   * @param service the contextual information of the invoked service that failed. See
   *        {@link com.devonfw.module.service.common.api.client.context.ServiceContext#getServiceDescription(String)}.
   * @return the new {@link RuntimeException} with the error details.
   */
  RuntimeException create(Throwable cause, String message, String code, UUID uuid, String service);

  /**
   * @param data the marshalled {@link Exception}.
   * @param format the content type of the {@code data} (typically "application/json").
   * @param statusCode the HTTP status code or {@code 0} if not available (no response was received).
   * @param service the contextual information of the invoked service that failed. See
   *        {@link com.devonfw.module.service.common.api.client.context.ServiceContext#getServiceDescription(String)}.
   * @return the unmarshalled {@link RuntimeException}.
   */
  RuntimeException unmarshall(String data, String format, int statusCode, String service);
}
