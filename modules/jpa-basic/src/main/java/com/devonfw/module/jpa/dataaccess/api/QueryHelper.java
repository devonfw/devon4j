package com.devonfw.module.jpa.dataaccess.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.StringUtils;

import com.devonfw.module.basic.common.api.query.LikePatternSyntax;
import com.devonfw.module.basic.common.api.query.StringSearchConfigTo;
import com.devonfw.module.basic.common.api.query.StringSearchOperator;
import com.querydsl.core.JoinExpression;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparablePath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * Class with utility methods for dealing with queries. Either extend this class or use {@link QueryUtil#get()}.
 *
 * @since 3.0.0
 */
public class QueryHelper {

  private static final Logger LOG = LoggerFactory.getLogger(QueryHelper.class);

  /** JPA query property to configure the timeout in milliseconds. */
  protected static final String QUERY_PROPERTY_TIMEOUT = "javax.persistence.query.timeout";

  /**
   * @param query the {@link JPAQuery} to modify.
   * @param timeout the timeout in milliseconds.
   */
  protected void applyTimeout(JPAQuery<?> query, Number timeout) {

    if (timeout != null) {
      query.setHint(QUERY_PROPERTY_TIMEOUT, timeout.intValue());
    }
  }

  /**
   * @param query the {@link JPAQuery} to modify.
   * @param expression the {@link StringExpression} to {@link StringExpression#like(String) create the LIKE-clause}
   *        from.
   * @param pattern the pattern for the LIKE-clause to create.
   * @param syntax the {@link LikePatternSyntax} of the given {@code pattern}.
   * @param ignoreCase - {@code true} to ignore the case, {@code false} otherwise (to search case-sensitive).
   * @param matchSubstring - {@code true} to match also if the given {@code pattern} shall also match substrings on the
   *        given {@link StringExpression}.
   */
  protected void whereLike(JPAQuery<?> query, StringExpression expression, String pattern, LikePatternSyntax syntax,
      boolean ignoreCase, boolean matchSubstring) {

    BooleanExpression like = newLikeClause(expression, pattern, syntax, ignoreCase, matchSubstring, false);
    if (like != null) {
      query.where(like);
    }
  }

  /**
   * @param query the {@link JPAQuery} to modify.
   * @param expression the {@link StringExpression} to {@link StringExpression#notLike(String) create the NOT
   *        LIKE-clause} from.
   * @param pattern the pattern for the NOT LIKE-clause to create.
   * @param syntax the {@link LikePatternSyntax} of the given {@code pattern}.
   * @param ignoreCase - {@code true} to ignore the case, {@code false} otherwise (to search case-sensitive).
   * @param matchSubstring - {@code true} to match also if the given {@code pattern} shall also match substrings on the
   *        given {@link StringExpression}.
   */
  protected void whereNotLike(JPAQuery<?> query, StringExpression expression, String pattern, LikePatternSyntax syntax,
      boolean ignoreCase, boolean matchSubstring) {

    BooleanExpression like = newLikeClause(expression, pattern, syntax, ignoreCase, matchSubstring, true);
    if (like != null) {
      query.where(like);
    }
  }

  /**
   * @param expression the {@link StringExpression} to {@link StringExpression#like(String) create the LIKE-clause}
   *        from.
   * @param pattern the pattern for the LIKE-clause to create.
   * @param syntax the {@link LikePatternSyntax} of the given {@code pattern}.
   * @param ignoreCase - {@code true} to ignore the case, {@code false} otherwise (to search case-sensitive).
   * @param matchSubstring - {@code true} to match also if the given {@code pattern} shall also match substrings on the
   *        given {@link StringExpression}.
   * @return the LIKE-clause as {@link BooleanExpression}.
   */
  protected BooleanExpression newLikeClause(StringExpression expression, String pattern, LikePatternSyntax syntax,
      boolean ignoreCase, boolean matchSubstring) {

    return newLikeClause(expression, pattern, syntax, ignoreCase, matchSubstring, false);
  }

  /**
   * @param expression the {@link StringExpression} to {@link StringExpression#notLike(String) create the NOT
   *        LIKE-clause} from.
   * @param pattern the pattern for the NOT LIKE-clause to create.
   * @param syntax the {@link LikePatternSyntax} of the given {@code pattern}.
   * @param ignoreCase - {@code true} to ignore the case, {@code false} otherwise (to search case-sensitive).
   * @param matchSubstring - {@code true} to match also if the given {@code pattern} shall also match substrings on the
   *        given {@link StringExpression}.
   * @return the NOT LIKE-clause as {@link BooleanExpression}.
   */
  protected BooleanExpression newNotLikeClause(StringExpression expression, String pattern, LikePatternSyntax syntax,
      boolean ignoreCase, boolean matchSubstring) {

    return newLikeClause(expression, pattern, syntax, ignoreCase, matchSubstring, true);
  }

  /**
   * @param expression the {@link StringExpression} to {@link StringExpression#like(String) create the LIKE-clause}
   *        from.
   * @param pattern the pattern for the LIKE-clause to create.
   * @param syntax the {@link LikePatternSyntax} of the given {@code pattern}.
   * @param ignoreCase - {@code true} to ignore the case, {@code false} otherwise (to search case-sensitive).
   * @param matchSubstring - {@code true} to match also if the given {@code pattern} shall also match substrings on the
   *        given {@link StringExpression}.
   * @param negate - {@code true} for {@link StringExpression#notLike(String) NOT LIKE}, {@code false} for
   *        {@link StringExpression#like(String) LIKE}.
   * @return the LIKE-clause as {@link BooleanExpression}.
   */
  protected BooleanExpression newLikeClause(StringExpression expression, String pattern, LikePatternSyntax syntax,
      boolean ignoreCase, boolean matchSubstring, boolean negate) {

    if (syntax == null) {
      syntax = LikePatternSyntax.autoDetect(pattern);
      if (syntax == null) {
        syntax = LikePatternSyntax.SQL;
      }
    }
    String likePattern = LikePatternSyntax.SQL.convert(pattern, syntax, matchSubstring);
    StringExpression exp = expression;
    if (ignoreCase) {
      likePattern = likePattern.toUpperCase(Locale.US);
      exp = exp.upper();
    }
    BooleanExpression clause;
    if (syntax != LikePatternSyntax.SQL) {
      clause = exp.like(likePattern, LikePatternSyntax.ESCAPE);
    } else {
      clause = exp.like(likePattern);
    }
    if (negate) {
      clause = clause.not();
    }
    return clause;
  }

  /**
   * @param expression the {@link StringExpression} to search on.
   * @param value the string value or pattern to search for.
   * @param config the {@link StringSearchConfigTo} to configure the search. May be {@code null} for regular equals
   *        search as default fallback.
   * @return the new {@link BooleanExpression} for the specified string comparison clause.
   */
  protected BooleanExpression newStringClause(StringExpression expression, String value, StringSearchConfigTo config) {

    StringSearchOperator operator = StringSearchOperator.EQ;
    LikePatternSyntax syntax = null;
    boolean ignoreCase = false;
    boolean matchSubstring = false;
    if (config != null) {
      operator = config.getOperator();
      syntax = config.getLikeSyntax();
      ignoreCase = config.isIgnoreCase();
      matchSubstring = config.isMatchSubstring();
    }
    return newStringClause(expression, value, operator, syntax, ignoreCase, matchSubstring);
  }

  /**
   * @param expression the {@link StringExpression} to search on.
   * @param value the string value or pattern to search for.
   * @param syntax the {@link LikePatternSyntax} of the given {@code pattern}.
   * @param operator the {@link StringSearchOperator} used to compare the search string {@code value}.
   * @param ignoreCase - {@code true} to ignore the case, {@code false} otherwise (to search case-sensitive).
   * @param matchSubstring - {@code true} to match also if the given {@code pattern} shall also match substrings on the
   *        given {@link StringExpression}.
   * @return the new {@link BooleanExpression} for the specified string comparison clause.
   */
  protected BooleanExpression newStringClause(StringExpression expression, String value, StringSearchOperator operator,
      LikePatternSyntax syntax, boolean ignoreCase, boolean matchSubstring) {

    if (operator == null) {
      if (syntax == null) {
        syntax = LikePatternSyntax.autoDetect(value);
        if (syntax == null) {
          operator = StringSearchOperator.EQ;
        } else {
          operator = StringSearchOperator.LIKE;
        }
      } else {
        operator = StringSearchOperator.LIKE;
      }
    }
    if (matchSubstring && ((operator == StringSearchOperator.EQ) || (operator == StringSearchOperator.NE))) {
      if (syntax == null) {
        syntax = LikePatternSyntax.SQL;
      }
      if (operator == StringSearchOperator.EQ) {
        operator = StringSearchOperator.LIKE;
      } else {
        operator = StringSearchOperator.NOT_LIKE;
      }
    }
    String v = value;
    if (v == null) {
      switch (operator) {
        case LIKE:
        case EQ:
          return expression.isNull();
        case NE:
          return expression.isNotNull();
        default:
          throw new IllegalArgumentException("Operator " + operator + " does not accept null!");
      }
    } else if (v.isEmpty()) {
      switch (operator) {
        case LIKE:
        case EQ:
          return expression.isEmpty();
        case NOT_LIKE:
        case NE:
          return expression.isNotEmpty();
        default:
          // continue;
      }
    }
    StringExpression exp = expression;
    if (ignoreCase) {
      v = v.toUpperCase(Locale.US);
      exp = exp.upper();
    }
    switch (operator) {
      case LIKE:
        return newLikeClause(exp, v, syntax, false, matchSubstring, false);
      case NOT_LIKE:
        return newLikeClause(exp, v, syntax, false, matchSubstring, true);
      case EQ:
        return exp.eq(v);
      case NE:
        return exp.ne(v);
      case LT:
        return exp.lt(v);
      case LE:
        return exp.loe(v);
      case GT:
        return exp.gt(v);
      case GE:
        return exp.goe(v);
      default:
        throw new IllegalStateException("" + operator);
    }
  }

  /**
   * @param query the {@link JPAQuery} to modify.
   * @param expression the {@link StringExpression} to search on.
   * @param value the string value or pattern to search for.
   * @param config the {@link StringSearchConfigTo} to configure the search. May be {@code null} for regular equals
   *        search.
   */
  protected void whereString(JPAQuery<?> query, StringExpression expression, String value,
      StringSearchConfigTo config) {

    BooleanExpression clause = newStringClause(expression, value, config);
    if (clause != null) {
      query.where(clause);
    }
  }

  /**
   * @param <T> generic type of the {@link Collection} values for the {@link SimpleExpression#in(Collection)
   *        IN-clause}(s).
   * @param expression the {@link SimpleExpression} used to create the {@link SimpleExpression#in(Collection)
   *        IN-clause}(s).
   * @param inValues the {@link Collection} of values for the {@link SimpleExpression#in(Collection) IN-clause}(s).
   * @return the {@link BooleanExpression} for the {@link SimpleExpression#in(Collection) IN-clause}(s) oder
   *         {@code null} if {@code inValues} is {@code null} or {@link Collection#isEmpty() empty}.
   */
  protected <T> BooleanExpression newInClause(SimpleExpression<T> expression, Collection<T> inValues) {

    if ((inValues == null) || (inValues.isEmpty())) {
      return null;
    }
    int size = inValues.size();
    if (size == 1) {
      return expression.eq(inValues.iterator().next());
    }
    int maxSizeOfInClause = DatabaseConfigProperties.getInstance().getMaxSizeOfInClause();
    if (size <= maxSizeOfInClause) {
      return expression.in(inValues);
    }
    LOG.warn("Creating workaround for IN-clause with {} items - this can cause performance problems.", size);
    List<T> values;
    if (inValues instanceof List) {
      values = (List<T>) inValues;
    } else {
      values = new ArrayList<>(inValues);
    }
    BooleanExpression predicate = null;
    int start = 0;
    while (start < size) {
      int end = start + maxSizeOfInClause;
      if (end > size) {
        end = size;
      }
      List<T> partition = values.subList(start, end);
      if (predicate == null) {
        predicate = expression.in(partition);
      } else {
        predicate = predicate.or(expression.in(partition));
      }
      start = end;
    }
    return predicate;
  }

  /**
   * @param <T> generic type of the {@link Collection} values for the {@link SimpleExpression#in(Collection)
   *        IN-clause}(s).
   * @param query the {@link JPAQuery} where to {@link JPAQuery#where(com.querydsl.core.types.Predicate) add} the
   *        {@link #newInClause(SimpleExpression, Collection) IN-clause(s)} from the other given parameters.
   * @param expression the {@link SimpleExpression} used to create the {@link SimpleExpression#in(Collection)
   *        IN-clause}(s).
   * @param inValues the {@link Collection} of values for the {@link SimpleExpression#in(Collection) IN-clause}(s).
   * @see #newInClause(SimpleExpression, Collection)
   */
  protected <T> void whereIn(JPAQuery<?> query, SimpleExpression<T> expression, Collection<T> inValues) {

    BooleanExpression inClause = newInClause(expression, inValues);
    if (inClause != null) {
      query.where(inClause);
    }
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
  protected <E> Page<E> findPaginatedGeneric(Pageable pageable, JPAQuery<E> query, boolean determineTotal) {

    long total = -1;
    if (determineTotal) {
      total = query.clone().fetchCount();
    }
    long offset = 0;
    if (pageable != null) {
      offset = pageable.getOffset();
      query.offset(offset);
      query.limit(pageable.getPageSize());
      applySort(query, pageable.getSort());
    }
    List<E> hits = query.fetch();
    if (total == -1) {
      total = offset + hits.size();
    }
    return new PageImpl<>(hits, pageable, total);
  }

  /**
   * @param query the {@link JPAQuery} to apply the {@link Sort} to.
   * @param sort the {@link Sort} to apply as ORDER BY to the given {@link JPAQuery}.
   */
  @SuppressWarnings("rawtypes")
  protected void applySort(JPAQuery<?> query, Sort sort) {

    if (sort == null) {
      return;
    }
    PathBuilder<?> alias = findAlias(query);
    for (Order order : sort) {
      String property = order.getProperty();
      Direction direction = order.getDirection();
      ComparablePath<Comparable> path = alias.getComparable(property, Comparable.class);
      OrderSpecifier<Comparable> orderSpecifier;
      if (direction == Direction.ASC) {
        orderSpecifier = path.asc();
      } else {
        orderSpecifier = path.desc();
      }
      query.orderBy(orderSpecifier);
    }
  }

  private <E> PathBuilder<E> findAlias(JPAQuery<E> query) {

    String alias = null;
    List<JoinExpression> joins = query.getMetadata().getJoins();
    if ((joins != null) && !joins.isEmpty()) {
      JoinExpression join = joins.get(0);
      Expression<?> target = join.getTarget();
      if (target instanceof EntityPath) {
        alias = target.toString(); // no safe API
      }
    }
    Class<E> type = query.getType();
    if (alias == null) {
      // should actually never happen, but fallback is provided as buest guess
      alias = StringUtils.uncapitalize(type.getSimpleName());
    }
    return new PathBuilder<>(type, alias);
  }

}
