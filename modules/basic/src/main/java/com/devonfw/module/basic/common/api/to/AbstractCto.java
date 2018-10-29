package com.devonfw.module.basic.common.api.to;

/**
 * This is the abstract base class for a <em>CTO</em> (composite {@link AbstractTo transfer-object}). Such object should
 * contain (aggregate) other {@link AbstractTo transfer-object}s or {@link java.util.Collection}s of those. However, a
 * CTO shall never have properties for atomic data (datatypes).<br>
 * Classes extending this class should carry the suffix <code>Cto</code>.
 *
 * @since 3.0.0
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
