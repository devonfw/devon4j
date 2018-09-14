package com.devonfw.module.jpa.dataaccess.api;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.devonfw.module.basic.common.api.query.LikePatternSyntax;
import com.devonfw.module.basic.common.api.query.StringSearchConfigTo;
import com.devonfw.module.basic.common.api.query.StringSearchOperator;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * Helper class for {@link #get() static access} to features of {@link QueryHelper}.
 *
 * @since 3.0.0
 */
public class QueryUtil extends QueryHelper {

  private static final QueryUtil INSTANCE = new QueryUtil();

  @Override
  public void whereLike(JPAQuery<?> query, StringExpression expression, String pattern, LikePatternSyntax syntax,
      boolean ignoreCase, boolean matchSubstring) {

    super.whereLike(query, expression, pattern, syntax, ignoreCase, matchSubstring);
  }

  @Override
  public BooleanExpression newLikeClause(StringExpression expression, String pattern, LikePatternSyntax syntax,
      boolean ignoreCase, boolean matchSubstring, boolean negate) {

    return super.newLikeClause(expression, pattern, syntax, ignoreCase, matchSubstring, negate);
  }

  @Override
  public BooleanExpression newStringClause(StringExpression expression, String value, StringSearchConfigTo config) {

    return super.newStringClause(expression, value, config);
  }

  @Override
  public BooleanExpression newStringClause(StringExpression expression, String value, StringSearchOperator operator,
      LikePatternSyntax syntax, boolean ignoreCase, boolean matchSubstring) {

    return super.newStringClause(expression, value, operator, syntax, ignoreCase, matchSubstring);
  }

  @Override
  public void whereString(JPAQuery<?> query, StringExpression expression, String value, StringSearchConfigTo config) {

    super.whereString(query, expression, value, config);
  }

  @Override
  public void whereNotLike(JPAQuery<?> query, StringExpression expression, String pattern, LikePatternSyntax syntax,
      boolean ignoreCase, boolean matchSubstring) {

    super.whereNotLike(query, expression, pattern, syntax, ignoreCase, matchSubstring);
  }

  @Override
  public BooleanExpression newLikeClause(StringExpression expression, String pattern, LikePatternSyntax syntax,
      boolean ignoreCase, boolean matchSubstring) {

    return super.newLikeClause(expression, pattern, syntax, ignoreCase, matchSubstring);
  }

  @Override
  public BooleanExpression newNotLikeClause(StringExpression expression, String pattern, LikePatternSyntax syntax,
      boolean ignoreCase, boolean matchSubstring) {

    return super.newNotLikeClause(expression, pattern, syntax, ignoreCase, matchSubstring);
  }

  @Override
  public <T> BooleanExpression newInClause(SimpleExpression<T> expression, Collection<T> inValues) {

    return super.newInClause(expression, inValues);
  }

  @Override
  public <T> void whereIn(JPAQuery<?> query, SimpleExpression<T> expression, Collection<T> inValues) {

    super.whereIn(query, expression, inValues);
  }

  /**
   * Returns a {@link Page} of entities according to the supplied {@link Pageable} and {@link JPAQuery}.
   *
   * @param <E> generic type of the entity.
   * @param pageable contains information about the requested page and sorting.
   * @param query is a query which is pre-configured with the desired conditions for the search.
   * @param determineTotal - {@code true} to determine the {@link Page#getTotalElements() total number of hits},
   *        {@code false} otherwise.
   * @return a paginated list.
   */
  public <E> Page<E> findPaginated(Pageable pageable, JPAQuery<E> query, boolean determineTotal) {

    return findPaginatedGeneric(pageable, query, determineTotal);
  }

  /**
   * @return the {@link QueryUtil} instance.
   */
  public static QueryUtil get() {

    return INSTANCE;
  }

}
