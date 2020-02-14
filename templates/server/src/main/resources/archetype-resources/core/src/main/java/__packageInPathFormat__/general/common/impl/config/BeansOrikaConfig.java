package ${package}.general.common.impl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import com.devonfw.module.beanmapping.common.base.BaseOrikaConfig;

/**
 * Java bean configuration for Orika
 */
@Configuration
@ComponentScan(basePackages = { "com.devonfw.module.beanmapping" })
public class BeansOrikaConfig extends BaseOrikaConfig{

 
  /**
   * @return the {@link MapperFacade}.
   */
  @Bean
  public MapperFacade getOrika() {

    MapperFactory factory = new DefaultMapperFactory.Builder().build();
    //{@link #configureCustomMapping(MapperFactory)} can be overridden as per requirements
    MapperFacade orika = configureCustomMapping(factory).getMapperFacade();
    return orika;
  }
}
