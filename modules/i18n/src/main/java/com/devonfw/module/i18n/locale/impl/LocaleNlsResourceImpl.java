package com.devonfw.module.i18n.locale.impl;

import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.lang.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devonfw.module.i18n.common.api.exception.UnknownLocaleException;
import com.devonfw.module.i18n.common.util.I18nConstants;
import com.devonfw.module.i18n.common.util.I18nUtils;
import com.devonfw.module.i18n.locale.api.LocaleResourceFactory;

/**
 * NLS(Native Language Support) based Implementation for {@link LocaleResourceFactory}
 *
 */
public class LocaleNlsResourceImpl implements LocaleResourceFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(I18nImpl.class);

  @Override
  public String getResourceAsJson(String locale, String filter) throws Exception {

    String strJSON = null;
    HashMap<String, String> resourceMap = new HashMap<>();
    Locale objLocale = null;

    try {
      objLocale = I18nUtils.getLocale(locale);
      if (locale == null || locale.isEmpty() || !LocaleUtils.availableLocaleSet().contains(objLocale)) {
        throw new UnknownLocaleException(I18nConstants.INVALID_LOCALE);
      } else {
        resourceMap = (HashMap<String, String>) I18nUtils.getResourcesGeneratedFromMMMAsMap(objLocale);
        strJSON = I18nUtils.getResourcesAsJSON(resourceMap, filter);
      }
    } catch (UnknownLocaleException de) {
      LOGGER.error("Exception in getResourcesAsJSONStringUsingMMM ", de);
      throw de;
    } catch (Exception e) {
      LOGGER.error("Exception in getResourcesAsJSONStringUsingMMM ", e);
      throw e;
    }
    return strJSON;
  }

}
