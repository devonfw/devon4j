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
 * Basic Implementation for {@link LocaleResourceFactory} based on {@link com.google.gson.Gson}
 *
 */
public class LocaleResourceImpl implements LocaleResourceFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(LocaleResourceImpl.class);

  @Override
  public String getResourceAsJson(String locale, String filter) throws Exception {

    String strJSON = null;
    Locale objLocale = null;
    HashMap<String, String> resourceMap = new HashMap<>();

    try {
      objLocale = I18nUtils.getLocale(locale);

      if (locale == null || locale.isEmpty() || !LocaleUtils.availableLocaleSet().contains(objLocale)) {
        throw new UnknownLocaleException(I18nConstants.INVALID_LOCALE);
      }

      resourceMap = (HashMap<String, String>) I18nUtils.getResourcesGeneratedFromDefaultImplAsMap(locale, objLocale);
      strJSON = I18nUtils.getResourcesAsJSON(resourceMap, filter);
    } catch (UnknownLocaleException de) {
      LOGGER.error("Exception in getResourcesAsJSONStringUsingDefaultImpl ", de);
      throw de;
    } catch (Exception e) {
      LOGGER.error("Exception in getResourcesAsJSONStringUsingDefaultImpl ", e);
      throw e;
    }
    return strJSON;
  }

}
