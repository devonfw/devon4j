package com.devonfw.module.service.common.base.client;

/**
 * Abstract base implementation of {@link AbstractSyncServiceClientFactory} for REST.
 *
 * @since 2021.04.003
 */
public abstract class AbstractSyncServiceClientFactoryRest extends AbstractSyncServiceClientFactory {

  @Override
  protected ServiceClientTypeHandler getTypeHandler() {

    return ServiceClientTypeHandlerRest.get();
  }

}
