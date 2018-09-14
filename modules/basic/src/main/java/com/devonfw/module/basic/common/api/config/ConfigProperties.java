package com.devonfw.module.basic.common.api.config;

import java.util.Map;
import java.util.Set;

/**
 * Simple abstraction interface for generic access to configuration properties (from spring-boot
 * {@code application.properties}).
 *
 * @since 3.0.0
 */
public interface ConfigProperties {

  /** The separator charactor '.' for hierarchical keys such as "spring.datasource.url". */
  char KEY_SEPARATOR = '.';

  /** An immutable instance of {@link ConfigProperties} that is always {@link #isEmpty() empty} */
  ConfigProperties EMPTY = new EmptyConfigProperties();

  /**
   * @return the {@link Set} of the {@link #getChild(String) direct child keys} available in the
   *         {@link ConfigProperties}-node.
   */
  Set<String> getChildKeys();

  /**
   * @param key the {@link #getChildKeys() child key} of the requested configuration value.
   * @return the child {@link ConfigProperties}. Will be an {@link #isEmpty() empty} child if undefined.
   */
  ConfigProperties getChild(String key);

  /**
   * Recursive variant of {@link #getChild(String)} such that
   * <code>{@link ConfigProperties config}.{@link #getChild(String...) getChild}(key1, ..., keyN)</code> is the same as
   * <code>{@link ConfigProperties config}.{@link #getChild(String) getChild}(key1)...{@link #getChild(String) getChild}(keyN)</code>.
   *
   * @param keys the keys to traverse recursively.
   * @return the descendant {@link #getChild(String) child} reached from recursively traversing the given {@code keys}.
   */
  ConfigProperties getChild(String... keys);

  /**
   * Shortcut for {@link #getChild(String) getChild(key)}.{@link #getValue()}.
   *
   * @param key the {@link #getChild(String) key of the child}
   * @return the value of this {@link ConfigProperties}-node. May be {@code null}.
   */
  String getChildValue(String key);

  /**
   * Shortcut for {@link #getChild(String...) getChild(keys)}.{@link #getValue()}.
   *
   * @param keys the keys to traverse recursively.
   * @return the {@link #getValue() value} of the {@link #getChild(String...) descendant child} traversed by
   *         {@code keys}. May be {@code null}.
   */
  String getChildValue(String... keys);

  /**
   * @return the value of this {@link ConfigProperties}-node. May be {@code null}.
   */
  String getValue();

  /**
   * @param <T> the requested {@code type}
   * @param type the {@link Class} reflecting the requested result type.
   * @return the value of this {@link ConfigProperties}-node converted to the given {@code type}. Will be {@code null}
   *         if undefined.
   */
  <T> T getValue(Class<T> type);

  /**
   * @param <T> the requested {@code type}
   * @param type the {@link Class} reflecting the requested result type.
   * @param defaultValue the value returned as default if the actual {@link #getValue() value} is undefined.
   * @return the value of this {@link ConfigProperties}-node converted to the given {@code type}. Will be
   *         {@code defaultValue} if undefined.
   */
  <T> T getValue(Class<T> type, T defaultValue);

  /**
   * @return the {@link #getValue(Class, Object)} as {@code boolean} with {@code false} as default.
   */
  boolean getValueAsBoolean();

  /**
   * @return {@code true} if this is an empty {@link ConfigProperties}-node that neither has a {@link #getValue() value}
   *         nor {@link #getChildKeys() any} {@link #getChild(String) child}.
   */
  boolean isEmpty();

  /**
   * @return this {@link ConfigProperties} converted to a {@link ConfigProperties#toFlatMap() flat} {@link Map}.
   */
  Map<String, String> toFlatMap();

  /**
   * @param rootKey the root key used as prefix for the {@link java.util.Map.Entry#getKey() keys} separated with a dot
   *        if not {@link String#isEmpty() empty}. Typically the empty {@link String}.
   * @return this {@link ConfigProperties} converted to a {@link ConfigProperties#toFlatMap() flat} {@link Map}.
   */
  Map<String, String> toFlatMap(String rootKey);

  /**
   * @return this {@link ConfigProperties} converted to a {@link ConfigProperties#toHierarchicalMap() hierarchical}
   *         {@link Map}.
   */
  Map<String, Object> toHierarchicalMap();

  /**
   * @param parent the parent {@link ConfigProperties} to extend.
   * @return a new instance of {@link ConfigProperties} with all {@link #getChild(String) children} and
   *         {@link #getValue() value}(s) from this {@link ConfigProperties}-tree and all {@link #getChild(String)
   *         children} and {@link #getValue() value}(s) inherited from the given {@code parent}
   *         {@link ConfigProperties}-tree if they are undefined in this tree.
   */
  MutableConfigProperties inherit(ConfigProperties parent);

}
