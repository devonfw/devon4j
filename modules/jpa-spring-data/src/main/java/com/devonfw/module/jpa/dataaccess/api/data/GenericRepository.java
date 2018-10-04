package com.devonfw.module.jpa.dataaccess.api.data;

import static com.querydsl.core.alias.Alias.$;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.devonfw.module.jpa.dataaccess.api.QueryUtil;
import com.devonfw.module.jpa.dataaccess.api.feature.FeatureForceIncrementModificationCounter;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.jpa.impl.JPADeleteClause;

/**
 * {@link JpaRepository} with {@link QueryDslSupport} as well as typical Devon4j standard operations. It is recommended
 * to use {@link DefaultRepository} instead.
 *
 * @param <E> generic type of the managed {@link #getEntityClass() entity}. Typically implementing
 *        {@link com.devonfw.module.jpa.dataaccess.api.PersistenceEntity}.
 * @param <ID> generic type of the {@link com.devonfw.module.jpa.dataaccess.api.PersistenceEntity#getId() primary key}
 *        of the entity.
 *
 * @since 3.0.0
 */
public interface GenericRepository<E, ID extends Serializable>
    extends JpaRepository<E, ID>, QueryDslSupport<E>, FeatureForceIncrementModificationCounter<E> {

  /**
   * @return the {@link Class} of the managed entity.
   */
  Class<E> getEntityClass();

  /**
   * @param id the {@link com.devonfw.module.jpa.dataaccess.api.PersistenceEntity#getId() primary key}. May not be
   *        {@code null}.
   * @return the requested entity. Never {@code null}.
   * @see #findById(Object)
   */
  default E find(ID id) {

    return findById(id).orElseThrow(() -> new IllegalStateException(
        "Entity " + getEntityClass().getSimpleName() + " with ID '" + id + "' was not found!"));
  }

  /**
   * @param ids the {@link Collection} of {@link com.devonfw.module.jpa.dataaccess.api.PersistenceEntity#getId() IDs} to
   *        delete.
   * @return the number of entities that have actually been deleted.
   */
  @Modifying
  default long deleteByIds(Collection<ID> ids) {

    if ((ids == null) || (ids.isEmpty())) {
      return 0;
    }
    E alias = newDslAlias();
    EntityPathBase<E> entityPath = $(alias);
    JPADeleteClause delete = newDslDeleteClause(entityPath);
    @SuppressWarnings("rawtypes")
    Class idType = ids.iterator().next().getClass();
    // https://github.com/querydsl/querydsl/issues/2085
    @SuppressWarnings("unchecked")
    SimpleExpression<ID> idPath = Expressions.numberPath(idType, entityPath, "id");
    BooleanExpression inClause = QueryUtil.get().newInClause(idPath, ids);
    delete.where(inClause);
    return delete.execute();
  }

}
