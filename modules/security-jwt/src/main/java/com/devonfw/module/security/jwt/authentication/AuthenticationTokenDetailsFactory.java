package com.devonfw.module.security.jwt.authentication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.core.Authentication;

/**
 * Factory class for Constructing {@link Authentication} object using {@link principal} and {@link List of
 * GrantedAuthority>}
 *
 * @since 3.3.0
 */
@Named
public class AuthenticationTokenDetailsFactory {
  private final Map<String, AuthenticationTokenDetails> authenticatonTokenDetailsMap;

  /**
   * The constructor.
   */
  public AuthenticationTokenDetailsFactory() {

    super();
    this.authenticatonTokenDetailsMap = new HashMap<>();
  }

  /**
   * Loads all the fabricated {@link AuthenticationTokenDetails} objects
   *
   * @param authenticationTokens the {@link List} of {@link AuthenticationTokenDetails}s .
   */
  @Inject
  public void setAuthenticationTokenDetails(List<AuthenticationTokenDetails> authenticationTokens) {

    for (AuthenticationTokenDetails authToken : authenticationTokens) {
      this.authenticatonTokenDetailsMap.put(authToken.getName(), authToken);
    }

  }

  /**
   * Gets the instance of {@link AuthenticationTokenDetails}
   *
   * @param name
   * @return {@link AuthenticationTokenDetails}
   */
  public AuthenticationTokenDetails getAuthenticationTokenDetails(String name) {

    return this.authenticatonTokenDetailsMap.get(name);
  }
}
