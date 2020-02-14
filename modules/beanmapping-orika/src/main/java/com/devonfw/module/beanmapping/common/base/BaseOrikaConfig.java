package com.devonfw.module.beanmapping.common.base;

import com.devonfw.module.basic.common.api.entity.GenericEntity;
import com.devonfw.module.basic.common.api.to.AbstractEto;
import com.devonfw.module.beanmapping.common.impl.orika.CustomMapperEto;

import ma.glasnost.orika.MapperFactory;

/**
 * This is a base class which will add Custom Mapping for Entity to ETO. The method
 * {@link #configureCustomMapping(MapperFactory)} can be overridden as per requirements
 *
 */
public class BaseOrikaConfig {
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
