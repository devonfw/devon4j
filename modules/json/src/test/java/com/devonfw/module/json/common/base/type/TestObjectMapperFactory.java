package com.devonfw.module.json.common.base.type;

import org.springframework.data.domain.Pageable;

import com.devonfw.module.json.common.base.ObjectMapperFactory;
import com.devonfw.module.json.common.base.type.PageableJsonDeserializer;
import com.devonfw.module.json.common.base.type.PageableJsonSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * {@link ObjectMapperFactory} for testing.
 */
public class TestObjectMapperFactory extends ObjectMapperFactory {

  /**
   * The constructor.
   */
  public TestObjectMapperFactory() {

    super();
    SimpleModule module = getExtensionModule();
    module.addSerializer(Pageable.class, new PageableJsonSerializer());
    module.addDeserializer(Pageable.class, new PageableJsonDeserializer());
  }

}
