package com.devonfw.module.basic.common.api.user;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * The default implementation of {@link UserSessionProvider} based on {@code spring-security}. In case you want to use a
 * different technology stack but still using {@code devon4j} simply create your own implementation and
 * {@link UserSessionAccess#setProvider(UserSessionProvider) set} it during bootstrapping.
 *
 * @since 3.0.0
 */
@SuppressWarnings("javadoc")
class UserSessionProviderImpl implements UserSessionProvider {

  @Override
  public String getUserLogin() {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return null;
    }
    return authentication.getName();
  }

  @Override
  public Locale getUserLocale() {

    return LocaleContextHolder.getLocale();
  }

}
