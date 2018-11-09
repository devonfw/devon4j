package com.devonfw.module.basic.common.api.reference;

import com.devonfw.module.basic.common.api.entity.GenericEntity;

/**
 * A {@link Ref} using {@link Long} values as {@link #getId() ID}.
 *
 * @param <E> generic type of the referenced {@link GenericEntity entity}.
 */
public class IdRef<E> extends GenericIdRef<Long, E> {

  private static final long serialVersionUID = 1L;

  /**
   * The constructor.
   *
   * @param id the {@link #getId() ID}.
   */
  public IdRef(Long id) {

    super(id);
  }

  /**
   * @param <E> generic type of the referenced {@link GenericEntity}.
   * @param entity the {@link GenericEntity} to reference.
   * @return the {@link IdRef} pointing to the given {@link GenericEntity} or {@code null} if the {@link GenericEntity}
   *         or its {@link GenericEntity#getId() ID} is {@code null}.
   */
  public static <E extends GenericEntity<Long>> IdRef<E> of(E entity) {

    if (entity == null) {
      return null;
    }
    return of(entity.getId());
  }

  /**
   * @param <E> generic type of the referenced {@link GenericEntity}.
   * @param id the {@link #getId() ID} to wrap.
   * @return the {@link IdRef} pointing to an entity with the specified {@link #getId() ID} or {@code null} if the given
   *         {@code ID} was {@code null}.
   */
  public static <E> IdRef<E> of(Long id) {

    if (id == null) {
      return null;
    }
    return new IdRef<>(id);
  }

  /**
   * @param <E> generic type of the referenced {@link GenericEntity}.
   * @param id the {@link #getId() ID} to wrap.
   * @return the {@link IdRef} pointing to an entity with the specified {@link #getId() ID}.
   */
  public static <E> IdRef<E> of(long id) {

    return new IdRef<>(Long.valueOf(id));
  }

}
