package com.devonfw.module.beanmapping.common.impl.orika;

import com.devonfw.module.basic.common.api.entity.GenericEntity;
import com.devonfw.module.basic.common.api.to.AbstractEto;
import com.devonfw.module.basic.common.api.to.AbstractGenericEto;
import com.devonfw.module.basic.common.api.to.AbstractGenericEto.PersistentEntityAccess;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;

/**
 * {@link CustomMapper} to map from a persitent entity ({@code com.devonfw.module.jpa.dataaccess.api.PersistenceEntity})
 * to its corresponding {@link AbstractEto ETO} to solve {@link GenericEntity#getModificationCounter() modification
 * counter issue}.
 */
@SuppressWarnings("rawtypes")
public class CustomMapperEto extends CustomMapper<GenericEntity, AbstractGenericEto> {

  private static final EntityAccess ENTITY_ACCESS = new EntityAccess();

  /**
   * The constructor.
   */
  public CustomMapperEto() {

    super();

  }

  @SuppressWarnings("unchecked")
  @Override
  public void mapAtoB(GenericEntity source, AbstractGenericEto target, MappingContext context) {

    ENTITY_ACCESS.setPersistentEntity(target, source);
  }

  private static class EntityAccess extends PersistentEntityAccess {

    @Override
    protected <ID> void setPersistentEntity(AbstractGenericEto<ID> eto, GenericEntity<ID> persistentEntity) {

      super.setPersistentEntity(eto, persistentEntity);
    }

  }

}
