package com.devonfw.module.cxf.common.impl.client;

import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.transport.http.HTTPConduit;

import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.api.client.sync.SyncServiceClientFactory;

/**
 * Abstract base implementation of {@link SyncServiceClientFactory} for service clients using Apache CXF.
 *
 * @since 3.0.0
 */
public abstract class SyncServiceClientFactoryCxf extends PartialServiceClientFactoryCxf implements SyncServiceClientFactory {

  @Override
  public <S> S create(ServiceContext<S> context) {

    boolean responsible = isResponsibleForService(context);
    if (!responsible) {
      return null;
    }
    String serviceName = createServiceName(context);

    String url = getUrl(context);
    S serviceClient = createService(context, url, serviceName);

    applyAspects(context, serviceClient, serviceName);
    return serviceClient;
  }

  /**
   * @param <S> the generic type of the {@link ServiceContext#getApi() service API}.
   * @param context the {@link ServiceContext}.
   * @param url the resolved end-point URL of the service to invoke.
   * @param serviceName the {@link #createServiceName(ServiceContext) service name}.
   * @return a new client stub for the service. See {@link #create(ServiceContext)} for further details.
   */
  protected abstract <S> S createService(ServiceContext<S> context, String url, String serviceName);

  /**
   * Implementations should call the following methods:
   * <ul>
   * <li>{@link #applyInterceptors(ServiceContext, InterceptorProvider, String)}</li>
   * <li>{@link #applyClientPolicy(ServiceContext, HTTPConduit)}</li>
   * <li>{@link #applyHeaders(ServiceContext, Object)}</li>
   * </ul>
   *
   * @param <S> the generic type of the {@link ServiceContext#getApi() service API}.
   * @param context the {@link ServiceContext}.
   * @param serviceClient the {@link #create(ServiceContext) created service client stub}.
   * @param serviceName the {@link #createServiceName(ServiceContext) service name}.
   */
  protected abstract <S> void applyAspects(ServiceContext<S> context, S serviceClient, String serviceName);

  /**
   * Applies headers to the given {@code serviceClient}.
   *
   * @param context the {@link ServiceContext}.
   * @param serviceClient the service client instance.
   */
  protected abstract void applyHeaders(ServiceContext<?> context, Object serviceClient);

}
