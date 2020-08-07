package com.devonfw.module.service.common.api.client;

/**
 * Interface to {@link #unmarshall(String, String, int, String) unmarshall} a server-side {@link Exception} from its
 * payload data.
 *
 * @since 2020.08.001
 */
public interface ServiceClientErrorUnmarshaller {

  /**
   * @param data the marhsalled {@link Exception}.
   * @param format the content type of the {@code data} (typically "application/json").
   * @param statusCode the HTTP status code or {@code 0} if not available (no response was received).
   * @param service a description of the invoked service that failed (service name, operation, etc.).
   * @return the unmarshalled {@link RuntimeException}.
   */
  RuntimeException unmarshall(String data, String format, int statusCode, String service);
}
