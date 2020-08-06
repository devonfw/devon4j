package com.devonfw.module.service.common.base.client;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Internal class to log the performance, status-code and potential exception of a
 * {@link com.devonfw.module.service.common.api.client.ServiceClientFactory service invocation}.
 *
 * @since 2020.08.001
 */
public abstract class ServiceClientPerformanceLogger {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(ServiceClientPerformanceLogger.class);

  private ServiceClientPerformanceLogger() {

    super();
  }

  /**
   * @param startTime the {@link System#nanoTime() nano time} when the service invocation started.
   * @param service the invoked service (e.g. {@link Class} reflecting JAX-RS interface or service
   *        {@link java.lang.reflect.Method}).
   * @param target the target on which the service was invoked (typically URL).
   * @param statusCode the HTTP status code that has been received.
   * @param error the optional {@link Throwable} that has been catched or {@code null} if no error occurred.
   */
  public static void log(long startTime, Object service, String target, int statusCode, Throwable error) {

    long endTime = System.nanoTime();
    long nanos = endTime - startTime;
    String duration = Duration.ofNanos(nanos) + " (" + nanos + "ns)";
    String status = Integer.toString(statusCode);
    if ((error != null) || (statusCode >= 400)) {
      LOG.warn("Invoking service " + service + " at " + target + " took " + duration + " and failed with status "
          + status + ".", error);
    } else {
      LOG.info("Invoking service {} at {} took {} and succeded with status {}.", service, target, duration, status);
    }
  }

}
