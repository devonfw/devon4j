package ${package}.general.common.impl.config;

import javax.inject.Named;

import org.springframework.data.domain.Pageable;
import org.springframework.security.web.csrf.CsrfToken;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.devonfw.module.json.common.base.ObjectMapperFactory;
import com.devonfw.module.json.common.base.type.PageableJsonSerializer;
import com.devonfw.module.json.common.base.type.PageableJsonDeserializer;

/**
 * The MappingFactory class to resolve polymorphic conflicts within the ${rootArtifactId} application.
 */
@Named("ApplicationObjectMapperFactory")
public class ApplicationObjectMapperFactory extends ObjectMapperFactory {

  /**
   * The constructor.
   */
  public ApplicationObjectMapperFactory() {

    super();
    // see https://github.com/devonfw-wiki/devon4j/wiki/guide-json#json-and-inheritance
    SimpleModule module = getExtensionModule();
    module.addAbstractTypeMapping(CsrfToken.class, CsrfTokenImpl.class);
	// register spring-data Pageable
    module.addSerializer(Pageable.class, new PageableJsonSerializer());
    module.addDeserializer(Pageable.class, new PageableJsonDeserializer());

    ObjectMapper objectMapper = super.createInstance();
    // omit properties in JSON that are null
    objectMapper.setSerializationInclusion(Include.NON_NULL);
    // Write legacy date/calendar as readable text instead of numeric value
    // See
    // https://fasterxml.github.io/jackson-databind/javadoc/2.6/com/fasterxml/jackson/databind/SerializationFeature.html#WRITE_DATES_AS_TIMESTAMPS
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    // ignore unknown properties in JSON to prevent errors
    // e.g. when the service has been updated/extended but the calling REST client is not yet updated
    // see https://github.com/devonfw-wiki/devon4j/wiki/guide-service-layer#versioning
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
  }
}
