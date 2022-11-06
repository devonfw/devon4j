package com.devonfw.module.i18n.common.util;

/**
 * Constants for i18n module
 *
 */
public class I18nConstants {

  /**
   * Constant for the character DOT
   */
  public static final String DOT = ".";

  /**
   * Constant for the character 0
   */
  public static final int ZERO = 0;

  /**
   * Constant for the Error message in case of Invalid Locale
   */
  public static final String INVALID_LOCALE = "Invalid Locale . Please provide valid Locale";

  /**
   * Constants for Resource path for NLS bundle
   *
   * The files needed to be store in following package, and file name will be starting with NlsBundleI18n
   *
   * sample - com\devonfw\module\i18n\common\api\nls\NlsBundleI18n_en.properties
   */
  public static final String NLS_BUNDLE_INTF_NAME = "com/devonfw/module/i18n/common/api/nls/NlsBundleI18n";

  /**
   * Empty String
   */
  public static final String EMPTY_STRING = "";

  /**
   *
   */
  // public static final String LOCALE_FILES_LOCATION = "src/main/resources/locale/";

  /**
   * Constants for resource for other locale integration using gson option
   *
   * The files needed to be store in following package, and file name will be starting with messages
   *
   * Sample file - locale\messages_en_US.properties
   */
  public static final String RESOURCE_BASENAME = "locale/messages";

  /**
   * Closing bracket
   */
  public static final String CLOSING_BRACE = "}";

  /**
   * Opening bracket
   */
  public static final String OPENING_BRACE = "{";

  /**
   * Underscore
   */
  public static final String UNDER_SCORE = "_";

  /**
   * Property file extension
   */
  public static final String PROPERTIES = "properties";

  /**
   * English locale
   *
   */
  public static final String en = "en";

  /**
   * Constants for Resource path for NLS bundle - qualified name.
   *
   * The files needed to be store in following package, and file name will be starting with NlsBundleI18n
   *
   * sample - com\devonfw\module\i18n\common\api\nls\NlsBundleI18n_en.properties
   */
  public static final String NLS_BUNDLE_INTF_QUAL_NAME = "com.devonfw.module.i18n.common.api.nls.NlsBundleI18n";
}
