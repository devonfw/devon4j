package io.oasp.module.json.common.base.type;

import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.module.SimpleModule;

import io.oasp.module.json.common.base.ObjectMapperFactory;
import io.oasp.module.json.common.base.type.PageableJsonDeserializer;
import io.oasp.module.json.common.base.type.PageableJsonSerializer;

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
