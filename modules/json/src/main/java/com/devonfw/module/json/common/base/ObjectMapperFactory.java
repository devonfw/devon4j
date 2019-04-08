package com.devonfw.module.json.common.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * A generic factory to {@link #createInstance() create} instances of a Jackson {@link ObjectMapper}. It allows to
 * configure the {@link ObjectMapper} for polymorphic transfer-objects.
 *
 * @see #setBaseClasses(Class...)
 * @see #setSubtypes(NamedType...)
 *
 * @since 3.0.0
 */
public class ObjectMapperFactory {

  static final String GROUP_ID = "com.devonfw.java";

  static final String ARTIFACT_ID = "devon4j-rest";

  private List<Class<?>> baseClassList;

  private List<NamedType> subtypeList;

  private SimpleModule extensionModule;

  /**
   * The constructor.
   */
  public ObjectMapperFactory() {

    super();
  }

  /**
   * Gets access to a generic extension {@link SimpleModule module} for customizations to Jackson JSON mapping.
   *
   * @see SimpleModule#addSerializer(Class, com.fasterxml.jackson.databind.JsonSerializer)
   * @see SimpleModule#addDeserializer(Class, com.fasterxml.jackson.databind.JsonDeserializer)
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
   * @param baseClasses are the base classes that are polymorphic (e.g. abstract transfer-object classes that have
   *        sub-types). You also need to register all sub-types of these polymorphic classes via
   *        {@link #setSubtypes(NamedType...)}.
   */
  public void setBaseClasses(Class<?>... baseClasses) {

    this.baseClassList = Arrays.asList(baseClasses);
  }

  /**
   * @see #setBaseClasses(Class...)
   *
   * @param baseClasses the base-classes to add to {@link #setBaseClasses(Class...) base classes list}.
   */
  public void addBaseClasses(Class<?>... baseClasses) {

    if (this.baseClassList == null) {
      this.baseClassList = new ArrayList<>();
    }
    this.baseClassList.addAll(Arrays.asList(baseClasses));
  }

  /**
   * @see #setSubtypes(NamedType...)
   *
   * @param subtypeList the {@link List} of {@link NamedType}s to register the subtypes.
   */
  public void setSubtypeList(List<NamedType> subtypeList) {

    this.subtypeList = subtypeList;
  }

  /**
   * @see #setSubtypes(NamedType...)
   *
   * @param subtypes the {@link NamedType}s to add to {@link #setSubtypeList(List) sub-type list} for registration.
   */
  public void addSubtypes(NamedType... subtypes) {

    if (this.subtypeList == null) {
      this.subtypeList = new ArrayList<>();
    }
    this.subtypeList.addAll(Arrays.asList(subtypes));
  }

  /**
   * @param subtypeList the {@link NamedType}s as pair of {@link Class} reflecting a polymorphic sub-type together with
   *        its unique name in JSON format.
   */
  public void setSubtypes(NamedType... subtypeList) {

    setSubtypeList(Arrays.asList(subtypeList));
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

    return mapper;
  }
}
