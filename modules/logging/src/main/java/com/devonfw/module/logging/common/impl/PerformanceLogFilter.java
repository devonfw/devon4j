package com.devonfw.module.logging.common.impl;

import static net.logstash.logback.argument.StructuredArguments.v;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Request logging filter that measures the execution time of a request.
 *
 * @since 1.5.0
 */
public class PerformanceLogFilter implements Filter {

  private static final Logger LOG = LoggerFactory.getLogger(PerformanceLogFilter.class);

  /**
   * Optional filter to only measure execution time of requests that match the filter.
   */
  private String urlFilter;

  /**
   * The constructor.
   */
  public PerformanceLogFilter() {

    super();
  }

  @Override
  public void init(FilterConfig config) throws ServletException {

    this.urlFilter = null;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    long startTime;
    String path = ((HttpServletRequest) request).getServletPath();
    String url = ((HttpServletRequest) request).getRequestURL().toString();

    if (this.urlFilter == null || path.matches(this.urlFilter)) {
      startTime = System.nanoTime();
      try {
        chain.doFilter(request, response);
        logPerformance(response, startTime, url, null);
      } catch (Throwable error) {
        logPerformance(response, startTime, url, error);
      }
    } else {
      chain.doFilter(request, response);
    }
  }

  /**
   * Logs the request URL, execution time. In case of an error also logs class name and error
   * message.
   *
   * @param response - the {@link ServletResponse}
   * @param startTime - start time of the {@link #doFilter(ServletRequest, ServletResponse, FilterChain)} function
   * @param url - requested URL
   * @param error - error thrown by the requested servlet, {@code null} if execution did not cause an error
   */
  private void logPerformance(ServletResponse response, long startTime, String url, Throwable error) {

    long endTime, duration;
    int statusCode = ((HttpServletResponse) response).getStatus();
    endTime = System.nanoTime();
    duration = TimeUnit.MILLISECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);

    String errorClass = "";
    String errorMessage = "";
    if (error != null) {
      statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
      errorClass = error.getClass().getName();
      errorMessage = error.getMessage();
    }
    LOG.info("{};{};{};{};{}", v("url", url), v("ms", duration), v("sc", statusCode), v("errCls", errorClass),
        v("errMsg", errorMessage));
  }

  @Override
  public void destroy() {

    // nothing to do...
  }

}
