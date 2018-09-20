package com.devonfw.module.security.common.api.accesscontrol;

import java.security.Principal;
import java.util.Collection;

/**
 * This is the interface for a provide that allows to {@link #getAccessControlIds(Principal) get the permission groups}
 * for a {@link Principal}.
 *
 * @param <P> is the generic type of the {@link Principal} representing the user or subject.
 *
 */
public interface PrincipalAccessControlProvider<P extends Principal> {

  /**
   * @param principal is the {@link Principal} (user).
   * @return the {@link Collection} of {@link AccessControl#getId() IDs} with the groups of the given {@link Principal}.
   */
  Collection<String> getAccessControlIds(P principal);

}
