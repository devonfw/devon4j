package ${package}.general.common.beanmapping;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ${package}.general.common.entity.GenericEntity;
import ${package}.general.common.to.AbstractEto;
import ${package}.general.common.to.AbstractGenericEto;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * Java bean configuration for Orika. The method {@link #configureCustomMapping(MapperFactory)} from
 * {@link BaseOrikaConfig} can be overridden as per requirements.
 */
@Configuration
public class BeansOrikaConfig {

  /**
   * @return the {@link BeanMapper} implementation.
   */
  @Bean
  public BeanMapper getBeanMapper() {

    return new BeanMapperImpl();
  }

  /**
   * @return the Orika {@link MapperFacade} required by
   *         {@link ${package}.general.common.beanmapping.BeanMapperImpl.common.impl.orika.BeanMapperImplOrika}.
   */
  @Bean
  public MapperFacade getOrika() {

    MapperFactory factory = new DefaultMapperFactory.Builder().build();
    MapperFacade orika = configureCustomMapping(factory).getMapperFacade();
    return orika;
  }

  /**
   * Configures the Orika {@link MapperFacade}. By default it adds the custom mapping for {@link GenericEntity} to
   * {@link AbstractEto} via {@link CustomMapperEto}. You may override to extend or replace the configuration.
   *
   * @param factory the {@link MapperFacade} to configure.
   * @return {@link MapperFactory}
   */
  protected MapperFactory configureCustomMapping(MapperFactory factory) {

    CustomMapperEto customMapper = new CustomMapperEto();
    factory.classMap(GenericEntity.class, AbstractGenericEto.class).customize(customMapper).byDefault()
        .favorExtension(true).register();
    return factory;
  }

}
