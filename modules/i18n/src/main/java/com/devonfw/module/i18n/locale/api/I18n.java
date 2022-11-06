package com.devonfw.module.i18n.locale.api;

/**
 * Interface for getting JSON using locale and filter.
 *
 */

public interface I18n {

  /**
   * Gets the JSON string for specified locale and filter
   *
   * @param locale locale
   * @param filter
   * @return Json String
   * @throws Exception
   */
  String getResourceObject(String locale, String filter) throws Exception;

}
