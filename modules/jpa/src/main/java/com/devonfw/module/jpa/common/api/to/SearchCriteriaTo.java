package com.devonfw.module.jpa.common.api.to;

import java.util.ArrayList;
import java.util.List;

import com.devonfw.module.basic.common.api.to.AbstractTo;

/**
 * This is the interface for a {@link net.sf.mmm.util.transferobject.api.TransferObject transfer-object } with the
 * criteria for a search and pagination query. Such object specifies the criteria selecting which hits will match when
 * performing a search.<br/>
 * <b>NOTE:</b><br/>
 * This interface only holds the necessary settings for the pagination part of a query. For your individual search, you
 * extend {@link SearchCriteriaTo} to create a java bean with all the fields for your search.
 *
 * @deprecated create your own TO and use org.springframework.data.domain.Pageable for pagination
 */
@Deprecated
public class SearchCriteriaTo extends AbstractTo {

  /** UID for serialization. */
  private static final long serialVersionUID = 1L;

  /** @see #getPagination() */
  private PaginationTo pagination;

  /** @see #getSearchTimeout() */
  private Integer searchTimeout;

  /** @see #getSort() */
  private List<OrderByTo> sort;

  /**
   * The constructor.
   */
  public SearchCriteriaTo() {

    super();
  }

  /**
   * The currently active pagination.
   *
   * @return pagination the currently active pagination or {@link PaginationTo#NO_PAGINATION} if no specific pagination
   *         has been set. Will never return {@code null}.
   */
  public PaginationTo getPagination() {

    return this.pagination == null ? PaginationTo.NO_PAGINATION : this.pagination;
  }

  /**
   * @param pagination the pagination to set
   */
  public void setPagination(PaginationTo pagination) {

    this.pagination = pagination;
  }

  /**
   * Limits the {@link PaginationTo#getSize() page size} by the given <code>limit</code>.
   * <p>
   * If currently no pagination is active, or the {@link PaginationTo#getSize() current page size} is {@code null} or
   * greater than the given {@code limit}, the value is replaced by {@code limit}
   *
   * @param limit is the maximum allowed value for the {@link PaginationTo#getSize() page size}.
   */
  public void limitMaximumPageSize(int limit) {

    if (getPagination() == PaginationTo.NO_PAGINATION) {
      setPagination(new PaginationTo());
    }

    Integer pageSize = getPagination().getSize();
    if ((pageSize == null) || (pageSize.intValue() > limit)) {
      getPagination().setSize((Integer.valueOf(limit)));
    }
  }

  /**
   * This method gets the maximum delay in milliseconds the search may last until it is canceled. <br>
   * <b>Note:</b><br>
   * This feature is the same as the query hint <code>"javax.persistence.query.timeout"</code> in JPA.
   *
   * @return the search timeout in milliseconds or {@code null} for NO timeout.
   */
  public Integer getSearchTimeout() {

    return this.searchTimeout;
  }

  /**
   * @param searchTimeout is the new value of {@link #getSearchTimeout()}.
   */
  public void setSearchTimeout(int searchTimeout) {

    this.searchTimeout = searchTimeout;
  }

  /**
   * @return sort Sort criterias list
   */
  public List<OrderByTo> getSort() {

    return this.sort;
  }

  /**
   * @param orderBy the {@link OrderByTo} to add to {@link #getSort() sort}. {@link List} will be created if
   *        {@code null}.
   */
  public void addSort(OrderByTo orderBy) {

    if (this.sort == null) {
      this.sort = new ArrayList<>();
    }
    this.sort.add(orderBy);
  }

  /**
   * @param sort Set the sort criterias list
   */
  public void setSort(List<OrderByTo> sort) {

    this.sort = sort;
  }

}
