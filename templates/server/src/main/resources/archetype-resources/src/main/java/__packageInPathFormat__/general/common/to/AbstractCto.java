package ${package}.general.common.to;

/**
 * This is the abstract base class for a <em>CTO</em> (composite {@link AbstractTo transfer-object}). Such object should
 * contain (aggregate) other {@link AbstractTo transfer-object}s or {@link java.util.Collection}s of those. However, a
 * CTO shall never have properties for atomic data (datatypes).<br>
 * Classes extending this class should carry the suffix <code>Cto</code>.
 */
public abstract class AbstractCto extends AbstractTo {

  /**
   * The constructor.
   */
  public AbstractCto() {

    super();
  }

}
