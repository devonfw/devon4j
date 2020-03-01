package com.devonfw.module.security.jwt.util;

/**
 * Simple POJO that contain only the fields to retrieve from the request
 *
 * @since 3.3.0
 */
public class AccountCredentials {

  private String username;

  private String password;

  /**
   * @return username
   */
  public String getUsername() {

    return this.username;
  }

  /**
   * @param username new value of {@link #getusername}.
   */
  public void setUsername(String username) {

    this.username = username;
  }

  /**
   * @return password
   */
  public String getPassword() {

    return this.password;
  }

  /**
   * @param password new value of {@link #getpassword}.
   */
  public void setPassword(String password) {

    this.password = password;
  }

}
