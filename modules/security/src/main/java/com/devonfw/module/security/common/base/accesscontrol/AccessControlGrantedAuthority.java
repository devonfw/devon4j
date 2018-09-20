package com.devonfw.module.security.common.base.accesscontrol;

import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;

import com.devonfw.module.security.common.api.accesscontrol.AccessControl;

/**
 * Implementation of {@link GrantedAuthority} for a {@link AccessControl}.
 *
 */
public class AccessControlGrantedAuthority implements GrantedAuthority {

  /** UID for serialization. */
  private static final long serialVersionUID = 1L;

  private final AccessControl accessControl;

  /**
   * The constructor.
   *
   * @param accessControl the {@link #getAccessControl() access control}.
   */
  public AccessControlGrantedAuthority(AccessControl accessControl) {

    super();
    Objects.requireNonNull(accessControl, AccessControl.class.getSimpleName());
    this.accessControl = accessControl;
  }

  /**
   * @return the contained {@link AccessControl}.
   */
  public AccessControl getAccessControl() {

    return this.accessControl;
  }

  @Override
  public String getAuthority() {

    return this.accessControl.getId();
  }

  @Override
  public String toString() {

    return getAuthority();
  }

}
