package com.devonfw.module.i18n.locale.impl;

import com.devonfw.module.i18n.locale.api.I18n;
import com.devonfw.module.i18n.locale.api.LocaleResourceFactory;

/**
 * Basic implementation of the {@link I18n} interface.
 *
 */

public class I18nImpl implements I18n {

  private LocaleResourceFactory localeResource;

  /**
   * The constructor.
   *
   * @param localeResource
   */
  public I18nImpl(LocaleResourceFactory localeResource) {

    super();
    this.localeResource = localeResource;
  }

  @Override
  public String getResourceObject(String locale, String filter) throws Exception {

    String jsonString = null;

    jsonString = this.localeResource.getResourceAsJson(locale, filter);

    return jsonString;
  }

}
