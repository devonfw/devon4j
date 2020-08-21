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
import com.devonfw.module.service.common.base.client.PartialServiceClientFactory;

/**
 * Abstract base implementation of {@link PartialServiceClientFactory} using Apache CXF.
 *
 * @since 2020.08.001
 */
public abstract class PartialServiceClientFactoryCxf extends PartialServiceClientFactory {

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
}
