package com.devonfw.module.basic.common.api.to;

import com.devonfw.module.basic.common.api.entity.RevisionedEntity;

/**
 * {@link AbstractEto ETO} for a {@link RevisionedEntity revisioned entity}.
 *
 * @since 3.0.0
 */
public class RevisionedEto extends AbstractEto implements RevisionedEntity<Long> {

  private static final long serialVersionUID = 1L;

  private Number revision;

  /**
   * The constructor.
   */
  public RevisionedEto() {

    super();
    this.revision = null;
  }

  @Override
  public Number getRevision() {

    return this.revision;
  }

  @Override
  public void setRevision(Number revision) {

    this.revision = revision;
  }

  @Override
  protected void toString(StringBuilder buffer) {

    super.toString(buffer);
    if (this.revision != null) {
      buffer.append("[rev=");
      buffer.append(this.revision);
      buffer.append("]");
    }
  }

}
