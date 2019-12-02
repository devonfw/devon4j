package com.devonfw.module.security.common.impl.accesscontrol;

import javax.inject.Named;

import com.devonfw.module.security.common.api.accesscontrol.AccessControlGroup;
import com.devonfw.module.security.common.base.accesscontrol.AccessControlConfig;

/**
 * Example of {@link AccessControlConfig} that used for testing.
 */
@Named
public class AccessControlConfigSimple extends AccessControlConfig {

  public static final String APP_ID = "MyApp";

  private static final String PREFIX = APP_ID + ".";

  public static final String PERMISSION_FIND_OFFER = PREFIX + "FindOffer";

  public static final String PERMISSION_SAVE_OFFER = PREFIX + "SaveOffer";

  public static final String PERMISSION_DELETE_OFFER = PREFIX + "DeleteOffer";

  public static final String PERMISSION_FIND_PRODUCT = PREFIX + "FindProduct";

  public static final String PERMISSION_SAVE_PRODUCT = PREFIX + "SaveProduct";

  public static final String PERMISSION_DELETE_PRODUCT = PREFIX + "DeleteProduct";

  public static final String PERMISSION_FIND_TABLE = PREFIX + "FindTable";

  public static final String PERMISSION_SAVE_TABLE = PREFIX + "SaveTable";

  public static final String PERMISSION_DELETE_TABLE = PREFIX + "DeleteTable";

  public static final String PERMISSION_FIND_STAFF_MEMBER = PREFIX + "FindStaffMember";

  public static final String PERMISSION_SAVE_STAFF_MEMBER = PREFIX + "SaveStaffMember";

  public static final String PERMISSION_DELETE_STAFF_MEMBER = PREFIX + "DeleteStaffMember";

  public static final String PERMISSION_FIND_ORDER = PREFIX + "FindOrder";

  public static final String PERMISSION_SAVE_ORDER = PREFIX + "SaveOrder";

  public static final String PERMISSION_DELETE_ORDER = PREFIX + "DeleteOrder";

  public static final String PERMISSION_FIND_ORDER_POSITION = PREFIX + "FindOrderPosition";

  public static final String PERMISSION_SAVE_ORDER_POSITION = PREFIX + "SaveOrderPosition";

  public static final String PERMISSION_DELETE_ORDER_POSITION = PREFIX + "DeleteOrderPosition";

  public static final String PERMISSION_FIND_BILL = PREFIX + "FindBill";

  public static final String PERMISSION_SAVE_BILL = PREFIX + "SaveBill";

  public static final String PERMISSION_DELETE_BILL = PREFIX + "DeleteBill";

  public static final String GROUP_READ_MASTER_DATA = PREFIX + "ReadMasterData";

  public static final String GROUP_USER3 = PREFIX + "User3";

  public static final String GROUP_USER2 = PREFIX + "User2";

  public static final String GROUP_USER1 = PREFIX + "User1";

  public static final String GROUP_ADMIN = PREFIX + "Admin";

  /**
   * The constructor.
   */
  public AccessControlConfigSimple() {

    super();
    AccessControlGroup readMasterData = group(GROUP_READ_MASTER_DATA, PERMISSION_FIND_OFFER, PERMISSION_FIND_PRODUCT,
        PERMISSION_FIND_STAFF_MEMBER, PERMISSION_FIND_TABLE);
    AccessControlGroup user3 = group(GROUP_USER3, readMasterData, PERMISSION_FIND_ORDER, PERMISSION_SAVE_ORDER,
        PERMISSION_FIND_ORDER_POSITION, PERMISSION_SAVE_ORDER_POSITION);
    AccessControlGroup user2 = group(GROUP_USER2, user3, PERMISSION_FIND_BILL, PERMISSION_SAVE_BILL,
        PERMISSION_DELETE_BILL, PERMISSION_DELETE_ORDER);
    AccessControlGroup user1 = group(GROUP_USER1, user2, PERMISSION_SAVE_TABLE);
    // AccessControlGroup chief =
    group(GROUP_ADMIN, user1, PERMISSION_SAVE_OFFER, PERMISSION_SAVE_PRODUCT, PERMISSION_SAVE_STAFF_MEMBER,
        PERMISSION_DELETE_OFFER, PERMISSION_DELETE_PRODUCT, PERMISSION_DELETE_STAFF_MEMBER,
        PERMISSION_DELETE_ORDER_POSITION, PERMISSION_DELETE_TABLE);
  }

}