package ${package}.general.common.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.inject.Named;

/**
 * The MappingFactory class to resolve polymorphic conflicts within the basic application.
 */
@Named
public class ApplicationObjectMapperFactory {

  static final String GROUP_ID = "com.devonfw.java";

  static final String ARTIFACT_ID = "devon4j-rest";

  private final List<Class<?>> baseClassList;

  private final List<NamedType> subtypeList;

  private SimpleModule extensionModule;

  /**
   * The constructor.
   */
  public ApplicationObjectMapperFactory() {

    super();
    this.baseClassList = new ArrayList<>();
    this.subtypeList = new ArrayList<>();
    initMapping();
  }

  /**
   * Gets access to a generic extension {@link SimpleModule module} for customizations to Jackson JSON mapping.
   *
   * @return extensionModule
   */
  public SimpleModule getExtensionModule() {

    if (this.extensionModule == null) {
      this.extensionModule = new SimpleModule("devonfw.ExtensionModule",
          new Version(1, 0, 0, null, GROUP_ID, ARTIFACT_ID));
    }
    return this.extensionModule;
  }

  /**
   * @param baseClasses the base-classes to add.
   */
  public void addBaseClasses(Class<?>... baseClasses) {

    for (Class<?> baseClass : baseClasses) {
      addBaseClass(baseClass);
    }
  }

  /**
   * @param baseClass the base-class to add.
   */
  public void addBaseClass(Class<?> baseClass) {

    this.baseClassList.add(baseClass);
  }

  /**
   * @param subtypes the {@link NamedType}s to add.
   */
  public void addSubtypes(NamedType... subtypes) {

    for (NamedType subtype : subtypes) {
      addSubtype(subtype);
    }
  }

  /**
   * @param subtype the {@link NamedType} to add.
   */
  public void addSubtype(NamedType subtype) {

    this.subtypeList.add(subtype);
  }

  /**
   * @return an instance of {@link ObjectMapper} configured for polymorphic resolution.
   */
  public ObjectMapper createInstance() {

    ObjectMapper mapper = new ObjectMapper();

    if ((this.baseClassList != null) && (!this.baseClassList.isEmpty())) {
      Class<?>[] baseClasses = this.baseClassList.toArray(new Class<?>[this.baseClassList.size()]);
      SimpleModule polymorphyModule = new MixInAnnotationsModule(baseClasses);
      mapper.registerModule(polymorphyModule);
    }

    if (this.extensionModule != null) {
      mapper.registerModule(this.extensionModule);
    }

    if (this.subtypeList != null) {
      SubtypeResolver subtypeResolver = mapper.getSubtypeResolver();
      for (NamedType subtype : this.subtypeList) {
        subtypeResolver.registerSubtypes(subtype);
      }
      mapper.setSubtypeResolver(subtypeResolver);
    }
    // register JavaTimeModule
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
    // omit properties in JSON that are null
    mapper.setSerializationInclusion(Include.NON_NULL);
    // Write legacy date/calendar as readable text instead of numeric value
    // See
    // https://fasterxml.github.io/jackson-databind/javadoc/2.6/com/fasterxml/jackson/databind/SerializationFeature.html#WRITE_DATES_AS_TIMESTAMPS
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    // ignore unknown properties in JSON to prevent errors
    // e.g. when the service has been updated/extended but the calling REST client is not yet updated
    // see https://github.com/devonfw/devon4j/blob/develop/documentation/guide-service-layer.asciidoc#versioning
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    return mapper;
  }

  /**
   * @return an instance of {@link SimpleModule} for registering configurations
   */
  protected SimpleModule initMapping() {

    SimpleModule module = getExtensionModule();
    return module;
  }

}
