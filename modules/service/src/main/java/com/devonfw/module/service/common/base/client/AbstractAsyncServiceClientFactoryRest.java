package com.devonfw.module.service.common.base.client;

/**
 * Abstract base implementation of {@link AbstractAsyncServiceClientFactory} for REST.
 *
 * @since 2021.04.003
 */
public abstract class AbstractAsyncServiceClientFactoryRest extends AbstractAsyncServiceClientFactory {

  @Override
  protected ServiceClientTypeHandler getTypeHandler() {

    return ServiceClientTypeHandlerRest.get();
  }

}
