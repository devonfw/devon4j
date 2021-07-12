package com.devonfw.module.service.common.base.client;

import com.devonfw.module.service.common.api.client.AsyncServiceClient;
import com.devonfw.module.service.common.api.client.async.AsyncServiceClientFactory;
import com.devonfw.module.service.common.api.client.context.ServiceContext;

/**
 * Abstract base implementation of {@link AsyncServiceClientFactory}.
 *
 * @since 2021.04.003
 */
public abstract class AbstractAsyncServiceClientFactory extends PartialServiceClientFactory
    implements AsyncServiceClientFactory {

  @Override
  public <S> AsyncServiceClient<S> create(ServiceContext<S> context) {

    ServiceClientTypeHandler typeHandler = getTypeHandler();
    boolean responsible = typeHandler.isResponsibleForService(context);
    if (!responsible) {
      return null;
    }
    String url = typeHandler.getUrl(context);
    AsyncServiceClient<S> serviceClient = createService(context, url);
    return serviceClient;
  }

  /**
   * @param <S> the generic type of the {@link ServiceContext#getApi() service API}.
   * @param context the {@link ServiceContext}.
   * @param baseUrl the resolved end-point URL of the service to invoke.
   * @return a new client stub for the service. See {@link #create(ServiceContext)} for further details.
   */
  protected abstract <S> AsyncServiceClient<S> createService(ServiceContext<S> context, String baseUrl);

}
