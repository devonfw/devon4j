package com.devonfw.module.beanmapping.common.base;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.devonfw.module.beanmapping.common.api.BeanMapper;

/**
 * The abstract base implementation of {@link BeanMapper}.
 *
 */
public abstract class AbstractBeanMapper implements BeanMapper {

  /**
   * The constructor.
   */
  public AbstractBeanMapper() {

    super();
  }

  @Override
  public <API, S extends API, T extends API> T mapTypesafe(Class<API> apiClass, S source, Class<T> targetClass) {

    return map(source, targetClass);
  }

  @Override
  public <T> List<T> mapList(List<?> source, Class<T> targetClass) {

    return mapList(source, targetClass, false);
  }

  @Override
  public <T> List<T> mapList(List<?> source, Class<T> targetClass, boolean suppressNullValues) {

    if ((source == null) || (source.isEmpty())) {
      return new ArrayList<>();
    }
    List<T> result = new ArrayList<>(source.size());
    for (Object sourceObject : source) {
      if ((sourceObject != null) || !suppressNullValues) {
        result.add(map(sourceObject, targetClass));
      }
    }
    return result;
  }

  @Override
  public <T> Set<T> mapSet(Set<?> source, Class<T> targetClass) {

    return mapSet(source, targetClass, false);
  }

  @Override
  public <T> Set<T> mapSet(Set<?> source, Class<T> targetClass, boolean suppressNullValues) {

    if ((source == null) || (source.isEmpty())) {
      return new HashSet<>();
    }
    Set<T> result = new HashSet<>(source.size());
    for (Object sourceObject : source) {
      if ((sourceObject != null) || !suppressNullValues) {
        result.add(map(sourceObject, targetClass));
      }
    }
    return result;
  }
}
