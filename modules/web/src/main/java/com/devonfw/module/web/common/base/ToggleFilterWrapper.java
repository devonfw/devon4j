package com.devonfw.module.web.common.base;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for wrapping a {@link Filter} and allows to be {@link #setEnabled(Boolean) disabled} e.g.
 * for development tests (e.g. via an application property). In case the filter gets {@link #setEnabled(Boolean)
 * disabled} a WARNING log message is produced and also written to {@link System#err}. <br/>
 *
 * As an example you can use it to wrap the {@codeCsrfFilter} in order to allow disabling in local tests (to ease REST
 * testing).
 */
public class ToggleFilterWrapper implements Filter {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(ToggleFilterWrapper.class);

  /**
   * The delegated Filter.
   */
  private Filter delegateFilter;

  /**
   * Is set if this filter is enabled.
   */
  private Boolean enabled = Boolean.FALSE;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  /**
   * Initializes this object.
   */
  @PostConstruct
  public void initialize() {

    if (!this.enabled) {
      String message = "****** FILTER " + this.delegateFilter
          + " HAS BEEN DISABLED! THIS FEATURE SHOULD ONLY BE USED IN DEVELOPMENT MODE ******";
      LOG.warn(message);
      // CHECKSTYLE:OFF (for development only)
      System.err.println(message);
      // CHECKSTYLE:ON
    }
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    if (this.enabled) {
      this.delegateFilter.doFilter(request, response, chain);
    } else {
      chain.doFilter(request, response);
    }
  }

  @Override
  public void destroy() {

  }

  /**
   * @param delegateFilter the filter to delegate to
   */
  public void setDelegateFilter(Filter delegateFilter) {

    this.delegateFilter = delegateFilter;
  }

  /**
   * @param enabled the enabled flag
   */
  public void setEnabled(Boolean enabled) {

    if (enabled != null) {
      this.enabled = enabled;
    } else {
      LOG.warn(this.delegateFilter + " - ToggleFilterWrapper#setEnabled should not be set to NULL");
    }
  }

  /**
   * @return disabled
   */
  public Boolean isEnabled() {

    return this.enabled;
  }

}
