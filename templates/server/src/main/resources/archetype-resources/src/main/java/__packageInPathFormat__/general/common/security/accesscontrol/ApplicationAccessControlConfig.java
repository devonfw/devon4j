package ${package}.general.common.security.accesscontrol;

import javax.inject.Named;

/**
 * Example of {@link AccessControlConfig} that used for testing.
 */
@Named
public class ApplicationAccessControlConfig extends AccessControlConfig {

  /** ID of this application. */
  public static final String APP_ID = "basic";

  /**
   * Prefix for this application used as namespace to avoid clashing of {@link AccessControl#getId() identifiers} in
   * identity- and access-management (IAM) of your app landscape.
   */
  private static final String PREFIX = APP_ID + ".";

  /** A {@link AccessControlPermission} to read the master. */
  public static final String PERMISSION_READ_MASTER_DATA = PREFIX + "ReadMasterData";

  /** A {@link AccessControlGroup} for administrators. */
  public static final String GROUP_ADMIN = PREFIX + "Admin";

  /**
   * The constructor.
   */
  public ApplicationAccessControlConfig() {

    super();
    group(GROUP_ADMIN, PERMISSION_READ_MASTER_DATA);
  }

}