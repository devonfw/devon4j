package com.devonfw.module.cxf.common.impl.server.rest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.json.common.base.ObjectMapperFactory;
import com.devonfw.module.rest.common.api.RestService;
import com.devonfw.module.rest.service.impl.RestServiceExceptionFacade;
import com.devonfw.module.service.common.api.constants.ServiceConstants;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * {@link Configuration} for REST (JAX-RS) services server using Apache CXF.
 *
 * @since 3.0.0
 */
@Configuration
public class CxfServerRestAutoConfiguration {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(CxfServerRestAutoConfiguration.class);

  @Value("${security.expose.error.details:false}")
  private boolean exposeInternalErrorDetails;

  @Value("${service.rest.find-by-name:true}")
  private boolean findRestServicesByName;

  @Inject
  private ConfigurableApplicationContext applicationContext;

  @Inject
  private SpringBus springBus;

  @Inject
  private ObjectMapperFactory objectMapperFactory;

  /**
   * @return the {@link Server} that provides the REST (JAX-RS) services.
   */
  @Bean
  public Server jaxRsServer() {

    List<Object> restServices = findRestServices();
    if (restServices.isEmpty()) {
      LOG.info("No REST Services have been found. Rest Endpoint will not be enabled in CXF.");
      return null;
    }
    List<Object> providers = findRestProviders();

    JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
    factory.setBus(this.springBus);
    factory.setAddress("/" + ServiceConstants.URL_FOLDER_REST);
    factory.setServiceBeans(restServices);
    factory.setProviders(providers);
    return factory.create();
  }

  /**
   * @return the {@link JacksonJsonProvider} for JSON support in REST (JAX-RS) services.
   */
  @Bean
  public JacksonJsonProvider jacksonJsonProvider() {

    return new JacksonJsonProvider(this.objectMapperFactory.createInstance());
  }

  /**
   * @return the {@link RestServiceExceptionFacade} used to handle {@link Exception}s during REST calls on the
   *         server-side and map them to according status code and JSON result.
   */
  @Bean
  public RestServiceExceptionFacade restServiceExceptionFacade() {

    RestServiceExceptionFacade exceptionFacade = new RestServiceExceptionFacade();
    exceptionFacade.setExposeInternalErrorDetails(this.exposeInternalErrorDetails);
    return exceptionFacade;
  }

  /**
   * @return the {@link List} of REST (JAX-RS) {@link Provider}s.
   */
  protected List<Object> findRestProviders() {

    return new ArrayList<>(this.applicationContext.getBeansWithAnnotation(Provider.class).values());
  }

  /**
   * @return the {@link List} of {@link RestService}s.
   */
  protected List<Object> findRestServices() {

    if (this.findRestServicesByName) {
      List<Object> result = new ArrayList<>();
      for (String beanName : this.applicationContext.getBeanDefinitionNames()) {
        if (beanName.contains("RestService")) {
          result.add(this.applicationContext.getBean(beanName));
        }
      }
      return result;
    } else {
      return new ArrayList<Object>(this.applicationContext.getBeansOfType(RestService.class).values());
    }
  }

}