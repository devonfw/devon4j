package com.devonfw.module.jpa.common.api.to;

import java.util.List;

import net.sf.mmm.util.entity.api.PersistenceEntity;
import net.sf.mmm.util.transferobject.api.TransferObject;

import com.devonfw.module.basic.common.api.to.AbstractTo;

/**
 * A paginated list of objects with additional pagination information.
 *
 * @param <E> is the generic type of the objects. Will usually be a {@link PersistenceEntity persistent entity} when
 *        used in the data layer, or a {@link TransferObject transfer object}.
 *
 * @deprecated use org.springframework.data.domain.Page instead.
 */
@Deprecated
public class PaginatedListTo<E> extends AbstractTo {

  /** UID for serialization. */
  private static final long serialVersionUID = 1L;

  /** @see #getPagination() */
  private PaginationResultTo pagination;

  /** @see #getResult() */
  private List<E> result;

  /**
   * The constructor.
   */
  public PaginatedListTo() {

    super();
  }

  /**
   * A convenience constructor which accepts a paginated list and {@link PaginationResultTo pagination information}.
   *
   * @param result is the list of objects.
   * @param pagination is the {@link PaginationResultTo pagination information}.
   */
  public PaginatedListTo(List<E> result, PaginationResultTo pagination) {

    this.result = result;
    this.pagination = pagination;
  }

  /**
   * @return the list of objects.
   */
  public List<E> getResult() {

    return this.result;
  }

  /**
   * @return pagination is the {@link PaginationResultTo pagination information}.
   */
  public PaginationResultTo getPagination() {

    return this.pagination;
  }

  @Override
  protected void toString(StringBuilder buffer) {

    super.toString(buffer);
    buffer.append('@');
    if (this.result != null) {
      buffer.append("#result=");
      buffer.append(this.result.size());
    }
    if (this.pagination != null) {
      buffer.append(',');
      this.pagination.toString(buffer);
    }
  }
}
