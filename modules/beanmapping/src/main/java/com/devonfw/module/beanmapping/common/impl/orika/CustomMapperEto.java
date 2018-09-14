package com.devonfw.module.beanmapping.common.impl.orika;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;

import net.sf.mmm.util.entity.api.GenericEntity;
import net.sf.mmm.util.entity.api.PersistenceEntity;
import net.sf.mmm.util.pojo.descriptor.api.PojoDescriptor;
import net.sf.mmm.util.pojo.descriptor.api.PojoDescriptorBuilder;
import net.sf.mmm.util.pojo.descriptor.api.PojoDescriptorBuilderFactory;
import net.sf.mmm.util.pojo.descriptor.impl.PojoDescriptorBuilderFactoryImpl;
import net.sf.mmm.util.transferobject.api.EntityTo;

/**
 * {@link CustomMapper} to map from {@link PersistenceEntity} to {@link EntityTo} to solve
 * {@link EntityTo#getModificationCounter() modification counter issue}.
 *
 */
// @Named
@SuppressWarnings("rawtypes")
public class CustomMapperEto extends CustomMapper<GenericEntity, EntityTo> {

  private PojoDescriptorBuilder pojoDescriptorBuilder;

  private PojoDescriptorBuilderFactory pojoDescriptorBuilderFactory;

  private PojoDescriptor<EntityTo> descriptor;

  /**
   * The constructor.
   */
  public CustomMapperEto() {

    super();

  }

  @Override
  public void mapAtoB(GenericEntity source, EntityTo target, MappingContext context) {

    this.descriptor.setProperty(target, "persistentEntity", source);
  }

  /**
   * Initializes this class to be functional.
   */
  @PostConstruct
  public void initialize() {

    if (this.pojoDescriptorBuilderFactory == null) {
      this.pojoDescriptorBuilderFactory = PojoDescriptorBuilderFactoryImpl.getInstance();
    }
    if (this.pojoDescriptorBuilder == null) {
      this.pojoDescriptorBuilder = this.pojoDescriptorBuilderFactory.createPrivateFieldDescriptorBuilder();
    }
    this.descriptor = this.pojoDescriptorBuilder.getDescriptor(EntityTo.class);
  }

  /**
   * @param pojoDescriptorBuilderFactory the pojoDescriptorBuilderFactory to set
   */
  @Inject
  public void setPojoDescriptorBuilderFactory(PojoDescriptorBuilderFactory pojoDescriptorBuilderFactory) {

    this.pojoDescriptorBuilderFactory = pojoDescriptorBuilderFactory;
  }

}
