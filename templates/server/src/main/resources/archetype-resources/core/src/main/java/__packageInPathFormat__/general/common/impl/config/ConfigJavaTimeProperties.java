package ${package}.general.common.impl.config;

/**
 * This is ENUM class for configuration of required properties for Java Time module
 */
public enum ConfigJavaTimeProperties {
  /**
   * Enum for omit properies in JSON that are null
   */
  Include_NON_NULL,
  /**
   * Enum for ignoring unknown properties in JSON to prevent errors
   */
  DeserializationFeature_FAIL_ON_UNKNOWN_PROPERTIES
}
