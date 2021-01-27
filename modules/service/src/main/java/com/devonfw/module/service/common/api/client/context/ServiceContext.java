package com.devonfw.module.service.common.api.client.context;

import java.util.Collection;

import com.devonfw.module.basic.common.api.config.ConfigProperties;

/**
 * This interface gives read access to contextual information of a
 * {@link com.devonfw.module.service.common.api.Service}.
 *
 * @param <S> type of the {@link #getApi() service API}.
 *
 * @since 3.0.0
 */
public interface ServiceContext<S> {

  /**
   * @return the {@link Class} reflecting the API of the externally provided
   *         {@link com.devonfw.module.service.common.api.Service}. E.g. a JAR-RS annotated interface. For flexibility
   *         and being not invasive the generic type is not bound to
   *         {@link com.devonfw.module.service.common.api.Service} ({@code S extends Service}).
   */
  Class<S> getApi();

  /**
   * @return the URL (or URI) of the remote service.
   */
  String getUrl();

  /**
   * @return a {@link Collection} with the available {@link #getHeader(String) header} names (keys).
   */
  Collection<String> getHeaderNames();

  /**
   * @param name the name (key) of the header to get.
   * @return the value of the requested header or {@code null} if undefined.
   */
  String getHeader(String name);

  /**
   * @return the {@link ConfigProperties} with configuration metadata.
   * @see com.devonfw.module.service.common.api.client.ServiceClientFactory#create(Class, java.util.Map)
   */
  ConfigProperties getConfig();

  /**
   * @param operation the invoked service operation (Java method called on API or URL path detail called via REST).
   * @return a {@link String} describing the invoked {@link com.devonfw.module.service.common.api.Service} with
   *         available details as provided.
   */
  default String getServiceDescription(String operation) {

    return getServiceDescription(null, getUrl());
  }

  /**
   * @param operation the invoked service operation (Java method called on API or URL path detail called via REST).
   * @param url the URL of the invoked service.
   * @return a {@link String} describing the invoked {@link com.devonfw.module.service.common.api.Service} with
   *         available details as provided.
   */
  default String getServiceDescription(String operation, String url) {

    StringBuilder sb = new StringBuilder(getApi().getName());
    if ((operation != null) && !operation.isEmpty()) {
      sb.append('#');
      sb.append(operation);
    }
    if ((url != null) && !url.isEmpty()) {
      sb.append('[');
      sb.append(url);
      sb.append(']');
    }
    return sb.toString();
  }

}
