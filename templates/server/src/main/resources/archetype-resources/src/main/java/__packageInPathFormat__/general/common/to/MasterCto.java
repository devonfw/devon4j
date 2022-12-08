package ${package}.general.common.to;

/**
 * Abstract base class for a {@link AbstractCto CTO} that holds a {@link #getEto() master} {@link AbstractEto ETO}
 * together with other related {@link AbstractTo TOs} defined by its concrete sub-class.
 *
 * @param <E> the generic type of the {@link #getEto() master} {@link AbstractEto ETO}.
 */
public abstract class MasterCto<E extends AbstractEto> extends AbstractCto {

  private E eto;

  /**
   * @return the {@link AbstractEto ETO} of the main entity transferred with this {@link MasterCto}.
   */
  public E getEto() {

    return this.eto;
  }

  /**
   * @param master new value of {@link #getEto()}.
   */
  public void setEto(E master) {

    this.eto = master;
  }

  @Override
  protected void toString(StringBuilder buffer) {

    super.toString(buffer);
    if (this.eto != null) {
      buffer.append('[');
      this.eto.toString(buffer);
      buffer.append(']');
    }
  }

}
