package com.devonfw.module.cxf.common.impl.client;

import javax.inject.Inject;

import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import com.devonfw.module.basic.common.api.config.ConfigProperties;
import com.devonfw.module.cxf.common.impl.client.interceptor.PerformanceStartInterceptor;
import com.devonfw.module.cxf.common.impl.client.interceptor.PerformanceStopInterceptor;
import com.devonfw.module.cxf.common.impl.client.interceptor.TechnicalExceptionInterceptor;
import com.devonfw.module.service.common.api.client.ServiceClientErrorFactory;
import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.api.config.ServiceConfig;
import com.devonfw.module.service.common.base.client.AbstractSyncServiceClientFactory;

/**
 * Abstract base implementation of {@link AbstractSyncServiceClientFactory} for service clients using Apache CXF.
 *
 * @since 3.0.0
 */
public abstract class SyncServiceClientFactoryCxf extends AbstractSyncServiceClientFactory {

  private ServiceClientErrorFactory errorFactory;

  /**
   * @return the {@link ServiceClientErrorFactory}.
   */
  public ServiceClientErrorFactory getErrorFactory() {

    return this.errorFactory;
  }

  /**
   * @param errorFactory new value of {@link #getErrorFactory() errorFactory} to {@link Inject}.
   */
  @Inject
  public void setErrorFactory(ServiceClientErrorFactory errorFactory) {

    this.errorFactory = errorFactory;
  }

  /**
   * @param context the {@link ServiceContext}.
   * @return the {@link HTTPClientPolicy} for the {@link ServiceContext#getConfig() configuration} or {@code null} to
   *         use defaults.
   */
  protected HTTPClientPolicy createClientPolicy(ServiceContext<?> context) {

    ConfigProperties timeoutConfig = context.getConfig().getChild(ServiceConfig.KEY_SEGMENT_TIMEOUT);
    if (!timeoutConfig.isEmpty()) {
      HTTPClientPolicy policy = new HTTPClientPolicy();
      Long connectionTimeout = timeoutConfig.getChild(ServiceConfig.KEY_SEGMENT_TIMEOUT_CONNECTION)
          .getValue(Long.class);
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
   * Applies CXF interceptors to the given {@code serviceClient}.
   *
   * @param context the {@link ServiceContext}.
   * @param client the {@link InterceptorProvider}.
   */
  protected void applyInterceptors(ServiceContext<?> context, InterceptorProvider client) {

    client.getOutInterceptors().add(new PerformanceStartInterceptor());
    client.getInInterceptors().add(new PerformanceStopInterceptor());
    client.getInFaultInterceptors().add(new TechnicalExceptionInterceptor(getErrorFactory(), context));
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
   * Implementations should call the following methods:
   * <ul>
   * <li>{@link #applyInterceptors(ServiceContext, InterceptorProvider)}</li>
   * <li>{@link #applyClientPolicy(ServiceContext, HTTPConduit)}</li>
   * <li>{@link #applyHeaders(ServiceContext, Object)}</li>
   * </ul>
   *
   * @param <S> the generic type of the {@link ServiceContext#getApi() service API}.
   * @param context the {@link ServiceContext}.
   * @param serviceClient the {@link #create(ServiceContext) created service client stub}.
   */
  protected abstract <S> void applyAspects(ServiceContext<S> context, S serviceClient);

  /**
   * Applies headers to the given {@code serviceClient}.
   *
   * @param context the {@link ServiceContext}.
   * @param serviceClient the service client instance.
   */
  protected abstract void applyHeaders(ServiceContext<?> context, Object serviceClient);

}
