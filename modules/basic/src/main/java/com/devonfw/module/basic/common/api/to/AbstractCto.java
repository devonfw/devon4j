package com.devonfw.module.basic.common.api.to;

import net.sf.mmm.util.transferobject.api.AbstractTransferObject;
import net.sf.mmm.util.transferobject.api.TransferObject;

/**
 * This is the abstract base class for a composite {@link AbstractTo transfer-object}. Such object should contain
 * (aggregate) other {@link AbstractTransferObject}s but no atomic data. This means it has properties that contain a
 * {@link TransferObject} or a {@link java.util.Collection} of those but no {@link net.sf.mmm.util.lang.api.Datatype
 * values}. <br>
 * Classes extending this class should carry the suffix <code>Cto</code>.
 *
 */
public abstract class AbstractCto extends AbstractTo {

  private static final long serialVersionUID = 1L;

  /**
   * The constructor.
   */
  public AbstractCto() {

    super();
  }

}
