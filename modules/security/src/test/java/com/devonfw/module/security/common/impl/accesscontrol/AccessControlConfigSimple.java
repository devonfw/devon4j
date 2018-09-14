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

  public static final String GROUP_COOK = PREFIX + "Cook";

  public static final String GROUP_BARKEEPER = PREFIX + "Barkeeper";

  public static final String GROUP_WAITER = PREFIX + "Waiter";

  public static final String GROUP_CHIEF = PREFIX + "Chief";

  /**
   * The constructor.
   */
  public AccessControlConfigSimple() {

    super();
    AccessControlGroup readMasterData = group(GROUP_READ_MASTER_DATA, PERMISSION_FIND_OFFER, PERMISSION_FIND_PRODUCT,
        PERMISSION_FIND_STAFF_MEMBER, PERMISSION_FIND_TABLE);
    AccessControlGroup cook = group(GROUP_COOK, readMasterData, PERMISSION_FIND_ORDER, PERMISSION_SAVE_ORDER,
        PERMISSION_FIND_ORDER_POSITION, PERMISSION_SAVE_ORDER_POSITION);
    AccessControlGroup barkeeper = group(GROUP_BARKEEPER, cook, PERMISSION_FIND_BILL, PERMISSION_SAVE_BILL,
        PERMISSION_DELETE_BILL, PERMISSION_DELETE_ORDER);
    AccessControlGroup waiter = group(GROUP_WAITER, barkeeper, PERMISSION_SAVE_TABLE);
    // AccessControlGroup chief =
    group(GROUP_CHIEF, waiter, PERMISSION_SAVE_OFFER, PERMISSION_SAVE_PRODUCT, PERMISSION_SAVE_STAFF_MEMBER,
        PERMISSION_DELETE_OFFER, PERMISSION_DELETE_PRODUCT, PERMISSION_DELETE_STAFF_MEMBER,
        PERMISSION_DELETE_ORDER_POSITION, PERMISSION_DELETE_TABLE);
  }

}