package com.devonfw.module.logging.common.impl;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Class which provides {@link Marker}s for differential logging. Implements "MultiMarker"s
 * ({@link org.owasp.security.logging.MultiMarker}) for optimal filtering if the dependency org.owasp is available, or
 * corresponding conventional Markers as a fall back solution.
 * <P>
 * Example usage:
 *
 * <pre>
 * <code>
 * LOG.info({@link SecureLogging}.{@link #SECURITY_FAILURE_CONFIDENTIAL}, "Confidential Security Failure message.");
 * </code>
 * </pre>
 *
 * Example filters for appenders in logback.xml to accept or reject the above log event:
 *
 * <pre>
 * {@code <}filter class="{@link org.owasp.security.logging.filter.SecurityMarkerFilter}"{@code />}
 * {@code <}filter class="{@link org.owasp.security.logging.filter.ExcludeClassifiedMarkerFilter}"{@code />}
 * </pre>
 */
public class SecureLogging {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(SecureLogging.class);

  private static final String EXT_CLASS = "org.owasp.security.logging.SecurityMarkers";

  private static final String METHOD_NAME = "getMarker";

  private static boolean initialized = false;

  private static Marker markerSecurSuccConfid = null;

  private static Marker markerSecurFailConfid = null;

  private static Marker markerSecurAuditConfid = null;

  private static final String RESTRICTED_MARKER_NAME = "RESTRICTED";

  private static final String CONFIDENTIAL_MARKER_NAME = "CONFIDENTIAL";

  // private static final String SECRET_MARKER_NAME = "SECRET"; // see below.

  private static final String SECURITY_SUCCESS_MARKER_NAME = "SECURITY SUCCESS";

  private static final String SECURITY_FAILURE_MARKER_NAME = "SECURITY FAILURE";

  private static final String SECURITY_AUDIT_MARKER_NAME = "SECURITY AUDIT";

  // MultiMarkers by OWASP do not contain a space between the individual names, so we stick to this behavior.
  private static final String SECURITY_SUCCESS_CONFIDENTIAL_MARKER_NAME = "SECURITY SUCCESSCONFIDENTIAL";

  private static final String SECURITY_FAILURE_CONFIDENTIAL_MARKER_NAME = "SECURITY FAILURECONFIDENTIAL";

  private static final String SECURITY_AUDIT_CONFIDENTIAL_MARKER_NAME = "SECURITY AUDITCONFIDENTIAL";

  /**
   * Marker for Restricted log events.
   */
  public static final Marker RESTRICTED = MarkerFactory.getDetachedMarker(RESTRICTED_MARKER_NAME);

  /**
   * Marker for Confidential log events. Usage with OWASP provides possibility for masking, e.g. of passwords.
   */
  public static final Marker CONFIDENTIAL = MarkerFactory.getDetachedMarker(CONFIDENTIAL_MARKER_NAME);

  /**
   * Marker for Secret log events. This shall not be used until a clear use-case is defined and corresponding measures
   * are implemented in the logging chain. By default, secret information shall not be logged.
   */
  // public static final Marker SECRET = MarkerFactory.getDetachedMarker(SECRET_MARKER_NAME);

  /**
   * Marker for Security Success log events.
   */
  public static final Marker SECURITY_SUCCESS = MarkerFactory.getDetachedMarker(SECURITY_SUCCESS_MARKER_NAME);

  /**
   * Marker for Security Failure log events.
   */
  public static final Marker SECURITY_FAILURE = MarkerFactory.getDetachedMarker(SECURITY_FAILURE_MARKER_NAME);

  /**
   * Marker or MultiMarker for Confidential Security Success log events.
   */
  public static final Marker SECURITY_SUCCESS_CONFIDENTIAL = getMarkerSecurSuccConfid();

  /**
   * Marker or MultiMarker for Confidential Security Failure log events.
   */
  public static final Marker SECURITY_FAILURE_CONFIDENTIAL = getMarkerSecurFailConfid();

  /**
   * Marker or MultiMarker for Confidential Security Audit log events.
   */
  public static final Marker SECURITY_AUDIT_CONFIDENTIAL = getMarkerSecurAuditConfid();

  private SecureLogging() {
  }

  private static Marker getMarkerSecurSuccConfid() {

    initMarkers();
    return markerSecurSuccConfid;
  }

  private static Marker getMarkerSecurFailConfid() {

    initMarkers();
    return markerSecurFailConfid;
  }

  private static Marker getMarkerSecurAuditConfid() {

    initMarkers();
    return markerSecurAuditConfid;
  }

  /**
   * Main method to initialize the combined {@link Marker}s provided by this class.
   */
  private static void initMarkers() {

    if (initialized)
      return;

    Class<?> cExtClass = findExtClass(EXT_CLASS);

    if (cExtClass.isAssignableFrom(String.class)) {
      createDefaultMarkers();
    } else {
      createMultiMarkers(cExtClass);
    }

    if (!initialized)
      LOG.warn("SecureLogging Markers could not be initialized!");
    else
      LOG.debug("SecureLogging Markers created: '{}', ...", markerSecurSuccConfid.getName());
    return;
  }

  private static void createDefaultMarkers() {

    LOG.debug("Creating default markers.");
    markerSecurSuccConfid = MarkerFactory.getDetachedMarker(SECURITY_SUCCESS_CONFIDENTIAL_MARKER_NAME);
    markerSecurFailConfid = MarkerFactory.getDetachedMarker(SECURITY_FAILURE_CONFIDENTIAL_MARKER_NAME);
    markerSecurAuditConfid = MarkerFactory.getDetachedMarker(SECURITY_AUDIT_CONFIDENTIAL_MARKER_NAME);
    initialized = true;
  }

  private static void createMultiMarkers(Class<?> cExtClass) {

    LOG.debug("Creating MultiMarkers.");

    Object objExtClass = null;
    try {
      objExtClass = cExtClass.newInstance();
      Class<?>[] paramTypes = { Marker[].class }; // the method to invoke is "getMarker(Marker... markers)".
      Method method = cExtClass.getMethod(METHOD_NAME, paramTypes);

      Marker[] markerArray = { MarkerFactory.getDetachedMarker(SECURITY_SUCCESS_MARKER_NAME),
      MarkerFactory.getDetachedMarker(CONFIDENTIAL_MARKER_NAME) };
      markerSecurSuccConfid = (Marker) method.invoke(objExtClass, (Object) markerArray);
      markerArray[0] = MarkerFactory.getDetachedMarker(SECURITY_FAILURE_MARKER_NAME);
      markerSecurFailConfid = (Marker) method.invoke(objExtClass, (Object) markerArray);
      markerArray[0] = MarkerFactory.getDetachedMarker(SECURITY_AUDIT_MARKER_NAME);
      markerSecurAuditConfid = (Marker) method.invoke(objExtClass, (Object) markerArray);
      initialized = true;

    } catch (Exception e) {
      LOG.warn("Error getting Method '{}' of Class '{}'. Falling back to default.", METHOD_NAME, cExtClass.getName());
      LOG.warn("Exception occurred.", e);
      e.printStackTrace();
      createDefaultMarkers();
    }
  }

  /**
   * @return True if the dependency is available.
   */
  public static boolean hasExtClass() {

    Class<?> cExtClass = findExtClass(EXT_CLASS);
    return (!cExtClass.isAssignableFrom(String.class));
  }

  /**
   * @return The given {@link Class} if parameter 'className' can be resolved, otherwise {@link String}.class.
   */
  private static Class<?> findExtClass(String className) {

    Class<?> cExtClass;
    try {
      cExtClass = Class.forName(className);
      return cExtClass;
    } catch (Exception e) {
      LOG.debug("Class '{}' or one of its dependencies is not present.", className);
      cExtClass = String.class;
      return cExtClass;
    }
  }

}
