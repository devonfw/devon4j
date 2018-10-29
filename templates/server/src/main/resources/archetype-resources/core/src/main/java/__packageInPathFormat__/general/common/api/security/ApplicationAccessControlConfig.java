package ${package}.general.common.api.security;

import javax.inject.Named;

import com.devonfw.module.security.common.api.accesscontrol.AccessControlGroup;
import com.devonfw.module.security.common.base.accesscontrol.AccessControlConfig;

/**
 * Example of {@link AccessControlConfig} that used for testing.
 */
@Named
public class ApplicationAccessControlConfig extends AccessControlConfig {

  public static final String APP_ID = "${rootArtifactId}";

  private static final String PREFIX = APP_ID + ".";

  public static final String PERMISSION_FIND_BINARY_OBJECT = PREFIX + "FindBinaryObject";

  public static final String PERMISSION_SAVE_BINARY_OBJECT = PREFIX + "SaveBinaryObject";

  public static final String PERMISSION_DELETE_BINARY_OBJECT = PREFIX + "DeleteBinaryObject";

  public static final String GROUP_READ_MASTER_DATA = PREFIX + "ReadMasterData";

  public static final String GROUP_ADMIN = PREFIX + "Admin";

  /**
   * The constructor.
   */
  public ApplicationAccessControlConfig() {

    super();
    AccessControlGroup readMasterData = group(GROUP_READ_MASTER_DATA, PERMISSION_FIND_BINARY_OBJECT);
    group(GROUP_ADMIN, readMasterData, PERMISSION_SAVE_BINARY_OBJECT, PERMISSION_DELETE_BINARY_OBJECT);
  }

}