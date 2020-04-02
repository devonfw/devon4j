package com.devonfw.module.i18n.locale.api;

import com.devonfw.module.i18n.locale.impl.LocaleNlsResourceImpl;
import com.devonfw.module.i18n.locale.impl.LocaleResourceImpl;

/**
 * Factory for generating JSON according to implementation see {@link LocaleResourceImpl} ,
 * {@link LocaleNlsResourceImpl}
 *
 */
public interface LocaleResourceFactory {
  /**
   * @param locale
   * @param filter
   * @return
   * @throws Exception
   */
  String getResourceAsJson(String locale, String filter) throws Exception;

}
