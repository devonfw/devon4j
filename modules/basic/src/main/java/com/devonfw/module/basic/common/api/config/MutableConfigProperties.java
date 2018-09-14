package com.devonfw.module.basic.common.api.config;

/**
 * Extends {@link ConfigProperties} with ability to modify.
 */
public interface MutableConfigProperties extends ConfigProperties {

  /**
   * @param key the key of the {@link #getChild(String) child} where to {@link #setValue(String) set the value}. All
   *        such children will be created if they do not yet exist.
   * @param value the new value to {@link #setValue(String) set}.
   */
  void setChildValue(String key, String value);

  /**
   * @param value the new value of {@link #getValue()}.
   */
  void setValue(String value);

}
