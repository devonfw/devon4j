package com.devonfw.module.basic.common.api.user;

import java.util.Locale;

/**
 * Interface for the provider (SPI) for {@link UserSessionAccess}.
 *
 * @since 3.0.0
 */
public interface UserSessionProvider {

  /**
   * @return the {@link UserSessionAccess#getUserLogin() user login}.
   */
  String getUserLogin();

  /**
   * @return the {@link UserSessionAccess#getUserLocale() user locale}.
   */
  Locale getUserLocale();

}
