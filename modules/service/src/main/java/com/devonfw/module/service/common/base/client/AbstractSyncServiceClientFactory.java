package com.devonfw.module.service.common.base.client;

import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.api.client.sync.SyncServiceClientFactory;

/**
 * Abstract base implementation of {@link SyncServiceClientFactory}.
 *
 * @since 2021.04.003
 */
public abstract class AbstractSyncServiceClientFactory extends PartialServiceClientFactory
    implements SyncServiceClientFactory {

  @Override
  public <S> S create(ServiceContext<S> context) {

    ServiceClientTypeHandler typeHandler = getTypeHandler();
    boolean responsible = typeHandler.isResponsibleForService(context);
    if (!responsible) {
      return null;
    }
    String url = typeHandler.getUrl(context);
    S serviceClient = createService(context, url);
    return serviceClient;
  }

  /**
   * @param <S> the generic type of the {@link ServiceContext#getApi() service API}.
   * @param context the {@link ServiceContext}.
   * @param url the resolved end-point URL of the service to invoke.
   * @return a new client stub for the service. See {@link #create(ServiceContext)} for further details.
   */
  protected abstract <S> S createService(ServiceContext<S> context, String url);

}
