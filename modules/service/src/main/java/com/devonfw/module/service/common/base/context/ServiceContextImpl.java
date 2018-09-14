package com.devonfw.module.service.common.base.context;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.devonfw.module.basic.common.api.config.ConfigProperties;
import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.api.client.discovery.ServiceDiscoveryContext;
import com.devonfw.module.service.common.api.config.ServiceConfig;
import com.devonfw.module.service.common.api.header.ServiceHeaderContext;

/**
 * Implementation of {@link ServiceContext}.
 *
 * @param <S> the generic type of the {@link #getApi() service API}.
 *
 * @since 3.0.0
 */
public class ServiceContextImpl<S> extends AbstractServiceContext<S>
    implements ServiceHeaderContext<S>, ServiceDiscoveryContext<S> {

  private final Map<String, String> headers;

  private final Collection<String> headerNames;

  private ConfigProperties configProperties;

  /**
   * The constructor.
   *
   * @param api the {@link #getApi() API}.
   * @param configProperties the initial {@link ConfigProperties}. May be {@link ConfigProperties#isEmpty() empty}.
   */
  public ServiceContextImpl(Class<S> api, ConfigProperties configProperties) {
    super(api);
    this.headers = new HashMap<>();
    this.headerNames = Collections.unmodifiableSet(this.headers.keySet());
    this.configProperties = configProperties;
  }

  @Override
  public String getUrl() {

    return this.configProperties.getChildValue(ServiceConfig.KEY_SEGMENT_URL);
  }

  @Override
  public Collection<String> getHeaderNames() {

    return this.headerNames;
  }

  @Override
  public String getHeader(String name) {

    return this.headers.get(name);
  }

  @Override
  public void setHeader(String key, String value) {

    this.headers.put(key, value);
  }

  @Override
  public ConfigProperties getConfig() {

    return this.configProperties;
  }

  @Override
  public void setConfig(ConfigProperties configProperties) {

    String url = getUrl();
    if (url != null) {
      throw new IllegalStateException(
          "Discovery for " + getApi() + " is invalid as it has already been discovered (" + url + ").");
    }
    String newUrl = configProperties.getChildValue(ServiceConfig.KEY_SEGMENT_URL);
    if (newUrl == null) {
      throw new IllegalStateException("Discovery for " + getApi() + " is invalid as no URL has been discovered.");
    }
    this.configProperties = configProperties;
  }

}
