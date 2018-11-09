package com.devonfw.module.jpa.dataaccess.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devonfw.module.basic.common.api.entity.PersistenceEntity;
import com.devonfw.module.basic.common.api.reference.Ref;
import com.devonfw.module.jpa.dataaccess.api.GenericDao;
import com.devonfw.module.jpa.dataaccess.api.QueryHelper;

/**
 * This is the abstract base-implementation of the {@link GenericDao} interface.
 *
 * @param <ID> is the generic type if the {@link PersistenceEntity#getId() primary key}.
 * @param <E> is the generic type of the managed {@link PersistenceEntity}.
 */
public abstract class AbstractGenericDao<ID, E extends PersistenceEntity<ID>> extends QueryHelper
    implements GenericDao<ID, E> {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(AbstractGenericDao.class);

  private EntityManager entityManager;

  /**
   * The constructor.
   */
  public AbstractGenericDao() {

    super();
  }

  /**
   * @return the {@link Class} reflecting the managed entity.
   */
  protected abstract Class<E> getEntityClass();

  /**
   * @return the {@link EntityManager} instance.
   */
  protected EntityManager getEntityManager() {

    return this.entityManager;
  }

  /**
   * @param entityManager the {@link EntityManager} to inject.
   */
  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {

    this.entityManager = entityManager;
  }

  /**
   * @return the name of the managed entity.
   */
  protected String getEntityName() {

    return getEntityClass().getSimpleName();
  }

  @Override
  public E save(E entity) {

    if (isNew(entity)) {
      getEntityManager().persist(entity);
      LOG.debug("Saved new {} with id {}.", getEntityName(), entity.getId());
      return entity;
    } else {
      if (getEntityManager().find(entity.getClass(), entity.getId()) != null) {
        E update = getEntityManager().merge(entity);
        LOG.debug("Updated {} with id {}.", getEntityName(), entity.getId());
        return update;
      } else {
        throw new EntityNotFoundException("Entity not found");
      }
    }
  }

  /**
   * Determines if the given {@link PersistenceEntity} is new.
   *
   * @param entity is the {@link PersistenceEntity} to check.
   * @return {@code true} if new, {@code false} otherwise (e.g. managed or detached).
   */
  protected boolean isNew(E entity) {

    return entity.getId() == null;
  }

  @Override
  public void save(Iterable<? extends E> entities) {

    for (E entity : entities) {
      save(entity);
    }
  }

  @Override
  public void forceIncrementModificationCounter(E entity) {

    getEntityManager().lock(entity, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
  }

  @Override
  public E findOne(ID id) {

    E entity = getEntityManager().find(getEntityClass(), id);
    return entity;
  }

  @Override
  public E find(ID id) {

    E entity = findOne(id);
    if (entity == null) {
      throw new IllegalStateException(
          "Entity " + getEntityClass().getSimpleName() + " with ID '" + id + "' was not found!");
    }
    return entity;
  }

  @Override
  public E get(Ref<ID, E> reference) {

    if (reference == null) {
      return null;
    }
    return getEntityManager().getReference(getEntityClass(), reference.getId());
  }

  @Override
  public boolean exists(ID id) {

    // pointless...
    return findOne(id) != null;
  }

  /**
   * @return an {@link Iterable} to find ALL {@link #getEntityClass() managed entities} from the persistent store. Not
   *         exposed to API by default as this might not make sense for all kind of entities.
   */
  public List<E> findAll() {

    CriteriaQuery<E> query = getEntityManager().getCriteriaBuilder().createQuery(getEntityClass());
    Root<E> root = query.from(getEntityClass());
    query.select(root);
    TypedQuery<E> typedQuery = getEntityManager().createQuery(query);
    List<E> resultList = typedQuery.getResultList();
    LOG.debug("Query for all {} objects returned {} hit(s).", getEntityName(), resultList.size());
    return resultList;
  }

  @Override
  public List<E> findAll(Iterable<ID> ids) {

    CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
    CriteriaQuery<E> query = builder.createQuery(getEntityClass());
    Root<E> root = query.from(getEntityClass());
    query.select(root);
    query.where(root.get("id").in(toCollection(ids)));
    TypedQuery<E> typedQuery = getEntityManager().createQuery(query);
    List<E> resultList = typedQuery.getResultList();
    LOG.debug("Query for selection of {} objects returned {} hit(s).", getEntityName(), resultList.size());
    return resultList;
  }

  /**
   * @param ids sequence of id
   * @return a collection of these ids to use {@link Predicate#in(Collection)} for instance
   */
  protected Collection<ID> toCollection(Iterable<ID> ids) {

    if (ids instanceof Collection) {
      return (Collection<ID>) ids;
    }

    final Collection<ID> idsList = new ArrayList<>();
    for (final ID id : ids) {
      idsList.add(id);
    }
    return idsList;
  }

  @Override
  public void delete(ID id) {

    E entity = getEntityManager().getReference(getEntityClass(), id);
    getEntityManager().remove(entity);
    LOG.debug("Deleted {} with ID {}.", getEntityName(), id);
  }

  @Override
  public void delete(E entity) {

    // entity might be detached and could cause trouble in entityManager on remove
    if (getEntityManager().contains(entity)) {
      getEntityManager().remove(entity);
      LOG.debug("Deleted {} with ID {}.", getEntityName(), entity.getId());
    } else {
      delete(entity.getId());
    }

  }

  @Override
  public void delete(Iterable<? extends E> entities) {

    for (E entity : entities) {
      delete(entity);
    }
  }

}