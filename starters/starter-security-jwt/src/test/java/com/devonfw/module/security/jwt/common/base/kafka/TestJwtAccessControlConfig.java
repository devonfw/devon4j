package com.devonfw.module.security.jwt.common.base.kafka;

import javax.inject.Named;

import com.devonfw.module.security.common.base.accesscontrol.AccessControlConfig;

/**
 * Example of {@link AccessControlConfig} that used for testing.
 */
@Named
public class TestJwtAccessControlConfig extends AccessControlConfig {

  public static final String APP_ID = "test";

  private static final String PREFIX = APP_ID + ".";

  public static final String PERMISSION_READ_CATEGORY = PREFIX + "ReadCategory";

  public static final String PERMISSION_READ_DISH = PREFIX + "ReadDish";

  public static final String GROUP_READ_MASTER_DATA = PREFIX + "ReadMasterData";

  public static final String GROUP_SAVE_USER = PREFIX + "SaveUser";

  /**
   * The constructor.
   */
  public TestJwtAccessControlConfig() {

    super();
    group(GROUP_READ_MASTER_DATA, PERMISSION_READ_CATEGORY, PERMISSION_READ_DISH);
    group(GROUP_SAVE_USER);
  }

}