package com.devonfw.module.basic.common.api.config;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Simple implementation of {@link ConfigProperties}.
 *
 * @since 3.0.0
 */
public class SimpleConfigProperties implements ConfigProperties {

  private final String nodeKey;

  private final Map<String, SimpleConfigProperties> childMap;

  private final Set<String> childKeys;

  private String value;

  private int nodeModifications;

  /**
   * The constructor.
   *
   * @param key the hierarchical key of this {@link ConfigProperties}-node.
   */
  protected SimpleConfigProperties(String key) {
    super();
    this.nodeKey = key;
    this.childMap = new HashMap<>();
    this.childKeys = Collections.unmodifiableSet(this.childMap.keySet());
    this.nodeModifications = 0;
  }

  @Override
  public boolean isEmpty() {

    return false;
  }

  /**
   * @return the absolute key of this {@link ConfigProperties}-node.
   */
  protected String getNodeKey() {

    return this.nodeKey;
  }

  /**
   * @return the modification counter that gets incremented whenever a child node is added.
   */
  protected int getNodeModifications() {

    return this.nodeModifications;
  }

  @Override
  public Set<String> getChildKeys() {

    updateChildren();
    return this.childKeys;
  }

  /**
   * Updates the child-nodes in case of a mutable copy node.
   */
  protected void updateChildren() {

    // nothing by default
  }

  @Override
  public ConfigProperties getChild(String key) {

    return getChild(key, false);
  }

  /**
   * @see #getChild(String)
   *
   * @param key the key of the requested child.
   * @param create - {@code true} to create if not exits, {@code false} otherwise.
   * @return the requested child.
   */
  protected ConfigProperties getChild(String key, boolean create) {

    if ((key == null) || (key.isEmpty())) {
      return this;
    }
    if (key.indexOf(KEY_SEPARATOR) > 0) {
      String[] segments = key.split("\\.");
      return getChild(create, segments);
    }
    SimpleConfigProperties result = this.childMap.get(key);
    if (result == null) {
      result = createChild(key, create);
      if (result == null) {
        return EMPTY;
      } else {
        this.childMap.put(key, result);
        this.nodeModifications++;
      }
    }
    return result;
  }

  /**
   * @param childKey the key segment of the child to create.
   * @param create - {@code true} to force creation, {@code false} otherwise.
   * @return the new child or {@code null}.
   */
  protected SimpleConfigProperties createChild(String childKey, boolean create) {

    if (!create) {
      return null;
    }
    return new SimpleConfigProperties(composeKey(this.nodeKey, childKey));
  }

  @Override
  public ConfigProperties getChild(String... keys) {

    return getChild(false, keys);
  }

  /**
   * @param create - {@code true} to create if not exits, {@code false} otherwise.
   * @param keys the key segments of the requested child.
   * @return the requested child.
   */
  protected ConfigProperties getChild(boolean create, String... keys) {

    if ((keys == null) || (keys.length == 0)) {
      return this;
    }
    SimpleConfigProperties result = this;
    for (String key : keys) {
      ConfigProperties child = result.getChild(key, create);
      if (child.isEmpty()) {
        return child;
      }
      result = (SimpleConfigProperties) child;
    }
    return result;
  }

  @Override
  public String getValue() {

    return this.value;
  }

  @Override
  public <T> T getValue(Class<T> type) {

    return getValue(type, null);
  }

  @Override
  public <T> T getValue(Class<T> type, T defaultValue) {

    String result = getValue();
    if (result == null) {
      return defaultValue;
    } else {
      return ConfigValueUtil.convertValue(result, type);
    }
  }

  @Override
  public boolean getValueAsBoolean() {

    return "true".equalsIgnoreCase(getValue());
  }

  /**
   * @param value new value of {@link #getValue()}.
   */
  protected void setValue(String value) {

    this.value = value;
  }

  @Override
  public String getChildValue(String key) {

    return getChild(key).getValue();
  }

  @Override
  public String getChildValue(String... keys) {

    return getChild(keys).getValue();
  }

  @Override
  public Map<String, String> toFlatMap() {

    return toFlatMap("");
  }

  @Override
  public Map<String, String> toFlatMap(String rootKey) {

    Map<String, String> map = new HashMap<>();
    toFlatMap(rootKey, map);
    return map;
  }

  private void toFlatMap(String key, Map<String, String> map) {

    updateChildren();
    String nodeValue = getValue();
    if (nodeValue != null) {
      map.put(key, nodeValue);
    }
    for (Entry<String, SimpleConfigProperties> entry : this.childMap.entrySet()) {
      String childKey = entry.getKey();
      String subKey = composeKey(key, childKey);
      entry.getValue().toFlatMap(subKey, map);
    }
  }

  @Override
  public Map<String, Object> toHierarchicalMap() {

    Map<String, Object> map = new HashMap<>();
    toHierarchicalMap(map);
    return map;
  }

  private void toHierarchicalMap(Map<String, Object> map) {

    String nodeValue = getValue();
    if (nodeValue != null) {
      map.put("", nodeValue);
    }
    updateChildren();
    for (Entry<String, SimpleConfigProperties> entry : this.childMap.entrySet()) {
      String childKey = entry.getKey();
      Map<String, Object> subMap = new HashMap<>();
      entry.getValue().toHierarchicalMap(subMap);
      map.put(childKey, subMap);
    }
  }

  /**
   * @see SimpleConfigProperties#ofFlatMap(String, Map)
   *
   * @param map the flat {@link Map} of the configuration values.
   */
  protected void fromFlatMap(Map<String, String> map) {

    for (Entry<String, String> entry : map.entrySet()) {
      SimpleConfigProperties child;
      child = (SimpleConfigProperties) getChild(entry.getKey(), true);
      child.value = entry.getValue();
    }
  }

  /**
   * @see SimpleConfigProperties#ofHierarchicalMap(String, Map)
   *
   * @param map the hierarchical {@link Map} of the configuration values.
   */
  @SuppressWarnings("unchecked")
  protected void fromHierarchicalMap(Map<String, Object> map) {

    for (Entry<String, Object> entry : map.entrySet()) {
      SimpleConfigProperties child = (SimpleConfigProperties) getChild(entry.getKey(), true);
      Object childObject = entry.getValue();
      if (childObject instanceof Map) {
        child.fromHierarchicalMap((Map<String, Object>) childObject);
      } else {
        child.value = childObject.toString();
      }
    }
  }

  @Override
  public String toString() {

    StringBuilder buffer = new StringBuilder(this.nodeKey);
    buffer.append('=');
    String nodeValue = getValue();
    if (nodeValue != null) {
      buffer.append(nodeValue);
    }
    return buffer.toString();
  }

  @Override
  public MutableConfigProperties inherit(ConfigProperties parentNode) {

    return new MutableConfigPropertiesImpl(getNodeKey(), this, parentNode);
  }

  /**
   * @see #ofFlatMap(String, Map)
   *
   * @param map the flat {@link Map} of the configuration values.
   * @return the root {@link ConfigProperties}-node of the given flat {@link Map} converted to hierarchical
   *         {@link ConfigProperties}.
   */
  public static ConfigProperties ofFlatMap(Map<String, String> map) {

    return ofFlatMap("", map);
  }

  /**
   * Converts a flat {@link Map} of configuration values to hierarchical {@link ConfigProperties}. E.g. the flat map
   * <code>{"foo.bar.some"="some-value", "foo.bar.other"="other-value"}</code> would result in {@link ConfigProperties}
   * {@code myRoot} such that
   * <code>myRoot.{@link #getChild(String...) getChild}("foo", "bar").{@link #getChildKeys()}</code> returns the
   * {@link Collection} {"some", "other"} and
   * <code>myRoot.{@link #getChildValue(String) getValue}("foo.bar.some")</code> returns "my-value".
   *
   * @param key the top-level key of the returned root {@link ConfigProperties}-node. Typically the empty string ("")
   *        for root.
   * @param map the flat {@link Map} of the configuration values.
   * @return the root {@link ConfigProperties}-node of the given flat {@link Map} converted to hierarchical
   *         {@link ConfigProperties}.
   */
  public static ConfigProperties ofFlatMap(String key, Map<String, String> map) {

    SimpleConfigProperties root = new SimpleConfigProperties(key);
    root.fromFlatMap(map);
    return root;
  }

  /**
   * @see #ofHierarchicalMap(String, Map)
   *
   * @param map the hierarchical {@link Map} of the configuration values.
   * @return the root {@link ConfigProperties}-node of the given hierarchical {@link Map} converted to
   *         {@link ConfigProperties}.
   */
  public static ConfigProperties ofHierarchicalMap(Map<String, Object> map) {

    return ofHierarchicalMap("", map);
  }

  /**
   * Converts a hierarchical {@link Map} of configuration values to {@link ConfigProperties}. E.g. the hierarchical map
   * <code>{"foo"={"bar"={"some"="my-value", "other"="magic-value"}}}</code> would result in {@link ConfigProperties}
   * {@code myRoot} such that
   * <code>myRoot.{@link #getChild(String...) getChild}("foo", "bar").{@link #getChildKeys()}</code> returns the
   * {@link Collection} {"some", "other"} and
   * <code>myRoot.{@link #getChildValue(String) getValue}("foo.bar.some")</code> returns "my-value".
   *
   * @param key the top-level key of the returned root {@link ConfigProperties}-node. Typically the empty string ("")
   *        for root.
   * @param map the hierarchical {@link Map} of the configuration values.
   * @return the root {@link ConfigProperties}-node of the given hierarchical {@link Map} converted to
   *         {@link ConfigProperties}.
   */
  public static ConfigProperties ofHierarchicalMap(String key, Map<String, Object> map) {

    SimpleConfigProperties root = new SimpleConfigProperties(key);
    root.fromHierarchicalMap(map);
    return root;
  }

  /**
   * @param parentKey the parent key.
   * @param childKey the child key.
   * @return the composed key.
   */
  protected static String composeKey(String parentKey, String childKey) {

    if (parentKey.isEmpty()) {
      return childKey;
    } else {
      return parentKey + KEY_SEPARATOR + childKey;
    }
  }

  /**
   * @param configProperties the {@link ConfigProperties}.
   * @return the given {@link ConfigProperties} as {@link SimpleConfigProperties} or {@code null} if no such instance.
   */
  protected static SimpleConfigProperties asSimple(ConfigProperties configProperties) {

    if (configProperties instanceof SimpleConfigProperties) {
      return (SimpleConfigProperties) configProperties;
    }
    return null;
  }

}
