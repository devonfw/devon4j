package com.devonfw.module.service.common.api.header;

import com.devonfw.module.service.common.api.client.context.ServiceContext;

/**
 * Extends {@link ServiceContext} and allows to {@link #setHeader(String, String) set headers} to the underlying network
 * protocol.
 *
 * @param <S> the generic type of the {@link #getApi() service API}.
 *
 * @since 3.0.0
 */
public interface ServiceHeaderContext<S> extends ServiceContext<S> {

  /**
   * Adds a header to underlying network invocations (e.g. HTTP) triggered by a
   * {@link com.devonfw.module.service.common.api.client.ServiceClientFactory#create(Class) service client}.
   *
   * @param key the name of the header to set.
   * @param value the value of the header to set.
   */
  void setHeader(String key, String value);

}
