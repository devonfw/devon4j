package com.devonfw.module.cxf.common.impl.client.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;

import com.devonfw.module.cxf.common.impl.client.SyncServiceClientFactoryCxf;
import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.api.constants.ServiceConstants;
import com.devonfw.module.service.common.api.sync.SyncServiceClientFactory;
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

  /**
   * @param jsonProvider the {@link JacksonJsonProvider} to {@link Inject}.
   */
  @Inject
  public void setJsonProvider(JacksonJsonProvider jsonProvider) {

    this.jsonProvider = jsonProvider;
  }

  @Override
  protected <S> void applyAspects(ServiceContext<S> context, S serviceClient, String serviceName) {

    ClientConfiguration clientConfig = WebClient.getConfig(serviceClient);
    applyInterceptors(context, clientConfig, serviceName);
    applyClientPolicy(context, clientConfig.getHttpConduit());
    applyHeaders(context, serviceClient);
  }

  @Override
  protected <S> S createService(ServiceContext<S> context, String url, String serviceName) {

    List<Object> providers = createProviderList(context, serviceName);
    return JAXRSClientFactory.create(url, context.getApi(), providers);
  }

  @Override
  protected String getServiceTypeFolderName() {

    return ServiceConstants.URL_FOLDER_REST;
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

  @Override
  protected boolean isResponsibleForService(ServiceContext<?> context) {

    return context.getApi().isAnnotationPresent(Path.class);
  }

  /**
   * @param context the {@link ServiceContext}.
   * @param serviceName the {@link #createServiceName(ServiceContext) service name}.
   * @return the {@link List} of {@link javax.ws.rs.ext.Provider}s.
   */
  protected List<Object> createProviderList(ServiceContext<?> context, String serviceName) {

    List<Object> providers = new ArrayList<>();
    if (this.jsonProvider != null) {
      providers.add(this.jsonProvider);
    }
    providers.add(new RestServiceExceptionMapper(serviceName));
    return providers;
  }

}
