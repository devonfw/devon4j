package com.devonfw.module.service.common.base.context;

import com.devonfw.module.service.common.api.client.context.ServiceContext;

/**
 * The abstract base implementation of {@link ServiceContext}.
 *
 * @param <S> the generic type of the {@link #getApi() service API}.
 *
 * @since 3.0.0
 */
public abstract class AbstractServiceContext<S> implements ServiceContext<S> {

  private final Class<S> api;

  /**
   * The constructor.
   *
   * @param api the {@link #getApi() API}.
   */
  public AbstractServiceContext(Class<S> api) {
    super();
    this.api = api;
  }

  @Override
  public Class<S> getApi() {

    return this.api;
  }

}
