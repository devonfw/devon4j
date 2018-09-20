package com.devonfw.module.basic.common.api.config;

/**
 * This class provides {@code String} constants which allow to distinguish several bean definition profiles. The
 * constants should be used in {@code @Profile} annotations to avoid multiple points of failure (e.g., through typos
 * within annotations).<br/>
 * In test scenarios, these constants should be used in conjunction with the {@code @ActiveProfile} annotation.
 *
 * @since 2.2.0
 */
public class SpringProfileConstants {

  /**
   * This constant applies to all tests.
   */
  public static final String JUNIT = "junit";

  /**
   * This constant denotes a live profile.
   */
  public static final String NOT_JUNIT = "!" + JUNIT;

  /**
   * This constant should be used in conjunction with component tests.
   */
  public static final String COMPONENT_TEST = "component-test";

  /**
   * This constant should be used in conjunction with module tests.
   */
  public static final String MODULE_TEST = "module-test";

  /**
   * This constant should be used in conjunction with subsystem tests.
   */
  public static final String SUBSYSTEM_TEST = "subsystem-test";

  /**
   * This constant should be used in conjunction with system tests.
   */
  public static final String SYSTEM_TEST = "system-test";

}
