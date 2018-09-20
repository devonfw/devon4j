package com.devonfw.module.beanmapping.common.impl.orika;

import javax.inject.Inject;

import com.devonfw.module.beanmapping.common.base.AbstractBeanMapper;

import ma.glasnost.orika.MapperFacade;

/**
 * This is the implementation of {@link com.devonfw.module.beanmapping.common.api.BeanMapper} using orika
 * {@link MapperFacade}.
 *
 */
public class BeanMapperImplOrika extends AbstractBeanMapper {

  private MapperFacade orika;

  /**
   * The constructor.
   */
  public BeanMapperImplOrika() {

    super();
  }

  /**
   * @param orika the orika to set
   */
  @Inject
  public void setOrika(MapperFacade orika) {

    this.orika = orika;
  }

  @Override
  public <T> T map(Object source, Class<T> targetClass) {

    if (source == null) {
      return null;
    }
    return this.orika.map(source, targetClass);
  }

}
