package com.devonfw.module.json.common.base.type;

import java.io.IOException;

import com.devonfw.module.basic.common.api.reference.IdRef;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * {@link JsonDeserializer} for {@link IdRef}.
 */
@SuppressWarnings("rawtypes")
public class IdRefJsonDeserializer extends JsonDeserializer<IdRef> {

  @Override
  public IdRef deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

    long id = p.getLongValue();
    return IdRef.of(id);
  }

}
