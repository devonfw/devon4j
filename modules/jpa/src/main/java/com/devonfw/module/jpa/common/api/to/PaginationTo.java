package com.devonfw.module.jpa.common.api.to;

import net.sf.mmm.util.exception.api.NlsIllegalArgumentException;

import com.devonfw.module.basic.common.api.to.AbstractTo;

/**
 * A {@link net.sf.mmm.util.transferobject.api.TransferObject transfer-object} containing criteria for paginating
 * queries.
 *
 * @deprecated use org.springframework.data.domain.Pageable instead.
 */
@Deprecated
public class PaginationTo extends AbstractTo {

  /**
   * Empty {@link PaginationTo} indicating no pagination.
   */
  public static final PaginationTo NO_PAGINATION = new PaginationTo();

  /** UID for serialization. */
  private static final long serialVersionUID = 1L;

  /** @see #getSize() */
  private Integer size;

  /** @see #getPage() */
  private int page = 1;

  /** @see #isTotal() */
  private boolean total;

  /**
   * @return size the size of a page.
   */
  public Integer getSize() {

    return this.size;
  }

  /**
   * @param size the size of a page.
   */
  public void setSize(Integer size) {

    this.size = size;
  }

  /**
   * @return page the current page.
   */
  public int getPage() {

    return this.page;
  }

  /**
   * @param page the current page. Must be greater than 0.
   */
  public void setPage(int page) {

    if (page <= 0) {
      throw new NlsIllegalArgumentException(page, "page");
    }
    this.page = page;
  }

  /**
   * @return total is {@code true} if the client requests that the server calculates the total number of entries found.
   */
  public boolean isTotal() {

    return this.total;
  }

  /**
   * @param total is {@code true} to request calculation of the total number of entries.
   */
  public void setTotal(boolean total) {

    this.total = total;
  }

  @Override
  protected void toString(StringBuilder buffer) {

    super.toString(buffer);
    buffer.append("@page=");
    buffer.append(this.page);
    if (this.size != null) {
      buffer.append(", size=");
      buffer.append(this.size);
    }
    if (this.total) {
      buffer.append(", total");
    }
  }

}
