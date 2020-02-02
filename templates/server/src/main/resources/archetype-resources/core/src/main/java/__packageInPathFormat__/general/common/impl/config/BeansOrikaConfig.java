package ${package}.general.common.impl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * Java bean configuration for Dozer
 */
@Configuration
@ComponentScan(basePackages = { "com.devonfw.module.beanmapping" })
public class BeansOrikaConfig {

 
  /**
   * @return the {@link DozerBeanMapper}.
   */
  @Bean
  public MapperFacade getOrika() {

    MapperFactory factory = new DefaultMapperFactory.Builder().build();
    MapperFacade orika = factory.getMapperFacade();
    return orika;
  }
}
