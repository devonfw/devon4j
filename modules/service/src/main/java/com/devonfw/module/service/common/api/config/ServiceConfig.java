package com.devonfw.module.service.common.api.config;

import com.devonfw.module.basic.common.api.config.ConfigProperties;
import com.devonfw.module.service.common.base.config.ServiceConfigProperties;

/**
 * Configuration for {@link com.devonfw.module.service.common.api.client.ServiceClientFactory} and all related
 * implementations.
 *
 * @see ServiceConfigProperties
 *
 * @since 3.0.0
 */
public interface ServiceConfig {

  /** The key segment for the client specific configuration sub-tree. */
  String KEY_SEGMENT_CLIENT = "client";

  /** The key segment for the URL of a {@link com.devonfw.module.service.common.api.Service}. */
  String KEY_SEGMENT_URL = "url";

  /** The key segment for the port of a {@link com.devonfw.module.service.common.api.Service}. */
  String KEY_SEGMENT_PORT = "port";

  /** The key segment for the host of a {@link com.devonfw.module.service.common.api.Service}. */
  String KEY_SEGMENT_HOST = "host";

  /** The key segment for the protocol of a {@link com.devonfw.module.service.common.api.Service}. */
  String KEY_SEGMENT_PROTOCOL = "protocol";

  /** The key segment for the WSDL settings of a SOAP {@link javax.jws.WebService}. */
  String KEY_SEGMENT_WSDL = "wsdl";

  /** The key segment for the boolean property to disable download (e.g. of {@link #KEY_SEGMENT_WSDL WSDL}). */
  String KEY_SEGMENT_DISABLE_DOWNLOAD = "disable-download";

  /** The key segment for the application specific configuration sub-tree. */
  String KEY_SEGMENT_APP = "app";

  /** The key segment for the default configuration sub-tree. */
  String KEY_SEGMENT_DEFAULT = "default";

  /** The key segment for the authentication mechanism (values are "basic", "oauth", etc.). */
  String KEY_SEGMENT_AUTH = "auth";

  /**
   * The key segment for the user configuration sub-tree.
   *
   * @see #KEY_SEGMENT_USER_LOGIN
   * @see #KEY_SEGMENT_USER_PASSWORD
   */
  String KEY_SEGMENT_USER = "user";

  /** The key segment for the {@link #KEY_SEGMENT_USER user} login name. */
  String KEY_SEGMENT_USER_LOGIN = "login";

  /** The key segment for the {@link #KEY_SEGMENT_USER user} password. */
  String KEY_SEGMENT_USER_PASSWORD = "password";

  /**
   * The key segment for the timeout configuration sub-tree.
   *
   * @see #KEY_SEGMENT_TIMEOUT_CONNECTION
   * @see #KEY_SEGMENT_TIMEOUT_RESPONSE
   */
  String KEY_SEGMENT_TIMEOUT = "timeout";

  /** The key segment for the {@link #KEY_SEGMENT_TIMEOUT timeout} to establish a connection in seconds. */
  String KEY_SEGMENT_TIMEOUT_CONNECTION = "connection";

  /** The key segment for the {@link #KEY_SEGMENT_TIMEOUT timeout} to wait for a response in seconds. */
  String KEY_SEGMENT_TIMEOUT_RESPONSE = "response";

  /** The value of {@link #KEY_SEGMENT_AUTH authentication} for basic auth. */
  String VALUE_AUTH_BASIC = "basic";

  /** The value of {@link #KEY_SEGMENT_AUTH authentication} for OAuth. */
  String VALUE_AUTH_OAUTH = "oauth";

  /**
   * The value of {@link #KEY_SEGMENT_AUTH authentication} for Basic, Oauth,JWT.
   **/

  String VALUE_AUTH_FORWARD = "authForward";

  /**
   * @return the root {@link ConfigProperties}-node with the configuration for services.
   */
  ConfigProperties asConfig();

  /**
   * @return the client {@link ConfigProperties}-node with the configuration for
   *         {@link com.devonfw.module.service.common.api.client.ServiceClientFactory} and all related implementations.
   */
  ConfigProperties asClientConfig();

}
