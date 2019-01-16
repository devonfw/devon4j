package ${package}.general.service.impl.config;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.devonfw.module.logging.common.api.DiagnosticContextFacade;
import com.devonfw.module.logging.common.impl.DiagnosticContextFacadeImpl;
import com.devonfw.module.logging.common.impl.DiagnosticContextFilter;
import com.devonfw.module.logging.common.impl.PerformanceLogFilter;
import com.devonfw.module.service.common.api.constants.ServiceConstants;

/**
 * Registers a number of filters for web requests.
 */
@Configuration
public class WebConfig {

  private @Autowired AutowireCapableBeanFactory beanFactory;

  /**
   * @return the {@link FilterRegistrationBean} to register the {@link PerformanceLogFilter} that will log all requests
   *         with their duration and status code.
   */
  @Bean
  public FilterRegistrationBean performanceLogFilter() {

    FilterRegistrationBean registration = new FilterRegistrationBean();
    Filter performanceLogFilter = new PerformanceLogFilter();
    this.beanFactory.autowireBean(performanceLogFilter);
    registration.setFilter(performanceLogFilter);
    registration.addUrlPatterns("/*");
    registration.setOrder(2);
    return registration;
  }

  /**
   * @return the {@link DiagnosticContextFacade} implementation.
   */
  @Bean(name = "DiagnosticContextFacade")
  public DiagnosticContextFacade diagnosticContextFacade() {

    return new DiagnosticContextFacadeImpl();
  }

  /**
   * @return the {@link FilterRegistrationBean} to register the {@link DiagnosticContextFilter} that adds the
   *         correlation id as MDC so it will be included in all associated logs.
   */
  @Bean
  public FilterRegistrationBean diagnosticContextFilter() {

    FilterRegistrationBean registration = new FilterRegistrationBean();
    Filter diagnosticContextFilter = new DiagnosticContextFilter();
    this.beanFactory.autowireBean(diagnosticContextFilter);
    registration.setFilter(diagnosticContextFilter);
    registration.addUrlPatterns(ServiceConstants.URL_PATH_SERVICES + "/*");
    registration.setOrder(1);
    return registration;
  }

  /**
   * @return the {@link FilterRegistrationBean} to register the {@link CharacterEncodingFilter} to set the encoding.
   */
  @Bean
  public FilterRegistrationBean setCharacterEncodingFilter() {

    FilterRegistrationBean registration = new FilterRegistrationBean();
    CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
    characterEncodingFilter.setEncoding("UTF-8");
    characterEncodingFilter.setForceEncoding(false);
    this.beanFactory.autowireBean(characterEncodingFilter);
    registration.setFilter(characterEncodingFilter);
    registration.addUrlPatterns("/*");
    return registration;
  }
}