package com.devonfw.module.basic.common.api.reference;

import java.io.Serializable;

/**
 * Interface for a reference to an {@link com.devonfw.module.basic.common.api.entity.GenericEntity entity} via its
 * {@link #getId() ID}. In most cases you want to use {@link IdRef}.
 *
 * @param <ID> generic type of {@link #getId() ID}.
 * @param <E> generic type of the referenced {@link com.devonfw.module.basic.common.api.entity.GenericEntity entity}.
 *        For flexibility not technically bound to {@link com.devonfw.module.basic.common.api.entity.GenericEntity} so
 *        it can also be used for an external entity not satisfying any requirements.
 */
public interface Ref<ID, E> extends Serializable {

  /**
   * @return the ({@link com.devonfw.module.basic.common.api.entity.GenericEntity#getId() ID} of the referenced
   *         {@link com.devonfw.module.basic.common.api.entity.GenericEntity entity}.
   */
  ID getId();

}
