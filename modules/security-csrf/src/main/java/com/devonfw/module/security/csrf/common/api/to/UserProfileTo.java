package com.devonfw.module.security.csrf.common.api.to;

import com.devonfw.module.basic.common.api.to.AbstractTo;
import com.devonfw.module.security.csrf.common.api.UserProfile;

/**
 * Implementation of {@link UserProfile} as {AbstractTo TO}.
 */
public class UserProfileTo extends AbstractTo implements UserProfile {

  private static final long serialVersionUID = 1L;

  private String login;

  /**
   * The constructor.
   */
  public UserProfileTo() {

    super();
  }

  @Override
  public String getLogin() {

    return this.login;
  }

  /**
   * @param login the new {@link #getLogin() login}.
   */
  public void setLogin(String login) {

    this.login = login;
  }

  @Override
  protected void toString(StringBuilder buffer) {

    buffer.append(getClass().getSimpleName() + ": " + "login = " + this.login + ";");
  }
}