package ${package}.general.common.impl.config;

import javax.inject.Named;

import org.springframework.data.domain.Pageable;
import org.springframework.security.web.csrf.CsrfToken;

import com.fasterxml.jackson.databind.module.SimpleModule;

import io.oasp.module.json.common.base.ObjectMapperFactory;
import io.oasp.module.json.common.base.type.PageableJsonSerializer;
import io.oasp.module.json.common.base.type.PageableJsonDeserializer;

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
    // see https://github.com/oasp-forge/oasp4j-wiki/wiki/guide-json#json-and-inheritance
    SimpleModule module = getExtensionModule();
    module.addAbstractTypeMapping(CsrfToken.class, CsrfTokenImpl.class);
	// register spring-data Pageable
    module.addSerializer(Pageable.class, new PageableJsonSerializer());
    module.addDeserializer(Pageable.class, new PageableJsonDeserializer());
  }
}
