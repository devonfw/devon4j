package com.devonfw.module.basic.common.api.config;

/**
 * The implementation of {@link MutableConfigProperties}.
 */
class MutableConfigPropertiesImpl extends SimpleConfigProperties implements MutableConfigProperties {

  private final SimpleConfigProperties parent;

  private final SimpleConfigProperties copy;

  private int copyModifications;

  private int parentModifications;

  /**
   * The constructor.
   *
   * @param key the hierarchical key of this {@link ConfigProperties}-node.
   */
  protected MutableConfigPropertiesImpl(String key) {
    this(key, null, null);
  }

  /**
   * The constructor.
   *
   * @param key the hierarchical key of this {@link ConfigProperties}-node.
   * @param copy the {@link ConfigProperties}-node to copy with {@link ConfigProperties#inherit(ConfigProperties)
   *        inheritance} from the given {@code parent}.
   * @param parent the parent {@link ConfigProperties} to {@link ConfigProperties#inherit(ConfigProperties) inherit
   *        from} or {@code null} for none.
   */
  protected MutableConfigPropertiesImpl(String key, ConfigProperties copy, ConfigProperties parent) {
    super(key);
    this.copy = asSimple(copy);
    this.parent = asSimple(parent);
    this.copyModifications = -1;
    this.parentModifications = -1;
  }

  @Override
  protected void updateChildren() {

    super.updateChildren();
    if (this.copy != null) {
      int copyMod = this.copy.getNodeModifications();
      if (this.copyModifications != copyMod) {
        for (String key : this.copy.getChildKeys()) {
          getChild(key);
        }
        this.copyModifications = copyMod;
      }
    }
    if (this.parent != null) {
      int parentMod = this.parent.getNodeModifications();
      if (this.parentModifications != parentMod) {
        for (String key : this.parent.getChildKeys()) {
          getChild(key);
        }
        this.parentModifications = parentMod;
      }
    }
  }

  @Override
  public MutableConfigProperties inherit(ConfigProperties parentNode) {

    if ((parentNode == null) || (parentNode.isEmpty())) {
      return this;
    }
    return super.inherit(parentNode);
  }

  @Override
  public String getValue() {

    String result = super.getValue();
    if (result == null) {
      if (this.copy != null) {
        result = this.copy.getValue();
      }
      if ((result == null) && (this.parent != null)) {
        result = this.parent.getValue();
      }
    }
    return result;
  }

  @Override
  public void setValue(String value) {

    super.setValue(value);
  }

  @Override
  public void setChildValue(String key, String value) {

    MutableConfigProperties child = (MutableConfigProperties) getChild(key, true);
    child.setValue(value);
  }

  @Override
  protected SimpleConfigProperties createChild(String key, boolean create) {

    ConfigProperties copyNode = EMPTY;
    if (this.copy != null) {
      copyNode = this.copy.getChild(key);
    }
    ConfigProperties parentNode = EMPTY;
    if (this.parent != null) {
      parentNode = this.parent.getChild(key);
    }
    if (!create && copyNode.isEmpty() && parentNode.isEmpty()) {
      return null;
    }
    return new MutableConfigPropertiesImpl(key, copyNode, parentNode);
  }

}
