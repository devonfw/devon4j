package com.devonfw.module.security.common.api.authentication;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Implementation of {@link AdvancedAuthentication} to be used by default in devon4j apps after successful
 * authentication.
 *
 * @since 2020.04.001
 */
public class DefaultAuthentication extends UsernamePasswordAuthenticationToken implements AdvancedAuthentication {

  private static final long serialVersionUID = 1L;

  private final Set<String> permissions;

  private Map<String, Object> attributes;

  /**
   * The constructor.
   *
   * @param principal the {@link #getPrincipal() principal}.
   * @param credentials the optional {@link #getCredentials() credentials}.
   * @param authorities the {@link #getAuthorities() authorities}.
   */
  public DefaultAuthentication(Object principal, Object credentials,
      Collection<? extends GrantedAuthority> authorities) {

    this(principal, credentials, authorities, null, null);
  }

  /**
   * The constructor.
   *
   * @param principal the {@link #getPrincipal() principal}.
   * @param credentials the optional {@link #getCredentials() credentials}.
   * @param authorities the {@link #getAuthorities() authorities}.
   * @param attributes the {@link Map} of {@link #getAttribute(String) attributes}. Use
   *        {@link java.util.Collections#unmodifiableMap(Map)} to prevent {@link #setAttribute(String, Object)
   *        mutation}.
   */
  public DefaultAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities,
      Map<String, Object> attributes) {

    this(principal, credentials, authorities, null, attributes);
  }

  /**
   * The constructor.
   *
   * @param principal the {@link #getPrincipal() principal}.
   * @param credentials the optional {@link #getCredentials() credentials}.
   * @param permissions the {@link #getPermissions() permissions}.
   */
  public DefaultAuthentication(Object principal, Object credentials, Set<String> permissions) {

    this(principal, credentials, permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()),
        permissions, null);
  }

  /**
   * The constructor.
   *
   * @param principal the {@link #getPrincipal() principal}.
   * @param credentials the optional {@link #getCredentials() credentials}.
   * @param permissions the {@link #getPermissions() permissions}.
   * @param attributes the {@link Map} of {@link #getAttribute(String) attributes}. Use
   *        {@link java.util.Collections#unmodifiableMap(Map)} to prevent {@link #setAttribute(String, Object)
   *        mutation}.
   */
  public DefaultAuthentication(Object principal, Object credentials, Set<String> permissions,
      Map<String, Object> attributes) {

    this(principal, credentials, permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()),
        permissions, attributes);
  }

  private DefaultAuthentication(Object principal, Object credentials,
      Collection<? extends GrantedAuthority> authorities, Set<String> permissions, Map<String, Object> attributes) {

    super(principal, credentials, authorities);
    this.attributes = attributes;
    if (permissions == null) {
      this.permissions = authorities2permissions(authorities);
    } else {
      this.permissions = permissions;
    }
  }

  @Override
  public Set<String> getPermissions() {

    return this.permissions;
  }

  /**
   * @return the attributes {@link Map}.
   */
  public Map<String, Object> getAttributes() {

    return this.attributes;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getAttribute(String key) {

    if (this.attributes != null) {
      Object value = this.attributes.get(key);
      if (value != null) {
        return (T) value;
      }
    }
    return AdvancedAuthentication.super.getAttribute(key);
  }

  @Override
  public void setAttribute(String key, Object value) {

    Objects.requireNonNull(key, "key");
    if (value == null) {
      throw new IllegalArgumentException("Value can not be null for key: " + key);
    }
    if (this.attributes == null) {
      this.attributes = new HashMap<>();
    }
    Object duplicate = this.attributes.putIfAbsent(key, value);
    if (duplicate != null) {
      throw new IllegalArgumentException("Attribute for key '" + key + "' is already set!");
    }
  }

  /**
   * @param authorities the {@link org.springframework.security.core.Authentication#getAuthorities() authorities} to
   *        convert.
   * @return the {@link Set} of {@link GrantedAuthority#getAuthority() authorities}.
   */
  static Set<String> authorities2permissions(Collection<? extends GrantedAuthority> authorities) {

    Set<String> permissions = new HashSet<>(authorities.size());
    for (GrantedAuthority authority : authorities) {
      if (authority != null) {
        permissions.add(authority.getAuthority());
      }
    }
    return permissions;
  }

}
