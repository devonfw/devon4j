package com.devonfw.module.cxf.common.impl.client.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;

import com.devonfw.module.cxf.common.impl.client.SyncServiceClientFactoryCxf;
import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.api.client.sync.SyncServiceClientFactory;
import com.devonfw.module.service.common.base.client.ServiceClientTypeHandler;
import com.devonfw.module.service.common.base.client.ServiceClientTypeHandlerRest;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * Implementation of {@link SyncServiceClientFactory} for JAX-RS REST service clients using Apache CXF.
 *
 * @since 3.0.0
 */
public class SyncServiceClientFactoryCxfRest extends SyncServiceClientFactoryCxf {

  private JacksonJsonProvider jsonProvider;

  /**
   * The constructor.
   */
  public SyncServiceClientFactoryCxfRest() {

    super();
  }

  @Override
  protected ServiceClientTypeHandler getTypeHandler() {

    return ServiceClientTypeHandlerRest.get();
  }

  /**
   * @param jsonProvider the {@link JacksonJsonProvider} to {@link Inject}.
   */
  @Inject
  public void setJsonProvider(JacksonJsonProvider jsonProvider) {

    this.jsonProvider = jsonProvider;
  }

  @Override
  protected <S> void applyAspects(ServiceContext<S> context, S serviceClient) {

    ClientConfiguration clientConfig = WebClient.getConfig(serviceClient);
    applyInterceptors(context, clientConfig);
    applyClientPolicy(context, clientConfig.getHttpConduit());
    applyHeaders(context, serviceClient);
  }

  @Override
  protected <S> S createService(ServiceContext<S> context, String url) {

    List<Object> providers = createProviderList(context);
    S service = JAXRSClientFactory.create(url, context.getApi(), providers);
    applyAspects(context, service);
    return service;
  }

  @Override
  protected void applyHeaders(ServiceContext<?> context, Object serviceClient) {

    Collection<String> headerNames = context.getHeaderNames();
    if (!headerNames.isEmpty()) {
      Client webClient = WebClient.client(serviceClient);
      for (String headerName : headerNames) {
        webClient.header(headerName, context.getHeader(headerName));
      }
    }
  }

  /**
   * @param context the {@link ServiceContext}.
   * @return the {@link List} of {@link javax.ws.rs.ext.Provider}s.
   */
  protected List<Object> createProviderList(ServiceContext<?> context) {

    List<Object> providers = new ArrayList<>();
    if (this.jsonProvider != null) {
      providers.add(this.jsonProvider);
    }
    providers.add(new RestServiceExceptionMapper(getErrorFactory(), context));
    return providers;
  }

}
