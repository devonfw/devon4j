package com.devonfw.module.jpa.dataaccess.api.data;

import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * Interface for QueryDsl support. All (non-static) methods defined in this interface are considered as internal API of
 * a {@link GenericRepository} in order to be used by Java8+ default-method implementations for simplicity. Never call
 * such methods from outside the data-access layer.
 *
 * @param <E> generic type of the entity to query.
 *
 * @since 3.0.0
 */
public interface QueryDslSupport<E> {

  /**
   * <b>Attention:</b> Please read documentation of {@link QueryDslSupport} before usage.
   *
   * @return a new {@link JPAQuery}. In most cases you should prefer using {@link #newDslQuery(Object)} instead.
   */
  JPAQuery<E> newDslQuery();

  /**
   * <b>Attention:</b> Please read documentation of {@link QueryDslSupport} before usage.
   *
   * @param alias the {@link #newDslAlias() alias}.
   * @return a new {@link JPAQuery} for the given {@link com.querydsl.core.alias.Alias}.
   */
  JPAQuery<E> newDslQuery(E alias);

  /**
   * <b>Attention:</b> Please read documentation of {@link QueryDslSupport} before usage.
   *
   * @param alias the {@link #newDslAlias() alias}.
   * @return a new {@link JPADeleteClause} for the given {@link com.querydsl.core.alias.Alias}.
   */
  JPADeleteClause newDslDeleteClause(E alias);

  /**
   * <b>Attention:</b> Please read documentation of {@link QueryDslSupport} before usage.
   *
   * @param entityPath the {@link EntityPath} to delete from.
   * @return a new {@link JPADeleteClause} for the given {@link EntityPath}.
   */
  JPADeleteClause newDslDeleteClause(EntityPath<E> entityPath);

  /**
   * <b>Attention:</b> Please read documentation of {@link QueryDslSupport} before usage.
   *
   * @return a new QueryDSL {@link com.querydsl.core.alias.Alias} for the managed entity.
   */
  E newDslAlias();

}
