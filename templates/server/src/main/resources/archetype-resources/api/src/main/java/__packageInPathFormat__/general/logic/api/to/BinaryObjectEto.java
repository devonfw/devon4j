package ${package}.general.logic.api.to;

import ${package}.general.common.api.BinaryObject;

import com.devonfw.module.basic.common.api.to.AbstractEto;

/**
 * The {@link com.devonfw.module.basic.common.api.to.AbstractEto ETO} for a {@link BinaryObject}.
 */
public class BinaryObjectEto extends AbstractEto implements BinaryObject {

  private static final long serialVersionUID = 1L;

  private String mimeType;

  private long size;

  /**
   * Constructor.
   */
  public BinaryObjectEto() {

    super();
  }

  @Override
  public void setMimeType(String mimeType) {

    this.mimeType = mimeType;

  }

  @Override
  public String getMimeType() {

    return this.mimeType;
  }

  @Override
  public long getSize() {

    return this.size;
  }

  @Override
  public void setSize(long size) {

    this.size = size;
  }

  @Override
  protected void toString(StringBuilder buffer) {

	buffer.append(getClass().getSimpleName() + ": " + "mimeType = " + mimeType + ", size = "  + size + ", hashcode = " + this.hashCode() + ";");
  }

}
