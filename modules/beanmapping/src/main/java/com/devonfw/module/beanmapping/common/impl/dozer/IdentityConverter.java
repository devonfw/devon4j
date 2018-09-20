package com.devonfw.module.beanmapping.common.impl.dozer;

import org.dozer.CustomConverter;

/**
 * Dozer {@link CustomConverter} that returns the original source object reference (identity conversion).
 *
 */
public class IdentityConverter implements CustomConverter {

  /**
   * The constructor.
   */
  public IdentityConverter() {

    super();
  }

  @Override
  public Object convert(Object destination, Object source, Class<?> destinationClass, Class<?> sourceClass) {

    return source;
  }

}
