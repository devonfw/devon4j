package com.devonfw.module.security.common.impl.accesscontrol;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.security.RolesAllowed;

import org.junit.jupiter.api.Test;

import com.devonfw.module.security.common.api.accesscontrol.AccessControl;
import com.devonfw.module.security.common.api.accesscontrol.AccessControlGroup;
import com.devonfw.module.security.common.base.accesscontrol.AccessControlConfig;
import com.devonfw.module.test.common.base.ModuleTest;

/**
 * Test of {@link AccessControlConfig}.
 */
public class AccessControlConfigTest extends ModuleTest {

  /**
   * Test of {@link AccessControlConfig#getAccessControl(String)} with top-level chief group.
   */
  @Test
  // @RolesAllowed only used to ensure that the constant can be referenced here
  @RolesAllowed(AccessControlConfigSimple.PERMISSION_FIND_TABLE)
  public void testGetAccessControl() {

    // given
    AccessControlConfigSimple config = new AccessControlConfigSimple();
    String groupChief = AccessControlConfigSimple.GROUP_CHIEF;

    // when
    AccessControlGroup chief = (AccessControlGroup) config.getAccessControl(groupChief);

    // then
    assertThat(chief).isNotNull();
    assertThat(chief.getId()).isEqualTo(groupChief);
    assertThat(flatten(chief.getPermissions())).containsExactlyInAnyOrder(
        AccessControlConfigSimple.PERMISSION_SAVE_OFFER, AccessControlConfigSimple.PERMISSION_SAVE_PRODUCT,
        AccessControlConfigSimple.PERMISSION_SAVE_STAFF_MEMBER, AccessControlConfigSimple.PERMISSION_DELETE_OFFER,
        AccessControlConfigSimple.PERMISSION_DELETE_PRODUCT, AccessControlConfigSimple.PERMISSION_DELETE_STAFF_MEMBER,
        AccessControlConfigSimple.PERMISSION_DELETE_ORDER_POSITION, AccessControlConfigSimple.PERMISSION_DELETE_TABLE);
    AccessControlGroup waiter = getSingleInherit(chief);
    assertThat(waiter).isNotNull();
    assertThat(waiter.getId()).isEqualTo(AccessControlConfigSimple.GROUP_WAITER);
    assertThat(flatten(waiter.getPermissions()))
        .containsExactlyInAnyOrder(AccessControlConfigSimple.PERMISSION_SAVE_TABLE);
    AccessControlGroup barkeeper = getSingleInherit(waiter);
    assertThat(barkeeper).isNotNull();
    assertThat(barkeeper.getId()).isEqualTo(AccessControlConfigSimple.GROUP_BARKEEPER);
    assertThat(flatten(barkeeper.getPermissions())).containsExactlyInAnyOrder(
        AccessControlConfigSimple.PERMISSION_FIND_BILL, AccessControlConfigSimple.PERMISSION_SAVE_BILL,
        AccessControlConfigSimple.PERMISSION_DELETE_BILL, AccessControlConfigSimple.PERMISSION_DELETE_ORDER);
    AccessControlGroup cook = getSingleInherit(barkeeper);
    assertThat(cook).isNotNull();
    assertThat(cook.getId()).isEqualTo(AccessControlConfigSimple.GROUP_COOK);
    assertThat(flatten(cook.getPermissions())).containsExactlyInAnyOrder(
        AccessControlConfigSimple.PERMISSION_FIND_ORDER, AccessControlConfigSimple.PERMISSION_SAVE_ORDER,
        AccessControlConfigSimple.PERMISSION_FIND_ORDER_POSITION,
        AccessControlConfigSimple.PERMISSION_SAVE_ORDER_POSITION);
    AccessControlGroup readMasterData = getSingleInherit(cook);
    assertThat(readMasterData).isNotNull();
    assertThat(readMasterData.getId()).isEqualTo(AccessControlConfigSimple.GROUP_READ_MASTER_DATA);
    assertThat(flatten(readMasterData.getPermissions())).containsExactlyInAnyOrder(
        AccessControlConfigSimple.PERMISSION_FIND_OFFER, AccessControlConfigSimple.PERMISSION_FIND_PRODUCT,
        AccessControlConfigSimple.PERMISSION_FIND_STAFF_MEMBER, AccessControlConfigSimple.PERMISSION_FIND_TABLE);
    assertThat(readMasterData.getInherits()).isEmpty();
  }

  /**
   * Test of {@link AccessControlConfig#collectAccessControlIds(String, Set)} with
   * {@link AccessControlConfigSimple#GROUP_READ_MASTER_DATA}.
   */
  @Test
  public void testCollectAccessControlIds4ReadMasterData() {

    // given
    AccessControlConfigSimple config = new AccessControlConfigSimple();

    // when
    Set<String> permissions = new HashSet<>();
    config.collectAccessControlIds(AccessControlConfigSimple.GROUP_READ_MASTER_DATA, permissions);

    // then
    assertThat(permissions).containsExactlyInAnyOrder(AccessControlConfigSimple.GROUP_READ_MASTER_DATA,
        AccessControlConfigSimple.PERMISSION_FIND_OFFER, AccessControlConfigSimple.PERMISSION_FIND_PRODUCT,
        AccessControlConfigSimple.PERMISSION_FIND_STAFF_MEMBER, AccessControlConfigSimple.PERMISSION_FIND_TABLE);
  }

  /**
   * Test of {@link AccessControlConfig#collectAccessControlIds(String, Set)} with
   * {@link AccessControlConfigSimple#GROUP_CHIEF}.
   */
  @Test
  public void testCollectAccessControlIds4Chief() {

    // given
    AccessControlConfigSimple config = new AccessControlConfigSimple();
    String groupChief = AccessControlConfigSimple.GROUP_CHIEF;

    // when
    Set<String> permissions = new HashSet<>();
    config.collectAccessControlIds(groupChief, permissions);

    // then
    assertThat(permissions).containsExactlyInAnyOrder(AccessControlConfigSimple.GROUP_READ_MASTER_DATA,
        AccessControlConfigSimple.PERMISSION_FIND_OFFER, AccessControlConfigSimple.PERMISSION_FIND_PRODUCT,
        AccessControlConfigSimple.PERMISSION_FIND_STAFF_MEMBER, AccessControlConfigSimple.PERMISSION_FIND_TABLE,
        //
        AccessControlConfigSimple.GROUP_COOK, AccessControlConfigSimple.PERMISSION_FIND_ORDER,
        AccessControlConfigSimple.PERMISSION_SAVE_ORDER, AccessControlConfigSimple.PERMISSION_FIND_ORDER_POSITION,
        AccessControlConfigSimple.PERMISSION_SAVE_ORDER_POSITION,
        //
        AccessControlConfigSimple.GROUP_BARKEEPER, AccessControlConfigSimple.PERMISSION_FIND_BILL,
        AccessControlConfigSimple.PERMISSION_SAVE_BILL, AccessControlConfigSimple.PERMISSION_DELETE_BILL,
        AccessControlConfigSimple.PERMISSION_DELETE_ORDER,
        //
        AccessControlConfigSimple.GROUP_WAITER, AccessControlConfigSimple.PERMISSION_SAVE_TABLE,
        //
        groupChief, AccessControlConfigSimple.PERMISSION_SAVE_OFFER, AccessControlConfigSimple.PERMISSION_SAVE_PRODUCT,
        AccessControlConfigSimple.PERMISSION_SAVE_STAFF_MEMBER, AccessControlConfigSimple.PERMISSION_DELETE_OFFER,
        AccessControlConfigSimple.PERMISSION_DELETE_PRODUCT, AccessControlConfigSimple.PERMISSION_DELETE_STAFF_MEMBER,
        AccessControlConfigSimple.PERMISSION_DELETE_ORDER_POSITION, AccessControlConfigSimple.PERMISSION_DELETE_TABLE);
  }

  private static AccessControlGroup getSingleInherit(AccessControlGroup group) {

    List<AccessControlGroup> inherits = group.getInherits();
    assertThat(inherits).hasSize(1);
    AccessControlGroup inheritedGroup = inherits.get(0);
    assertThat(inheritedGroup).isNotNull();
    return inheritedGroup;
  }

  private static String[] flatten(Collection<? extends AccessControl> accessControlList) {

    String[] ids = new String[accessControlList.size()];
    int i = 0;
    for (AccessControl accessControl : accessControlList) {
      ids[i++] = accessControl.getId();
    }
    return ids;
  }

}
