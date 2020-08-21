package com.devonfw.module.httpclient.common.impl;

import java.net.http.HttpClient;

import javax.inject.Inject;

import com.devonfw.module.service.common.api.client.AsyncServiceClient;
import com.devonfw.module.service.common.api.client.ServiceClientErrorFactory;
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

  private ServiceClientErrorFactory errorUnmarshaller;

  private HttpClient httpClient;

  @Override
  public <S> AsyncServiceClient<S> create(ServiceContext<S> context) {

    boolean responsible = isResponsibleForService(context);
    if (!responsible) {
      return null;
    }
    String url = getUrl(context);
    AsyncServiceClient<S> serviceClient = createService(context, url);
    return serviceClient;
  }

  /**
   * @param <S> the generic type of the {@link ServiceContext#getApi() service API}.
   * @param context the {@link ServiceContext}.
   * @param url the resolved end-point URL of the service to invoke.
   * @return a new client stub for the service. See {@link #create(ServiceContext)} for further details.
   */
  protected abstract <S> AsyncServiceClient<S> createService(ServiceContext<S> context, String url);

  /**
   * @return the {@link HttpClient} to use. Will be created lazily.
   */
  public HttpClient getHttpClient() {

    if (this.httpClient == null) {
      this.httpClient = HttpClient.newHttpClient();
    }
    return this.httpClient;
  }

  /**
   * @param httpClient the {@link HttpClient} to use.
   */
  public void setHttpClient(HttpClient httpClient) {

    this.httpClient = httpClient;
  }

  /**
   * @return the {@link ServiceClientErrorFactory}.
   */
  public ServiceClientErrorFactory getErrorUnmarshaller() {

    return this.errorUnmarshaller;
  }

  /**
   * @param errorUnmarshaller the {@link ServiceClientErrorFactory} to {@link Inject}.
   */
  @Inject
  public void setErrorUnmarshaller(ServiceClientErrorFactory errorUnmarshaller) {

    this.errorUnmarshaller = errorUnmarshaller;
  }

}
