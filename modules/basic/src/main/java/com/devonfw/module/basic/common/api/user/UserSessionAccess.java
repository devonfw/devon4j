package com.devonfw.module.basic.common.api.user;

import java.util.Locale;

/**
 * Access to properties of the user from the current session.
 *
 * @since 3.0.0
 */
public class UserSessionAccess {

  private static UserSessionProvider provider;

  /**
   * @return provider
   */
  static UserSessionProvider getProvider() {

    if (provider == null) {
      return getOrCreateProvider();
    }
    return provider;
  }

  private static synchronized UserSessionProvider getOrCreateProvider() {

    if (provider == null) {
      provider = new UserSessionProviderImpl();
    }
    return provider;
  }

  /**
   * @param provider new value of {@link #getProvider()}.
   */
  static void setProvider(UserSessionProvider provider) {

    if (UserSessionAccess.provider != null) {
      if (UserSessionAccess.provider == provider) {
        return;
      }
      throw new IllegalStateException(
          "Provider is already initialized! Please set the provider at the beginning of the bootstrapping of your application.");
    }
    UserSessionAccess.provider = provider;
  }

  /**
   * @return the login of the current user (e.g. "john.doe"). Will be {@code null} if called outside the scope of a
   *         current user session or before successful authentication.
   */
  public static String getUserLogin() {

    return getProvider().getUserLogin();
  }

  /**
   * @return the {@link Locale} of the current user, or the {@link Locale#getDefault() system default locale} as
   *         fallback.
   */
  public static Locale getUserLocale() {

    return getProvider().getUserLocale();
  }

}
