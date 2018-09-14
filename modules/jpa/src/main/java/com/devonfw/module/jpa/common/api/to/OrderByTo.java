package com.devonfw.module.jpa.common.api.to;

import com.devonfw.module.basic.common.api.to.AbstractTo;

/**
 * Transfer object to transmit order criteria
 * 
 * @deprecated use org.springframework.data.domain.Sort instead
 */
@Deprecated
public class OrderByTo extends AbstractTo {

  private static final long serialVersionUID = 1L;

  private String name;

  private OrderDirection direction;

  /**
   * The constructor.
   */
  public OrderByTo() {

    super();
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() field name}.
   */
  public OrderByTo(String name) {

    this(name, OrderDirection.ASC);
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() field name}.
   * @param direction the {@link #getDirection() sort order direction}.
   */
  public OrderByTo(String name, OrderDirection direction) {

    super();
    this.name = name;
    this.direction = direction;
  }

  /**
   * @return the name of the field to order by.
   */
  public String getName() {

    return this.name;
  }

  /**
   * @param name the new value of {@link #getName()}.
   */
  public void setName(String name) {

    this.name = name;
  }

  /**
   * @return the {@link OrderDirection} defining the sort order direction.
   */
  public OrderDirection getDirection() {

    if (this.direction == null) {
      return OrderDirection.ASC;
    }
    return this.direction;
  }

  /**
   * @param direction the new value of {@link #getDirection()}.
   */
  public void setDirection(OrderDirection direction) {

    this.direction = direction;
  }

  @Override
  protected void toString(StringBuilder buffer) {

    buffer.append(this.name);
    buffer.append(' ');
    buffer.append(this.direction);
  }

}
