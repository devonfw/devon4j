package com.devonfw.module.security.jwt.authentication;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.devonfw.module.security.jwt.util.TokenConfigProperties;

/**
 * Interface used for Constructing {@link Authentication} object using {@link principal} and {@link List of
 * GrantedAuthority>}
 *
 * @since 3.3.0
 */
public interface AuthenticationTokenDetails {
  /**
   * Returns custom {@link Authentication} object based on the name configured using {@link TokenConfigProperties}
   *
   * @return {@link String}
   */
  public String getName();

  /**
   * This method used to fabricate {@link Authentication} object
   *
   * @return {@link Authentication}
   */
  public Authentication composeAuthenticationTokenDetails();

  /**
   * sets the user/principal
   *
   * @param principal or the user
   */
  public void setPrincipal(String principal);

  /**
   * @return user/principal
   */
  public String getPrincipal();

  /**
   * @return credentials
   */
  public String getCredentials();

  /**
   * sets the credentials
   *
   * @param credentials
   */
  public void setCredentials(String credentials);

  /**
   * @return List of {@link GrantedAuthority}
   */
  public List<GrantedAuthority> getAuthorities();

  /**
   * Sets the List of {@link GrantedAuthority}
   *
   * @param authorities List of {@link GrantedAuthority}
   */
  public void setAuthorities(List<GrantedAuthority> authorities);

}
