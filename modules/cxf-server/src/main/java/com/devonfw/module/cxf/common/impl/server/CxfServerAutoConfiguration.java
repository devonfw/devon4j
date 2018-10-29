package com.devonfw.module.cxf.common.impl.server;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.devonfw.module.service.common.api.constants.ServiceConstants;

/**
 * Basic {@link Configuration} for Apache CXF server.
 *
 * @since 3.0.0
 */
@Configuration
@ImportResource({ "classpath:META-INF/cxf/cxf.xml" })
public class CxfServerAutoConfiguration {

  /**
   * @return the {@link ServletRegistrationBean} to register {@link CXFServlet} according to Devon4j conventions at
   *         {@link ServiceConstants#URL_PATH_SERVICES}.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Bean
  public ServletRegistrationBean servletRegistrationBean() {

    CXFServlet cxfServlet = new CXFServlet();
    ServletRegistrationBean servletRegistration = new ServletRegistrationBean(cxfServlet,
        ServiceConstants.URL_PATH_SERVICES + "/*");
    return servletRegistration;
  }

}