package com.devonfw.module.security.jwt.authentication;

import java.util.List;

import javax.inject.Named;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Default Implementation of {@link AuthenticationTokenDetails}
 *
 * @since 3.3.0
 */
@Named
public class DefaultAuthenticationTokenDetailsImpl implements AuthenticationTokenDetails {

  private final static String NAME = "JWT_DEFAULT";

  private String principal;

  private String credentials;

  private List<GrantedAuthority> authorities;

  /**
   * @return principal
   */
  @Override
  public String getPrincipal() {

    return this.principal;
  }

  /**
   * @param principal new value of {@link #getPrincipal}.
   */
  @Override
  public void setPrincipal(String principal) {

    this.principal = principal;
  }

  /**
   * @return credentials
   */
  @Override
  public String getCredentials() {

    return this.credentials;
  }

  /**
   * @param credentials new value of {@link #getCredentials}.
   */
  @Override
  public void setCredentials(String credentials) {

    this.credentials = credentials;
  }

  /**
   * @return authorities
   */
  @Override
  public List<GrantedAuthority> getAuthorities() {

    return this.authorities;
  }

  /**
   * @param authorities new value of {@link #getAuthorities}.
   */
  @Override
  public void setAuthorities(List<GrantedAuthority> authorities) {

    this.authorities = authorities;
  }

  @Override
  public Authentication composeAuthenticationTokenDetails() {

    return new UsernamePasswordAuthenticationToken(this.principal, this.credentials, this.authorities);
  }

  @Override
  public String getName() {

    return NAME;
  }

}
