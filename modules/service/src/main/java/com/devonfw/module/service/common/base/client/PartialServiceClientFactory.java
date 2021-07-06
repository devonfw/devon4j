package com.devonfw.module.service.common.base.client;

/**
 * Abstract base implementation of a partial service client factory.
 *
 * @see com.devonfw.module.service.common.api.client.async.AsyncServiceClientFactory
 * @see com.devonfw.module.service.common.api.client.sync.SyncServiceClientFactory
 * @since 2020.08.001
 */
public abstract class PartialServiceClientFactory {

  /**
   * @return the {@link ServiceClientTypeHandler} for the underlying service type protocol.
   */
  protected abstract ServiceClientTypeHandler getTypeHandler();

}
