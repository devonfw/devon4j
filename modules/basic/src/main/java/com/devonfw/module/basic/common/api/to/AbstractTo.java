package com.devonfw.module.basic.common.api.to;

import net.sf.mmm.util.transferobject.api.TransferObject;

/**
 * Abstract class for a plain {@link net.sf.mmm.util.transferobject.api.TransferObject} that is neither a
 * {@link AbstractEto ETO} nor a {@link AbstractCto CTO}. Classes extending this class should carry the suffix
 * <code>Cto</code>. <br>
 *
 */
public abstract class AbstractTo implements TransferObject {

  /** UID for serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * The constructor.
   */
  public AbstractTo() {

    super();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String toString() {

    StringBuilder buffer = new StringBuilder();
    toString(buffer);
    return buffer.toString();
  }

  /**
   * Method to extend {@link #toString()} logic. Override to add additional information.
   *
   * @param buffer is the {@link StringBuilder} where to {@link StringBuilder#append(Object) append} the string
   *        representation.
   */
  protected void toString(StringBuilder buffer) {

    buffer.append(getClass().getSimpleName());
  }

}
