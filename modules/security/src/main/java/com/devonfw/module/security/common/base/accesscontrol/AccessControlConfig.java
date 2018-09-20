package com.devonfw.module.security.common.base.accesscontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.devonfw.module.security.common.api.accesscontrol.AccessControl;
import com.devonfw.module.security.common.api.accesscontrol.AccessControlGroup;
import com.devonfw.module.security.common.api.accesscontrol.AccessControlPermission;

/**
 * {@link AbstractAccessControlProvider} for static configuration of
 * {@link com.devonfw.module.security.common.api.accesscontrol.AccessControlSchema}. Instead of maintaining it as XML file
 * you can directly configure it as code and therefore define and reference constants in annotations such as
 * {@link javax.annotation.security.RolesAllowed}.
 *
 * @since 3.0.0
 */
public abstract class AccessControlConfig extends AbstractAccessControlProvider {

  /**
   * Creates a new {@link AccessControlPermission} for static configuration of access controls.
   *
   * @param id {@link AccessControlPermission#getId() ID} of {@link AccessControlPermission} to get or create.
   * @return the existing {@link AccessControlPermission} for the given {@link AccessControlPermission#getId() ID} or a
   *         newly created and registered {@link AccessControlPermission}.
   */
  protected AccessControlPermission permission(String id) {

    AccessControl accessControl = getAccessControl(id);
    if (accessControl instanceof AccessControlPermission) {
      return (AccessControlPermission) accessControl;
    } else if (accessControl != null) {
      throw new IllegalStateException("Duplicate access control for ID '" + id + "'.");
    }
    AccessControlPermission permission = new AccessControlPermission(id);
    addAccessControl(permission);
    return permission;
  }

  /**
   * Creates a new {@link AccessControlGroup} for static configuration of access controls.
   *
   * @param groupId {@link AccessControlGroup#getId() ID} of {@link AccessControlGroup} to create.
   * @param permissionIds {@link AccessControlPermission#getId() ID}s of the {@link #permission(String) permissions} to
   *        {@link AccessControlGroup#getPermissions() use}.
   * @return the newly created and registered {@link AccessControlGroup}.
   */
  protected AccessControlGroup group(String groupId, String... permissionIds) {

    return group(groupId, Collections.<AccessControlGroup> emptyList(), permissionIds);
  }

  /**
   * Creates a new {@link AccessControlGroup} for static configuration of access controls.
   *
   * @param groupId {@link AccessControlGroup#getId() ID} of {@link AccessControlGroup} to create.
   * @param inherit single {@link AccessControlGroup} to {@link AccessControlGroup#getInherits() inherit}.
   * @param permissionIds {@link AccessControlPermission#getId() ID}s of the {@link #permission(String) permissions} to
   *        {@link AccessControlGroup#getPermissions() use}.
   * @return the newly created and registered {@link AccessControlGroup}.
   */
  protected AccessControlGroup group(String groupId, AccessControlGroup inherit, String... permissionIds) {

    return group(groupId, Collections.singletonList(inherit), permissionIds);
  }

  /**
   * Creates a new {@link AccessControlGroup} for static configuration of access controls.
   *
   * @param groupId {@link AccessControlGroup#getId() ID} of {@link AccessControlGroup} to create.
   * @param inherits {@link List} of {@link AccessControlGroup} to {@link AccessControlGroup#getInherits() inherit}.
   * @param permissionIds {@link AccessControlPermission#getId() ID}s of the {@link #permission(String) permissions} to
   *        {@link AccessControlGroup#getPermissions() use}.
   * @return the newly created and registered {@link AccessControlGroup}.
   */
  protected AccessControlGroup group(String groupId, List<AccessControlGroup> inherits, String... permissionIds) {

    AccessControlGroup group = new AccessControlGroup(groupId);
    group.setInherits(inherits);
    List<AccessControlPermission> permissions = new ArrayList<>(permissionIds.length);
    for (String permissionId : permissionIds) {
      permissions.add(permission(permissionId));
    }
    group.setPermissions(permissions);
    addAccessControl(group);
    return group;
  }

}
