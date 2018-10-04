package com.devonfw.module.basic.common.api.reference;

import java.util.Objects;

/**
 * Generic implementation of {@link Ref}.
 *
 * @param <ID> generic type of {@link #getId() ID}.
 * @param <E> generic type of the referenced {@link com.devonfw.module.basic.common.api.entity.GenericEntity entity}.
 */
public class GenericIdRef<ID, E> implements Ref<ID, E> {

  private static final long serialVersionUID = 1L;

  private final ID id;

  /**
   * The constructor.
   *
   * @param id the {@link #getId() ID}.
   */
  public GenericIdRef(ID id) {

    super();
    Objects.requireNonNull(id, "id");
    this.id = id;
  }

  @Override
  public ID getId() {

    return this.id;
  }

  @Override
  public int hashCode() {

    return this.id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }
    GenericIdRef<?, ?> other = (GenericIdRef<?, ?>) obj;
    return Objects.equals(this.id, other.id);
  }

  @Override
  public String toString() {

    return this.id.toString();
  }

}
