package com.devonfw.module.security.common.api.authentication;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Extends {@link Authentication} with advanced features.
 *
 * @since 2020.04.001
 */
public interface AdvancedAuthentication extends Authentication {

  /**
   * @param <T> type of the expected return type.
   * @param key the {@link java.util.Map#containsKey(Object) key} of the requested attribute.
   * @return the attribute for the given {@code key} or {@code null} if not present.
   */
  default <T> T getAttribute(String key) {

    return null;
  }

  /**
   * @param key the {@link Map#containsKey(Object) key} to set. May not be {@code null}.
   * @param value the {@link Map#containsValue(Object) value} to set. May not be {@code null}.
   */
  default void setAttribute(String key, Object value) {

    throw new UnsupportedOperationException();
  }

  /**
   * @return the {@link #getAuthorities() authorities} as a {@link Set} of flat {@link String}s. Allows to improve
   *         performance of of checks if permissions are present or not.
   * @see #hasPermission(String)
   */
  Set<String> getPermissions();

  /**
   * @param permission the identifier of the permission to check.
   * @return {@code true} if the current user represented by this {@link Authentication} {@link #getPermissions() has}
   *         the given {@code permission}, {@code false} otherwise.
   */
  default boolean hasPermission(String permission) {

    return getPermissions().contains(permission);
  }

  /**
   * @param authentication the {@link Authentication} to check.
   * @param permission the identifier of the permission to check.
   * @return {@code true} if the current user represented by the given {@link Authentication} {@link #getPermissions()
   *         has} the given {@code permission}, {@code false} otherwise.
   */
  static boolean hasPermission(Authentication authentication, String permission) {

    if (authentication == null) {
      return false;
    }
    if (authentication instanceof AdvancedAuthentication) {
      return ((AdvancedAuthentication) authentication).hasPermission(permission);
    }
    for (GrantedAuthority authority : authentication.getAuthorities()) {
      if ((authority != null) && Objects.equals(authority.getAuthority(), permission)) {
        return true;
      }
    }
    return false;
  }

  /**
   * @return the {@link AdvancedAuthentication} of the user currently authenticated as associated with this thread.
   */
  static AdvancedAuthentication get() {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return null;
    }
    if (authentication instanceof AdvancedAuthentication) {
      return (AdvancedAuthentication) authentication;
    }
    Object principal = authentication.getPrincipal();
    if (principal instanceof AdvancedAuthentication) {
      return (AdvancedAuthentication) principal;
    }
    Object details = authentication.getDetails();
    if (details instanceof AdvancedAuthentication) {
      return (AdvancedAuthentication) details;
    }
    throw new IllegalStateException(authentication.getClass().getSimpleName());
  }

  /**
   * @param authentication the {@link Authentication}.
   * @return the {@link AdvancedAuthentication#getPermissions() permissions}.
   */
  static Set<String> getPermissions(Authentication authentication) {

    if (authentication instanceof AdvancedAuthentication) {
      return ((AdvancedAuthentication) authentication).getPermissions();
    } else {
      return DefaultAuthentication.authorities2permissions(authentication.getAuthorities());
    }
  }

}
