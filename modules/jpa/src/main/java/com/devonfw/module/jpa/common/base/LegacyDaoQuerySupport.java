package com.devonfw.module.jpa.common.base;

import java.util.List;

import javax.persistence.Query;

import net.sf.mmm.util.search.base.AbstractSearchCriteria;

import com.devonfw.module.jpa.common.api.to.PaginatedListTo;
import com.devonfw.module.jpa.common.api.to.PaginationResultTo;
import com.devonfw.module.jpa.common.api.to.PaginationTo;
import com.devonfw.module.jpa.common.api.to.SearchCriteriaTo;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * Upgrade Hint: To gain compatibility to old paging support all you need to do is to navigate to your
 * {@code ApplicationDaoImpl} and add {@code LegacyDaoQuerySupport<E>} to the {@code implements} declaration.
 *
 * @deprecated is only provided for compatibility with legacy features from {@code AbstractGenericDao} for
 *             {@link SearchCriteriaTo} and paging support. For future usage it is recommended to use the paging API
 *             from spring-data directly. This module {@code devon4j-jpa} only contains deprecated classes and will not
 *             be maintained anymore in the future.
 * @param <E> type of the entity.
 */
@Deprecated
public interface LegacyDaoQuerySupport<E> {

  /**
   * Returns a paginated list of entities according to the supplied {@link SearchCriteriaTo criteria}.
   *
   * @see #findPaginated(SearchCriteriaTo, JPAQuery, Expression)
   *
   * @param criteria contains information about the requested page.
   * @param query is a query which is preconfigured with the desired conditions for the search.
   * @return a paginated list.
   */
  default PaginatedListTo<E> findPaginated(SearchCriteriaTo criteria, JPAQuery<E> query) {

    return findPaginated(criteria, query, null);
  }

  /**
   * Returns a paginated list of entities according to the supplied {@link SearchCriteriaTo criteria}.
   * <p>
   * Applies {@code limit} and {@code offset} values to the supplied {@code query} according to the supplied
   * {@link PaginationTo pagination} information inside {@code criteria}.
   * <p>
   * If a {@link PaginationTo#isTotal() total count} of available entities is requested, will also execute a second
   * query, without pagination parameters applied, to obtain said count.
   * <p>
   * Will install a query timeout if {@link SearchCriteriaTo#getSearchTimeout()} is not null.
   *
   * @param criteria contains information about the requested page.
   * @param query is a query which is preconfigured with the desired conditions for the search.
   * @param expr is used for the final mapping from the SQL result to the entities.
   * @return a paginated list.
   */
  @SuppressWarnings("unchecked")
  default PaginatedListTo<E> findPaginated(SearchCriteriaTo criteria, JPAQuery<?> query, Expression<E> expr) {

    applyCriteria(criteria, query);
    PaginationTo pagination = criteria.getPagination();
    PaginationResultTo paginationResult = createPaginationResult(pagination, query);
    applyPagination(pagination, query);
    JPAQuery<E> finalQuery;
    if (expr == null) {
      finalQuery = (JPAQuery<E>) query;
    } else {
      finalQuery = query.select(expr);
    }
    List<E> paginatedList = finalQuery.fetch();
    return new PaginatedListTo<>(paginatedList, paginationResult);
  }

  /**
   * Creates a {@link PaginationResultTo pagination result} for the given {@code pagination} and {@code query}.
   * <p>
   * Needs to be called before pagination is applied to the {@code query}.
   *
   * @param pagination contains information about the requested page.
   * @param query is a query preconfigured with the desired conditions for the search.
   * @return information about the applied pagination.
   */
  default PaginationResultTo createPaginationResult(PaginationTo pagination, JPAQuery<?> query) {

    Long total = calculateTotalBeforePagination(pagination, query);
    return new PaginationResultTo(pagination, total);
  }

  /**
   * Calculates the total number of entities the given {@link JPAQuery query} would return without pagination applied.
   * <p>
   * Needs to be called before pagination is applied to the {@code query}.
   *
   * @param pagination is the pagination information as requested by the client.
   * @param query is the {@link JPAQuery query} for which to calculate the total.
   * @return the total count, or {@literal null} if {@link PaginationTo#isTotal()} is {@literal false}.
   */
  default Long calculateTotalBeforePagination(PaginationTo pagination, JPAQuery<?> query) {

    Long total = null;
    if (pagination.isTotal()) {
      total = query.clone().fetchCount();
    }

    return total;
  }

  /**
   * Applies the {@link PaginationTo pagination criteria} to the given {@link JPAQuery}.
   *
   * @param pagination is the {@link PaginationTo pagination criteria} to apply.
   * @param query is the {@link JPAQuery} to apply to.
   */
  default void applyPagination(PaginationTo pagination, JPAQuery<?> query) {

    if (pagination == PaginationTo.NO_PAGINATION) {
      return;
    }

    Integer limit = pagination.getSize();
    if (limit != null) {
      query.limit(limit);

      int page = pagination.getPage();
      if (page > 0) {
        query.offset((page - 1) * limit);
      }
    }
  }

  /**
   * Applies the meta-data of the given {@link AbstractSearchCriteria search criteria} to the given {@link JPAQuery}.
   *
   * @param criteria is the {@link AbstractSearchCriteria search criteria} to apply.
   * @param query is the {@link JPAQuery} to apply to.
   */
  default void applyCriteria(AbstractSearchCriteria criteria, JPAQuery<?> query) {

    Integer limit = criteria.getMaximumHitCount();
    if (limit != null) {
      query.limit(limit);
    }
    int offset = criteria.getHitOffset();
    if (offset > 0) {
      query.offset(offset);
    }
    Long timeout = criteria.getSearchTimeout();
    if (timeout != null) {
      query.setHint("javax.persistence.query.timeout", timeout.intValue());
    }
  }

  /**
   * Applies the meta-data of the given {@link AbstractSearchCriteria search criteria} to the given {@link Query}.
   *
   * @param criteria is the {@link AbstractSearchCriteria search criteria} to apply.
   * @param query is the {@link Query} to apply to.
   */
  default void applyCriteria(AbstractSearchCriteria criteria, Query query) {

    Integer limit = criteria.getMaximumHitCount();
    if (limit != null) {
      query.setMaxResults(limit);
    }
    int offset = criteria.getHitOffset();
    if (offset > 0) {
      query.setFirstResult(offset);
    }
    Long timeout = criteria.getSearchTimeout();
    if (timeout != null) {
      query.setHint("javax.persistence.query.timeout", timeout.intValue());
    }
  }

  /**
   * Applies the meta-data of the given {@link SearchCriteriaTo search criteria} to the given {@link Query}.
   *
   * @param criteria is the {@link AbstractSearchCriteria search criteria} to apply.
   * @param query is the {@link JPAQuery} to apply to.
   */
  default void applyCriteria(SearchCriteriaTo criteria, JPAQuery<?> query) {

    Integer timeout = criteria.getSearchTimeout();
    if (timeout != null) {
      query.setHint("javax.persistence.query.timeout", timeout.intValue());
    }
  }

}
