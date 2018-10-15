package com.devonfw.module.jpa.dataaccess.api.feature;

/**
 * Feature for DAO or repository to {@link #forceIncrementModificationCounter(Object)}.
 *
 * @param <E> generic type of the managed {@link com.devonfw.module.basic.common.api.entity.PersistenceEntity entity}.
 *        Typically implementing {@link com.devonfw.module.basic.common.api.entity.PersistenceEntity}.
 *
 * @since 3.0.0
 */
public interface FeatureForceIncrementModificationCounter<E> {

  /**
   * Enforces to increment the {@link com.devonfw.module.basic.common.api.entity.PersistenceEntity#getModificationCounter()
   * modificationCounter} e.g. to enforce that a parent object gets locked when its children are modified.<br>
   * As an example we assume that we have the two optimistic locked entities {@code Order} and its contained
   * {@code OrderPosition}. By default the users can modify an {@code Order} and each of its {@code OrderPosition}s in
   * parallel without getting a locking conflict. This can be desired. However, it can also be a demand that an
   * {@code Order} gets approved and the user doing that is responsible for the total price as the sum of the prices of
   * each {@code OrderPosition}. Now if another user is adding or changing an {@code OrderPostion} belonging to that
   * {@code Order} in parallel the {@code Order} will get approved but the approved total price will differ from what
   * the user has actually seen when he clicked on approve. To prevent this the use-case to modify an
   * {@code OrderPosition} can use this method to trigger a locking on the associated {@code Order}. The implication is
   * also that two users changing an {@code OrderPosition} associated with the same {@code Order} in parallel will get a
   * conflict.
   *
   * @param entity that is getting checked.
   */
  void forceIncrementModificationCounter(E entity);

}
