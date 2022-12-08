package ${package}.general.common.security.accesscontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link AccessControlProvider} for configuration as code.
 *
 * @see ApplicationAccessControlConfig
 */
public abstract class AccessControlConfig implements AccessControlProvider {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(AccessControlConfig.class);

  /** @see #getAccessControl(String) */
  private final Map<String, AccessControl> id2nodeMap;

  /**
   * The constructor.
   */
  public AccessControlConfig() {

    super();
    this.id2nodeMap = new HashMap<>();
  }

  @Override
  public AccessControl getAccessControl(String nodeId) {

    return this.id2nodeMap.get(nodeId);
  }

  /**
   * Registers the given {@link AccessControl} and may be used for configuration of access controls during
   * bootstrapping. This method should not be used after the application startup (bootstrapping) has completed.
   *
   * @param accessControl the {@link AccessControl} to register.
   */
  protected void addAccessControl(AccessControl accessControl) {

    String id = accessControl.getId();
    AccessControl existing = this.id2nodeMap.get(id);
    if (existing == null) {
      this.id2nodeMap.put(id, accessControl);
      LOG.debug("Registered access control {}", accessControl);
    } else if (existing == accessControl) {
      LOG.debug("Access control {} was already registered.", accessControl);
    } else {
      throw new IllegalStateException("Duplicate access control with ID '" + id + "'.");
    }
  }

  @Override
  public boolean collectAccessControlIds(String groupId, Set<String> permissions) {

    AccessControl node = getAccessControl(groupId);
    if (node instanceof AccessControlGroup) {
      collectPermissionIds((AccessControlGroup) node, permissions);
    } else {
      // node does not exist or is a flat AccessControlPermission
      permissions.add(groupId);
    }
    return (node != null);
  }

  /**
   * Recursive implementation of {@link #collectAccessControlIds(String, Set)} for {@link AccessControlGroup}s.
   *
   * @param group is the {@link AccessControlGroup} to traverse.
   * @param permissions is the {@link Set} used to collect.
   */
  public void collectPermissionIds(AccessControlGroup group, Set<String> permissions) {

    boolean added = permissions.add(group.getId());
    if (!added) {
      // we have already visited this node, stop recursion...
      return;
    }
    for (AccessControlPermission permission : group.getPermissions()) {
      permissions.add(permission.getId());
    }
    for (AccessControlGroup inheritedGroup : group.getInherits()) {
      collectPermissionIds(inheritedGroup, permissions);
    }
  }

  @Override
  public boolean collectAccessControls(String groupId, Set<AccessControl> permissions) {

    AccessControl node = getAccessControl(groupId);
    if (node == null) {
      return false;
    }
    if (node instanceof AccessControlGroup) {
      collectPermissionNodes((AccessControlGroup) node, permissions);
    } else {
      // node is a flat AccessControlPermission
      permissions.add(node);
    }
    return true;
  }

  /**
   * Recursive implementation of {@link #collectAccessControls(String, Set)} for {@link AccessControlGroup}s.
   *
   * @param group is the {@link AccessControlGroup} to traverse.
   * @param permissions is the {@link Set} used to collect.
   */
  public void collectPermissionNodes(AccessControlGroup group, Set<AccessControl> permissions) {

    boolean added = permissions.add(group);
    if (!added) {
      // we have already visited this node, stop recursion...
      return;
    }
    for (AccessControlPermission permission : group.getPermissions()) {
      permissions.add(permission);
    }
    for (AccessControlGroup inheritedGroup : group.getInherits()) {
      collectPermissionNodes(inheritedGroup, permissions);
    }
  }

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
