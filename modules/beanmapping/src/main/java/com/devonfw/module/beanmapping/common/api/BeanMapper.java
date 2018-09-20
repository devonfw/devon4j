package com.devonfw.module.beanmapping.common.api;

import java.util.List;
import java.util.Set;

/**
 * This is the interface used to convert from one Java bean to another compatible bean (e.g. from a JPA entity to a
 * corresponding transfer-object).
 *
 */
public interface BeanMapper {

  /**
   * Recursively converts the given <code>source</code> {@link Object} to the given target {@link Class}.
   *
   * @param <T> is the generic type to convert to.
   * @param source is the object to convert.
   * @param targetClass is the {@link Class} reflecting the type to convert to.
   * @return the converted object. Will be {@code null} if <code>source</code> is {@code null}.
   */
  <T> T map(Object source, Class<T> targetClass);

  /**
   * A type-safe variant of {@link #map(Object, Class)} to prevent accidental abuse (e.g. mapping of apples to bananas).
   *
   * @param <API> is a common super-type (interface) of <code>source</code> and <code>targetType</code>.
   * @param <S> is the generic type of <code>source</code>.
   * @param <T> is the generic type to convert to (target).
   * @param apiClass is the {@link Class} reflecting the {@literal <API>}.
   * @param source is the object to convert.
   * @param targetClass is the {@link Class} reflecting the type to convert to.
   * @return the converted object. Will be {@code null} if <code>source</code> is {@code null}.
   * @since 1.3.0
   */
  <API, S extends API, T extends API> T mapTypesafe(Class<API> apiClass, S source, Class<T> targetClass);

  /**
   * Creates a new {@link List} with the {@link #map(Object, Class) mapped bean} for each {@link List#get(int) entry} of
   * the given {@link List}. Uses {@code false} for <code>suppressNullValues</code> (see
   * {@link #mapList(List, Class, boolean)}).
   *
   * @param <T> is the generic type to convert the {@link List} entries to.
   * @param source is the {@link List} with the source objects.
   * @param targetClass is the {@link Class} reflecting the type to convert each {@link List} entry to.
   * @return the {@link List} with the converted objects. Will be {@link List#isEmpty() empty} is <code>source</code> is
   *         empty or {@code null}.
   */
  <T> List<T> mapList(List<?> source, Class<T> targetClass);

  /**
   * Creates a new {@link List} with the {@link #map(Object, Class) mapped bean} for each {@link List#get(int) entry} of
   * the given {@link List}.
   *
   * @param <T> is the generic type to convert the {@link List} entries to.
   * @param source is the {@link List} with the source objects.
   * @param targetClass is the {@link Class} reflecting the type to convert each {@link List} entry to.
   * @param suppressNullValues {@code true} if {@code null} values shall be suppressed/omitted in the
   *        resulting {@link List}, {@code false} otherwise.
   * @return the {@link List} with the converted objects. Will be {@link List#isEmpty() empty} is <code>source</code> is
   *         empty or {@code null}.
   * @since 1.3.0
   */
  <T> List<T> mapList(List<?> source, Class<T> targetClass, boolean suppressNullValues);

  /**
   * Creates a new {@link Set} with the {@link #map(Object, Class) mapped bean} for each {@link Set#contains(Object)
   * entry} of the given {@link Set}. Uses {@code false} for <code>suppressNullValues</code> (see
   * {@link #mapSet(Set, Class, boolean)}).
   *
   * @param <T> is the generic type to convert the {@link Set} entries to.
   * @param source is the {@link Set} with the source objects.
   * @param targetClass is the {@link Class} reflecting the type to convert each {@link Set} entry to.
   * @return the {@link Set} with the converted objects. Will be {@link Set#isEmpty() empty} is <code>source</code> is
   *         empty or {@code null}.
   */
  <T> Set<T> mapSet(Set<?> source, Class<T> targetClass);

  /**
   * Creates a new {@link Set} with the {@link #map(Object, Class) mapped bean} for each {@link Set#contains(Object)
   * entry} of the given {@link Set}.
   *
   * @param <T> is the generic type to convert the {@link Set} entries to.
   * @param source is the {@link Set} with the source objects.
   * @param targetClass is the {@link Class} reflecting the type to convert each {@link Set} entry to.
   * @param suppressNullValues {@code true} if {@code null} values shall be suppressed/omitted in the
   *        resulting {@link Set}, {@code false} otherwise.
   * @return the {@link Set} with the converted objects. Will be {@link Set#isEmpty() empty} is <code>source</code> is
   *         empty or {@code null}.
   * @since 1.3.0
   */
  <T> Set<T> mapSet(Set<?> source, Class<T> targetClass, boolean suppressNullValues);

}
