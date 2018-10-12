package io.oasp.module.json.common.base.type;

import java.io.IOException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * {@link JsonSerializer} to serialize {@link Pageable} to JSON.
 *
 * @since 3.0.1
 */
public class PageableJsonSerializer extends JsonSerializer<Pageable> {

  @Override
  public void serialize(Pageable pageable, JsonGenerator gen, SerializerProvider serializers) throws IOException {

    if (pageable == null) {
      return;
    }
    gen.writeStartObject();
    gen.writeNumberField(PageableJsonDeserializer.PROPERTY_PAGE_NUMBER, pageable.getPageNumber());
    gen.writeNumberField(PageableJsonDeserializer.PROPERTY_PAGE_SIZE, pageable.getPageSize());
    Sort sort = pageable.getSort();
    if (sort != null) {
      gen.writeFieldName(PageableJsonDeserializer.PROPERTY_SORT);
      gen.writeStartArray();
      for (Order order : sort) {
        gen.writeStartObject();
        gen.writeStringField(PageableJsonDeserializer.PROPERTY_PROPERTY, order.getProperty());
        gen.writeStringField(PageableJsonDeserializer.PROPERTY_DIRECTION, order.getDirection().toString());
        gen.writeEndObject();
      }
      gen.writeEndArray();
    }
    gen.writeEndObject();
  }

}
