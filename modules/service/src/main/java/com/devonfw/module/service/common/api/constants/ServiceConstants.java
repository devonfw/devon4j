package com.devonfw.module.service.common.api.constants;

/**
 * Constants for {@link com.devonfw.module.service.common.api.Service}s.
 *
 * @since 3.0.0
 */
public class ServiceConstants {

  /** Key for {@link Throwable#getMessage() error message}. */
  public static final String KEY_MESSAGE = "message";

  /** Key for error {@link java.util.UUID}. */
  public static final String KEY_UUID = "uuid";

  /** Key for error code. */
  public static final String KEY_CODE = "code";

  /** Key for HTTP status code. */
  public static final String KEY_STATUS = "status";

  /** Key for HTTP error. */
  public static final String KEY_ERROR = "error";

  /** Key for (validation) error details. */
  public static final String KEY_ERRORS = "errors";

  /** The services URL folder. */
  public static final String URL_FOLDER_SERVICES = "services";

  /** The services URL path. */
  public static final String URL_PATH_SERVICES = "/" + URL_FOLDER_SERVICES;

  /** The rest URL folder. */
  public static final String URL_FOLDER_REST = "rest";

  /** The web-service URL folder. */
  public static final String URL_FOLDER_WEB_SERVICE = "ws";

  /** The rest services URL path. */
  public static final String URL_PATH_REST_SERVICES = URL_PATH_SERVICES + "/" + URL_FOLDER_REST;

  /** The web-service URL path. */
  public static final String URL_PATH_WEB_SERVICES = URL_PATH_SERVICES + "/" + URL_FOLDER_WEB_SERVICE;

  /**
   * The variable that resolves to the
   * {@link com.devonfw.module.basic.common.api.reflect.Devon4jPackage#getApplication() technical name of the
   * application}.
   */
  public static final String VARIABLE_APP = "${app}";

  /**
   * The variable that resolves to the
   * {@link com.devonfw.module.basic.common.api.reflect.Devon4jPackage#getApplication() technical name of the
   * application}.
   */
  public static final String VARIABLE_LOCAL_SERVER_PORT = "${local.server.port}";

  /**
   * The variable that resolves to type of the service (e.g. "rest" for REST service and "ws" for SOAP service).
   */
  public static final String VARIABLE_TYPE = "${type}";
}
