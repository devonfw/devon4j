package com.devonfw.module.cxf.common.impl.server.soap;

import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.jws.WebService;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;

import com.devonfw.module.service.common.api.constants.ServiceConstants;

/**
 * {@link Configuration} for (REST or SOAP) services server using CXF.<br>
 *
 * Scans for spring beans that represent a SOAP service (JAX-WS web service) by checking for {@link WebService}
 * annotation. It will register all these {@link WebService}s to CXF using their spring bean name as URL path. Hence you
 * should annotate your {@link WebService} implementation using {@link javax.inject.Named} providing a reasonable name:
 *
 * <pre>
 * &#64;{@link javax.inject.Named}("MyWebService")
 * &#64;{@link WebService}(endpointInterface = "my.package.MyWebService")
 * public class MyWebServiceImpl implements MyWebService {
 *   ...
 * }
 * </pre>
 *
 * @since 3.0.0
 */
@Configuration
@EnableWs
public class CxfServerSoapAutoConfiguration extends WsConfigurerAdapter {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(CxfServerSoapAutoConfiguration.class);

  @Inject
  private SpringBus springBus;

  @Inject
  private ConfigurableApplicationContext applicationContext;

  /**
   * @see CxfServerSoapAutoConfiguration
   * @return will always return {@code null}.
   */
  @Bean
  public Object registerWebServices() {

    Map<String, Object> webServiceMap = this.applicationContext.getBeansWithAnnotation(WebService.class);
    if (webServiceMap.isEmpty()) {
      LOG.info("No SOAP Services have been found. SOAP Endpoint will not be enabled in CXF.");
      return null;
    }
    ConfigurableListableBeanFactory beanFactory = this.applicationContext.getBeanFactory();
    for (Entry<String, Object> serviceEntry : webServiceMap.entrySet()) {
      EndpointImpl endpoint = new org.apache.cxf.jaxws.EndpointImpl(this.springBus, serviceEntry.getValue());
      endpoint.publish("/" + ServiceConstants.URL_FOLDER_WEB_SERVICE + "/" + serviceEntry.getKey());
      beanFactory.registerSingleton(serviceEntry.getKey() + "Endpoint", endpoint);
    }
    return null;
  }
}