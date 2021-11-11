package com.devonfw.module.service.common.base.client;

/**
 * Abstract base implementation of {@link AbstractSyncServiceClientFactory} for SOAP.
 *
 * @since 2021.04.003
 */
public abstract class AbstractSyncServiceClientFactoryWs extends AbstractSyncServiceClientFactory {

  @Override
  protected ServiceClientTypeHandler getTypeHandler() {

    return ServiceClientTypeHandlerWs.get();
  }

}
