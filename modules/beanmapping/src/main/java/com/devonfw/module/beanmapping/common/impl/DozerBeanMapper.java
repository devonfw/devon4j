package com.devonfw.module.beanmapping.common.impl;

import org.dozer.Mapper;

import com.devonfw.module.beanmapping.common.impl.dozer.BeanMapperImplDozer;

/**
 * This is the implementation of {@link com.devonfw.module.beanmapping.common.api.BeanMapper} using dozer {@link Mapper}.
 *
 * @deprecated - use {@link BeanMapperImplDozer} instead as this class name clashes with
 *             {@link org.dozer.DozerBeanMapper}.
 */
@Deprecated
public class DozerBeanMapper extends BeanMapperImplDozer {

  /**
   * The constructor.
   */
  public DozerBeanMapper() {

    super();
  }

}
