package com.devonfw.module.basic.common.api.reference;

import net.sf.mmm.util.lang.api.Datatype;

/**
 * Interface for a reference to an {@link net.sf.mmm.util.entity.api.GenericEntity entity} via its {@link #getId() ID}.
 * In most cases you want to use {@link IdRef}.
 *
 * @param <ID> generic type of {@link #getId() ID}.
 * @param <E> generic type of the referenced {@link net.sf.mmm.util.entity.api.Entity}. For flexibility not technically
 *        bound to {@link net.sf.mmm.util.entity.api.Entity} so it can also be used for an external entity not
 *        satisfying any requirements.
 */
public interface Ref<ID, E> extends Datatype {

  /**
   * @return the ({@link net.sf.mmm.util.entity.api.GenericEntity#getId() ID} of the referenced
   *         {@link net.sf.mmm.util.entity.api.Entity}.
   */
  ID getId();

}
