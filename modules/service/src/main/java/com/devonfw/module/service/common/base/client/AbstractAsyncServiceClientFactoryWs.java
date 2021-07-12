package com.devonfw.module.service.common.base.client;

/**
 * Abstract base implementation of {@link AbstractAsyncServiceClientFactory} for SOAP.
 *
 * @since 2021.04.003
 */
public abstract class AbstractAsyncServiceClientFactoryWs extends AbstractAsyncServiceClientFactory {

  @Override
  protected ServiceClientTypeHandler getTypeHandler() {

    return ServiceClientTypeHandlerWs.get();
  }

}
