package com.devonfw.module.service.common.api.client.config;

import java.util.HashMap;
import java.util.Map;

import com.devonfw.module.basic.common.api.config.ConfigProperties;
import com.devonfw.module.basic.common.api.config.SimpleConfigProperties;
import com.devonfw.module.service.common.api.config.ServiceConfig;

/**
 * A builder used to create the configuration for {@link com.devonfw.module.service.common.api.Service} clients.
 *
 * @see com.devonfw.module.service.common.api.client.ServiceClientFactory#create(Class, Map)
 */
public class ServiceClientConfigBuilder {

  private final Map<String, String> map;

  /**
   * The constructor.
   */
  public ServiceClientConfigBuilder() {
    super();
    this.map = new HashMap<>();
  }

  /**
   * Use HTTP (without encryption).
   *
   * @return this instance for fluent API calls.
   */
  public ServiceClientConfigBuilder protocolHttp() {

    return protocol("http");
  }

  /**
   * Use HTTPS (with TLS).
   *
   * @return this instance for fluent API calls.
   */
  public ServiceClientConfigBuilder protocolHttps() {

    return protocol("https");
  }

  /**
   * @param protocol the protocol to use.
   * @return this instance for fluent API calls.
   */
  public ServiceClientConfigBuilder protocol(String protocol) {

    this.map.put(ServiceConfig.KEY_SEGMENT_PROTOCOL, protocol);
    return this;
  }

  /**
   * @param port the port-number used to build the {@link #url(String) URL}.
   * @return this instance for fluent API calls.
   */
  public ServiceClientConfigBuilder port(int port) {

    this.map.put(ServiceConfig.KEY_SEGMENT_PORT, Integer.toString(port));
    return this;
  }

  /**
   * @param host the host (name or IP) used to build the {@link #url(String) URL}.
   * @return this instance for fluent API calls.
   */
  public ServiceClientConfigBuilder host(String host) {

    this.map.put(ServiceConfig.KEY_SEGMENT_HOST, host);
    return this;
  }

  /**
   * @param url the entire URL of the {@link com.devonfw.module.service.common.api.Service}.
   * @return this instance for fluent API calls.
   */
  public ServiceClientConfigBuilder url(String url) {

    this.map.put(ServiceConfig.KEY_SEGMENT_URL, url);
    return this;
  }

  /**
   * Use basic {@link #auth(String) authentication}.
   *
   * @return this instance for fluent API calls.
   */
  public ServiceClientConfigBuilder authBasic() {

    return auth(ServiceConfig.VALUE_AUTH_BASIC);
  }

  /**
   * Use OAuth {@link #auth(String) authentication}.
   *
   * @return this instance for fluent API calls.
   */
  public ServiceClientConfigBuilder authOAuth() {

    return auth(ServiceConfig.VALUE_AUTH_OAUTH);
  }

  /**
   * Use OAuth {@link #auth(String) authentication}.
   *
   * @return this instance for fluent API calls.
   */
  public ServiceClientConfigBuilder authForward() {

    return auth(ServiceConfig.VALUE_AUTH_FORWARD);
  }

  /**
   * @param authentication the {@link ServiceConfig#KEY_SEGMENT_AUTH authentication} type.
   * @return this instance for fluent API calls.
   */
  public ServiceClientConfigBuilder auth(String authentication) {

    this.map.put(ServiceConfig.KEY_SEGMENT_AUTH, authentication);
    return this;
  }

  /**
   * @param login the {@link ServiceConfig#KEY_SEGMENT_USER_LOGIN login} of the {@link ServiceConfig#KEY_SEGMENT_USER
   *        user} for authentication.
   * @return this instance for fluent API calls.
   */
  public ServiceClientConfigBuilder userLogin(String login) {

    this.map.put(ServiceConfig.KEY_SEGMENT_USER + "." + ServiceConfig.KEY_SEGMENT_USER_LOGIN, login);
    return this;
  }

  /**
   * @param password the {@link ServiceConfig#KEY_SEGMENT_USER_PASSWORD password} of the
   *        {@link ServiceConfig#KEY_SEGMENT_USER user} for authentication.
   * @return this instance for fluent API calls.
   */
  public ServiceClientConfigBuilder userPassword(String password) {

    this.map.put(ServiceConfig.KEY_SEGMENT_USER + "." + ServiceConfig.KEY_SEGMENT_USER_PASSWORD, password);
    return this;
  }

  /**
   * @param timeout the {@link ServiceConfig#KEY_SEGMENT_TIMEOUT_CONNECTION connecion timeout} in seconds.
   * @return this instance for fluent API calls.
   */
  public ServiceClientConfigBuilder timeoutConnection(long timeout) {

    this.map.put(ServiceConfig.KEY_SEGMENT_TIMEOUT + "." + ServiceConfig.KEY_SEGMENT_TIMEOUT_CONNECTION,
        Long.toString(timeout));
    return this;
  }

  /**
   * @param timeout the {@link ServiceConfig#KEY_SEGMENT_TIMEOUT_RESPONSE response timeout} in seconds.
   * @return this instance for fluent API calls.
   */
  public ServiceClientConfigBuilder timeoutResponse(long timeout) {

    this.map.put(ServiceConfig.KEY_SEGMENT_TIMEOUT + "." + ServiceConfig.KEY_SEGMENT_TIMEOUT_RESPONSE,
        Long.toString(timeout));
    return this;
  }

  /**
   * @return the current configuration as flat {@link Map}.
   */
  public Map<String, String> buildMap() {

    return this.map;
  }

  /**
   * @return the current configuration as {@link ConfigProperties}.
   */
  public ConfigProperties buildConfigProperties() {

    return SimpleConfigProperties.ofFlatMap(this.map);
  }

}
