package ${package}.general.common.impl.config;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Named;

import org.springframework.data.domain.Pageable;
import org.springframework.security.web.csrf.CsrfToken;

import com.fasterxml.jackson.databind.module.SimpleModule;
import ${package}.general.common.impl.config.ConfigJavaTimeProperties;
import com.devonfw.module.json.common.base.ObjectMapperFactory;
import com.devonfw.module.json.common.base.type.PageableJsonSerializer;
import com.devonfw.module.json.common.base.type.PageableJsonDeserializer;

/**
 * The MappingFactory class to resolve polymorphic conflicts within the ${rootArtifactId} application.
 */
@Named("ApplicationObjectMapperFactory")
public class ApplicationObjectMapperFactory extends ObjectMapperFactory {

  private List<String> configProps = Stream.of(ConfigJavaTimeProperties.values()).map(Enum::name)
      .collect(Collectors.toList());
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
    // configure properties for Java Time module
    setConfigPropertiesJavaTime(this.configProps);
  }
}
