package com.devonfw.module.beanmapping.common.base;

import org.springframework.context.annotation.Bean;

import com.devonfw.module.basic.common.api.entity.GenericEntity;
import com.devonfw.module.basic.common.api.to.AbstractEto;
import com.devonfw.module.beanmapping.common.impl.orika.CustomMapperEto;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * Base class which will add Custom Mapping for Entity to ETO. The method {@link #configureCustomMapping(MapperFactory)}
 * can be overridden as per requirements
 *
 */
public class BaseOrikaConfig {

  /**
   * @return {@link MapperFacade}
   */
  @Bean
  public MapperFacade getOrika() {

    MapperFactory factory = new DefaultMapperFactory.Builder().build();
    // {@link #configureCustomMapping(MapperFactory)} can be overridden as per requirements
    MapperFacade orika = configureCustomMapping(factory).getMapperFacade();
    return orika;
  }

  /**
   * Custom mapping for Entity to ETO
   *
   * @param factory
   * @return {@link MapperFactory}
   */
  protected MapperFactory configureCustomMapping(MapperFactory factory) {

    CustomMapperEto customMapper = new CustomMapperEto();
    factory.classMap(GenericEntity.class, AbstractEto.class).customize(customMapper).byDefault().favorExtension(true)
        .register();
    return factory;
  }

}
