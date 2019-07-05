package com.devonfw.module.json.common.base.type;

import java.io.IOException;

import com.devonfw.module.basic.common.api.reference.IdRef;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * {@link JsonSerializer} for {@link IdRef}.
 */
@SuppressWarnings("rawtypes")
public class IdRefJsonSerializer extends JsonSerializer<IdRef> {

  @Override
  public void serialize(IdRef value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

    long id = 0;
    if (value != null) {
      Long idLong = ((IdRef<?>) value).getId();
      if (idLong != null) {
        id = idLong.longValue();
      }
    }
    gen.writeNumber(id);
  }
}
