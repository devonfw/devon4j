package com.devonfw.module.beanmapping.common.impl.orika;

import com.devonfw.module.basic.common.api.entity.GenericEntity;
import com.devonfw.module.basic.common.api.to.AbstractEto;
import com.devonfw.module.beanmapping.common.api.BeanMapper;
import com.devonfw.module.beanmapping.common.impl.AbstractBeanMapperTest;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * Test of {@link BeanMapperImplOrika} based on {@link AbstractBeanMapperTest}.
 */
public class BeanMapperImplOrikaTest extends AbstractBeanMapperTest {

  @Override
  protected BeanMapper getBeanMapper() {

    BeanMapperImplOrika mapper = new BeanMapperImplOrika();
    MapperFactory factory = new DefaultMapperFactory.Builder().build();
    CustomMapperEto customMapper = new CustomMapperEto();
    factory.classMap(GenericEntity.class, AbstractEto.class).customize(customMapper).byDefault().favorExtension(true)
        .register();
    MapperFacade orika = factory.getMapperFacade();
    mapper.setOrika(orika);
    return mapper;
  }
}
