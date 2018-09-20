package com.devonfw.module.jpa.common.api.to;

/**
 * {@link Enum} for sort order.
 * 
 * @deprecated user org.springframework.data.domain.Sort.Direction instead
 */
public enum OrderDirection {

  /** Sort in ascending order. */
  ASC,

  /** Sort in descending order. */
  DESC;

  /**
   * @return {@code true}, if {@link OrderDirection#ASC} is set. {@code false} otherwise.
   */
  public boolean isAsc() {

    return this == ASC;
  }

  /**
   * @return {@code true}, if {@link OrderDirection#DESC} is set. {@code false} otherwise.
   */
  public boolean isDesc() {

    return this == DESC;
  }

}
