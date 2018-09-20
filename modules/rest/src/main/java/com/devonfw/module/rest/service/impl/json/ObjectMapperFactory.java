package com.devonfw.module.rest.service.impl.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;

/**
 * A generic factory to {@link #createInstance() create} instances of a Jackson {@link ObjectMapper}. It allows to
 * configure the {@link ObjectMapper} for polymorphic transfer-objects.
 *
 * @see #setBaseClasses(Class...)
 * @see #setSubtypes(NamedType...)
 * @deprecated use {@link com.devonfw.module.json.common.base.ObjectMapperFactory} instead.
 */
@Deprecated
public class ObjectMapperFactory extends com.devonfw.module.json.common.base.ObjectMapperFactory {

  /**
   * The constructor.
   */
  public ObjectMapperFactory() {

    super();
  }

}
