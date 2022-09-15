package ${package}.general.common;

/**
 * This class provides {@code String} constants which allow to distinguish several bean definition profiles. The
 * constants should be used in {@code @Profile} annotations to avoid multiple points of failure (e.g., through typos
 * within annotations).<br/>
 * In test scenarios, these constants should be used in conjunction with the {@code @ActiveProfile} annotation.
 *
 * @since 3.0.0
 */
public class SpringProfileConstants {

  /**
   * Profile active in JUnit tests.
   */
  public static final String JUNIT = "junit";

  /**
   * Profile active if not in JUnit tests (on real application startup).
   */
  public static final String NOT_JUNIT = "!" + JUNIT;

  /**
   * Profile active in local development environment.
   */
  public static final String DEV = "dev";

  /**
   * Profile active if not in local development environment ({@link #JUNIT} or real stages).
   */
  public static final String NOT_DEV = "!" + DEV;

}
