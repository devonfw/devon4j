package com.devonfw.module.beanmapping.common.base;

import org.springframework.context.annotation.Bean;

import com.devonfw.module.basic.common.api.entity.GenericEntity;
import com.devonfw.module.basic.common.api.to.AbstractEto;
import com.devonfw.module.beanmapping.common.api.BeanMapper;
import com.devonfw.module.beanmapping.common.impl.orika.BeanMapperImplOrika;
import com.devonfw.module.beanmapping.common.impl.orika.CustomMapperEto;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * Base configuration for Orika {@link MapperFacade}.
 *
 * @see #getOrika()
 * @see #configureCustomMapping(MapperFactory)
 */
public class BaseOrikaConfig {

  /**
   * @return the {@link BeanMapper} implementation.
   */
  @Bean
  public BeanMapper getBeanMapper() {

    return new BeanMapperImplOrika();
  }

  /**
   * @return the Orika {@link MapperFacade} required by
   *         {@link com.devonfw.module.beanmapping.common.impl.orika.BeanMapperImplOrika}.
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
    factory.classMap(GenericEntity.class, AbstractEto.class).customize(customMapper).byDefault().favorExtension(true)
        .register();
    return factory;
  }

}
