package com.devonfw.module.security.common.api.accesscontrol;

import java.util.Set;

/**
 * This is the interface for a provider of {@link AccessControl}s. It allows to
 * {@link #collectAccessControlIds(String, Set) collect} all {@link AccessControl}s for an ID of a {@link AccessControl}
 * (typically a {@link AccessControlGroup} or role). This is used to expand the groups provided by the access-manager
 * (authentication and identity-management) to the full set of {@link AccessControlPermission permissions} of the
 * {@link java.security.Principal user}.<br/>
 * The actual authorization can then check individual permissions of the user by simply checking for
 * {@link Set#contains(Object) contains} in the collected {@link Set}, what is very fast as security checks happen
 * frequently.
 *
 * @see PrincipalAccessControlProvider
 *
 */
public interface AccessControlProvider {

  /**
   * @param id is the {@link AccessControl#getId() ID} of the requested {@link AccessControl}.
   * @return the requested {@link AccessControl} or {@code null} if not found.
   */
  AccessControl getAccessControl(String id);

  /**
   * This method collects the {@link AccessControl#getId() IDs} of all {@link AccessControlPermission}s (or more
   * precisely of all {@link AccessControl}s) contained in the {@link AccessControl} {@link AccessControl#getId()
   * identified} by the given <code>groupId</code>.
   *
   * @see #collectAccessControls(String, Set)
   *
   * @param id is the {@link AccessControl#getId() ID} of the {@link AccessControl} (typically an
   *        {@link AccessControlGroup}) to collect.
   * @param permissions is the {@link Set} where to {@link Set#add(Object) add} the collected
   *        {@link AccessControl#getId() IDs}. This will include the given <code>groupId</code>.
   * @return {@code true} if the given <code>groupId</code> has been found, {@code false} otherwise.
   */
  boolean collectAccessControlIds(String id, Set<String> permissions);

  /**
   * This method collects the {@link AccessControl}s contained in the {@link AccessControl} 
   * {@link AccessControl#getId() identified} by the given <code>groupId</code>.
   *
   * @param id is the {@link AccessControl#getId() ID} of the {@link AccessControl} (typically an
   *        {@link AccessControlGroup}) to collect.
   * @param permissions is the {@link Set} where to {@link Set#add(Object) add} the collected {@link AccessControl}s.
   *        This will include the {@link AccessControl} {@link AccessControl#getId() identified} by the given
   *        <code>groupId</code>.
   * @return {@code true} if the given <code>groupId</code> has been found, {@code false} otherwise.
   */
  boolean collectAccessControls(String id, Set<AccessControl> permissions);

}
