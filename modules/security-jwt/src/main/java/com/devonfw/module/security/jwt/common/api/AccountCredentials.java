package com.devonfw.module.security.jwt.common.api;

/**
 * Simple bean for {@link #getUsername() login} with {@link #getPassword() password}.
 *
 * @since 2020.04.001
 */
public class AccountCredentials {

  private String username;

  private String password;

  /**
   * @return the username to login.
   */
  public String getUsername() {

    return this.username;
  }

  /**
   * @param username new value of {@link #getUsername()}.
   */
  public void setUsername(String username) {

    this.username = username;
  }

  /**
   * @return the password of the user to login.
   */
  public String getPassword() {

    return this.password;
  }

  /**
   * @param password new value of {@link #getPassword()}.
   */
  public void setPassword(String password) {

    this.password = password;
  }

}
