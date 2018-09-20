package com.devonfw.module.cxf.common.impl.client;

import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import com.devonfw.module.basic.common.api.config.ConfigProperties;
import com.devonfw.module.cxf.common.impl.client.interceptor.PerformanceStartInterceptor;
import com.devonfw.module.cxf.common.impl.client.interceptor.PerformanceStopInterceptor;
import com.devonfw.module.cxf.common.impl.client.interceptor.TechnicalExceptionInterceptor;
import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.api.config.ServiceConfig;
import com.devonfw.module.service.common.api.constants.ServiceConstants;
import com.devonfw.module.service.common.api.sync.SyncServiceClientFactory;

/**
 * Abstract base implementation of {@link SyncServiceClientFactory} for service clients using Apache CXF.
 *
 * @since 3.0.0
 */
public abstract class SyncServiceClientFactoryCxf implements SyncServiceClientFactory {

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
   * @param context the {@link ServiceContext}.
   * @return the {@link ServiceContext#getUrl() URL} with additional post-processing such as resolving variables.
   */
  protected String getUrl(ServiceContext<?> context) {

    String url = context.getUrl();
    url = url.replace(ServiceConstants.VARIABLE_TYPE, getServiceTypeFolderName());
    return url;
  }

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
   * @return the resolved value for {@link ServiceConstants#VARIABLE_TYPE} such as e.g.
   *         {@link ServiceConstants#URL_FOLDER_REST}.
   */
  protected abstract String getServiceTypeFolderName();

  /**
   * @param context the {@link ServiceContext}.
   * @return the {@link HTTPClientPolicy} for the {@link ServiceContext#getConfig() configuration} or {@code null} to
   *         use defaults.
   */
  protected HTTPClientPolicy createClientPolicy(ServiceContext<?> context) {

    ConfigProperties timeoutConfig = context.getConfig().getChild(ServiceConfig.KEY_SEGMENT_TIMEOUT);
    if (!timeoutConfig.isEmpty()) {
      HTTPClientPolicy policy = new HTTPClientPolicy();
      Long connectionTimeout =
          timeoutConfig.getChild(ServiceConfig.KEY_SEGMENT_TIMEOUT_CONNECTION).getValue(Long.class);
      if (connectionTimeout != null) {
        policy.setConnectionTimeout(connectionTimeout.longValue());
      }
      Long responseTimeout = timeoutConfig.getChild(ServiceConfig.KEY_SEGMENT_TIMEOUT_RESPONSE).getValue(Long.class);
      if (responseTimeout != null) {
        policy.setReceiveTimeout(responseTimeout.longValue());
      }
      return policy;
    }
    return null;
  }

  /**
   * @param context the {@link ServiceContext}.
   * @return the display name of the service for exception or log messages.
   */
  protected String createServiceName(ServiceContext<?> context) {

    return context.getApi().getName();
  }

  /**
   * Applies CXF interceptors to the given {@code serviceClient}.
   *
   * @param context the {@link ServiceContext}.
   * @param client the {@link InterceptorProvider}.
   * @param serviceName the {@link #createServiceName(ServiceContext) service name}.
   */
  protected void applyInterceptors(ServiceContext<?> context, InterceptorProvider client, String serviceName) {

    client.getOutInterceptors().add(new PerformanceStartInterceptor());
    client.getInInterceptors().add(new PerformanceStopInterceptor());
    client.getInFaultInterceptors().add(new TechnicalExceptionInterceptor(serviceName));
  }

  /**
   * @param context the {@link ServiceContext}.
   * @param conduit the {@link HTTPConduit} where to apply the {@link HTTPClientPolicy} to.
   */
  protected void applyClientPolicy(ServiceContext<?> context, HTTPConduit conduit) {

    if (conduit == null) {
      return;
    }
    HTTPClientPolicy clientPolicy = createClientPolicy(context);
    if (clientPolicy != null) {
      conduit.setClient(clientPolicy);
    }
  }

  /**
   * Applies headers to the given {@code serviceClient}.
   *
   * @param context the {@link ServiceContext}.
   * @param serviceClient the service client instance.
   */
  protected abstract void applyHeaders(ServiceContext<?> context, Object serviceClient);

  /**
   * @param context the {@link ServiceContext}.
   * @return {@code true} if this implementation is responsibe for creating a service client corresponding to the given
   *         {@link ServiceContext}, {@code false} otherwise.
   */
  protected abstract boolean isResponsibleForService(ServiceContext<?> context);

}
