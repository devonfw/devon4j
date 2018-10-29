package com.devonfw.module.jpa.dataaccess.api;

import java.util.List;

import com.devonfw.module.basic.common.api.entity.PersistenceEntity;
import com.devonfw.module.basic.common.api.reference.Ref;
import com.devonfw.module.jpa.dataaccess.api.feature.FeatureForceIncrementModificationCounter;

/**
 * This is the interface for a <em>Data Access Object</em> (DAO). It acts as a manager responsible for the persistence
 * operations on a specific {@link PersistenceEntity entity} {@literal <E>}.<br/>
 * This is base interface contains the CRUD operations:
 * <ul>
 * <li>Create: call {@link #save(PersistenceEntity)} on a new entity.</li>
 * <li>Retrieve: use <code>find*</code> methods such as {@link #findOne(Object)}. More specific queries will be added in
 * dedicated DAO interfaces.</li>
 * <li>Update: done automatically by JPA vendor (hibernate) on commit or call {@link #save(PersistenceEntity)} to
 * {@link javax.persistence.EntityManager#merge(Object) merge} an entity.</li>
 * <li>Delete: call {@link #delete(PersistenceEntity)} or {@link #delete(Object)}.</li>
 * </ul>
 * For each (non-abstract) implementation of {@link PersistenceEntity entity} <code>MyEntity</code> you should create an
 * interface interface <code>MyEntityDao</code> that inherits from this {@link GenericDao} interface. Also you create an
 * implementation of that interface <code>MyEntityDaoImpl</code> that you derive from
 * {@link com.devonfw.module.jpa.dataaccess.base.AbstractGenericDao}.
 *
 * @param <ID> is the generic type if the {@link PersistenceEntity#getId() primary key}.
 * @param <E> is the generic type of the {@link PersistenceEntity}.
 *
 */
public interface GenericDao<ID, E extends PersistenceEntity<ID>> extends FeatureForceIncrementModificationCounter<E> {

  /**
   * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
   * entity instance completely.
   *
   * @param entity the {@link PersistenceEntity entity} to save
   * @return the saved entity
   */
  E save(E entity);

  /**
   * Saves all given entities.
   *
   * @param entities the {@link PersistenceEntity entities} to save
   */
  void save(Iterable<? extends E> entities);

  /**
   * Retrieves an entity by its id.
   *
   * @param id must not be {@literal null}.
   * @return the entity with the given id or {@literal null} if none found
   * @throws RuntimeException if the requested entity does not exists (use {@link #findOne(Object)} to prevent).
   */
  E find(ID id);

  /**
   * Retrieves an entity by its id.
   *
   * @param id must not be {@literal null}.
   * @return the entity with the given id or {@literal null} if none found
   * @throws IllegalArgumentException if {@code id} is {@code null}
   */
  E findOne(ID id) throws IllegalArgumentException;

  /**
   * @param reference the {@link Ref} to the {@link PersistenceEntity} to get. Typically an instance of
   *        {@link com.devonfw.module.basic.common.api.reference.IdRef}.
   * @return the {@link PersistenceEntity} as {@link javax.persistence.EntityManager#getReference(Class, Object)
   *         reference} for the given {@link Ref}. Will be {@code null} if the given {@link Ref} was {@code null}.
   */
  E get(Ref<ID, E> reference);

  /**
   * Returns whether an entity with the given id exists.
   *
   * @param id must not be {@literal null}.
   * @return true if an entity with the given id exists, {@literal false} otherwise
   */
  boolean exists(ID id);

  /**
   * Returns all instances of the type with the given IDs.
   *
   * @param ids are the IDs of all entities to retrieve e.g. as {@link java.util.List}.
   * @return an {@link Iterable} with all {@link PersistenceEntity entites} for the given <code>ids</code>.
   */
  List<E> findAll(Iterable<ID> ids);

  /**
   * Deletes the entity with the given id.
   *
   * @param id must not be {@literal null}.
   * @throws IllegalArgumentException in case the given {@code id} is {@code null}
   */
  void delete(ID id) throws IllegalArgumentException;

  /**
   * Deletes a given entity.
   *
   * @param entity the {@link PersistenceEntity entity} to delete
   */
  void delete(E entity);

  /**
   * Deletes the given entities.
   *
   * @param entities the {@link PersistenceEntity entities} to delete
   */
  void delete(Iterable<? extends E> entities);

}
