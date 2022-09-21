package ${package}.general.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import ${package}.general.common.json.ApplicationObjectMapperFactory;
import javax.inject.Inject;

/**
 * {@link Configuration} for REST (JAX-RS) services.
 */
@Configuration
public class JaxRsConfig {

  @Inject
  private ApplicationObjectMapperFactory objectMapperFactory;

  /**
   * @return the {@link JacksonJsonProvider} for JSON and XML support in REST (JAX-RS) services.
   */
  @Bean
  public JacksonJsonProvider jacksonJsonProvider() {

    return new JacksonJsonProvider(this.objectMapperFactory.createInstance());
  }

}
