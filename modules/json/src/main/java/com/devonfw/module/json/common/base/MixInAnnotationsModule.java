package com.devonfw.module.json.common.base;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * A {@link SimpleModule} to extend Jackson to mixin annotations for polymorphic types.
 *
 * @since 3.0.0
 */
public class MixInAnnotationsModule extends SimpleModule {

  private static final long serialVersionUID = 1L;

  private final Class<?>[] polymorphicClasses;

  /**
   * @param polymorphicClasses the classes reflecting JSON transfer-objects that are polymorphic.
   */
  public MixInAnnotationsModule(Class<?>... polymorphicClasses) {

    super("oasp.PolymorphyModule",
        new Version(1, 0, 0, null, ObjectMapperFactory.GROUP_ID, ObjectMapperFactory.ARTIFACT_ID));
    this.polymorphicClasses = polymorphicClasses;
  }

  @Override
  public void setupModule(SetupContext context) {

    for (Class<?> type : this.polymorphicClasses) {
      context.setMixInAnnotations(type, JacksonPolymorphicAnnotation.class);
    }
  }

  /**
   * The blueprint class for the following JSON-annotation allowing to convert from JSON to POJO and vice versa
   *
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "@type")
  public static class JacksonPolymorphicAnnotation {

  }

}
