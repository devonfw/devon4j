package com.devonfw.module.basic.common.api.to;

/**
 * Abstract base class for a {@link AbstractCto CTO} that holds a {@link #getMaster() master} {@link AbstractEto ETO}
 * together with other related {@link AbstractTo TOs} defined by its concrete sub-class.
 * 
 * @param <E> the generic type of the {@link #getMaster() master} {@link AbstractEto ETO}.
 * @since 3.0.0
 */
public abstract class MasterCto<E extends AbstractEto> extends AbstractCto {

  private static final long serialVersionUID = 1L;

  private E master;

  /**
   * @return master
   */
  public E getMaster() {

    return this.master;
  }

  /**
   * @param master new value of {@link #getMaster()}.
   */
  public void setMaster(E master) {

    this.master = master;
  }

  @Override
  protected void toString(StringBuilder buffer) {

    super.toString(buffer);
    if (this.master != null) {
      buffer.append('[');
      this.master.toString(buffer);
      buffer.append(']');
    }
  }

}
