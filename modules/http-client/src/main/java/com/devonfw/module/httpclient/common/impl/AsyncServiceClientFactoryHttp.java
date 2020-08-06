package com.devonfw.module.httpclient.common.impl;

import com.devonfw.module.service.common.api.client.AsyncServiceClient;
import com.devonfw.module.service.common.api.client.async.AsyncServiceClientFactory;
import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.base.client.PartialServiceClientFactory;

/**
 * Abstract base implementation of {@link AsyncServiceClientFactory} for service clients using Java HTTP client.
 *
 * @since 2020.08.001
 */
public abstract class AsyncServiceClientFactoryHttp extends PartialServiceClientFactory
    implements AsyncServiceClientFactory {

  @Override
  public <S> AsyncServiceClient<S> create(ServiceContext<S> context) {

    boolean responsible = isResponsibleForService(context);
    if (!responsible) {
      return null;
    }
    String serviceName = createServiceName(context);

    String url = getUrl(context);
    AsyncServiceClient<S> serviceClient = createService(context, url, serviceName);
    return serviceClient;
  }

  /**
   * @param <S> the generic type of the {@link ServiceContext#getApi() service API}.
   * @param context the {@link ServiceContext}.
   * @param url the resolved end-point URL of the service to invoke.
   * @param serviceName the {@link #createServiceName(ServiceContext) service name}.
   * @return a new client stub for the service. See {@link #create(ServiceContext)} for further details.
   */
  protected abstract <S> AsyncServiceClient<S> createService(ServiceContext<S> context, String url, String serviceName);

  /**
   * Applies headers to the given {@code serviceClient}.
   *
   * @param context the {@link ServiceContext}.
   * @param serviceClient the service client instance.
   */
  protected abstract void applyHeaders(ServiceContext<?> context, Object serviceClient);

}
