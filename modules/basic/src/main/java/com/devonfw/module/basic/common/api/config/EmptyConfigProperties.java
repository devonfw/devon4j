package com.devonfw.module.basic.common.api.config;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of {@link ConfigProperties} that always is {@link ConfigProperties#isEmpty() empty}.
 *
 * @since 3.0.0
 */
class EmptyConfigProperties implements ConfigProperties {

  @Override
  public Set<String> getChildKeys() {

    return Collections.emptySet();
  }

  @Override
  public ConfigProperties getChild(String key) {

    return this;
  }

  @Override
  public ConfigProperties getChild(String... keys) {

    return this;
  }

  @Override
  public String getChildValue(String key) {

    return null;
  }

  @Override
  public String getChildValue(String... keys) {

    return null;
  }

  @Override
  public String getValue() {

    return null;
  }

  @Override
  public <T> T getValue(Class<T> type) {

    return null;
  }

  @Override
  public <T> T getValue(Class<T> type, T defaultValue) {

    return defaultValue;
  }

  @Override
  public boolean getValueAsBoolean() {

    return false;
  }

  @Override
  public boolean isEmpty() {

    return true;
  }

  @Override
  public Map<String, String> toFlatMap() {

    return toFlatMap("");
  }

  @Override
  public Map<String, String> toFlatMap(String rootKey) {

    return Collections.emptyMap();
  }

  @Override
  public Map<String, Object> toHierarchicalMap() {

    return Collections.emptyMap();
  }

  @Override
  public MutableConfigProperties inherit(ConfigProperties parent) {

    if (parent == null || parent.isEmpty()) {
      return new MutableConfigPropertiesImpl("");
    }
    if (parent instanceof MutableConfigProperties) {
      return (MutableConfigProperties) parent;
    } else
      return parent.inherit(EMPTY);
  }

  @Override
  public String toString() {

    return "<empty>";
  }

}
