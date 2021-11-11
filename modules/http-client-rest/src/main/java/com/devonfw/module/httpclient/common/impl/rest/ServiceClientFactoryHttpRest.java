package com.devonfw.module.httpclient.common.impl.rest;

import java.net.http.HttpClient;

import com.devonfw.module.httpclient.common.impl.ServiceClientHttpAdapter;
import com.devonfw.module.httpclient.common.impl.ServiceClientFactoryHttp;
import com.devonfw.module.service.common.api.client.async.AsyncServiceClientFactory;

/**
 * Abstract base implementation of {@link AsyncServiceClientFactory} for service clients using Java HTTP client.
 *
 * @since 2020.08.001
 */
public abstract class ServiceClientFactoryHttpRest extends ServiceClientFactoryHttp {

  private HttpClient httpClient;

  private ClassLoader classLoader;

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
   * @return the {@link ClassLoader} to use.
   */
  public ClassLoader getClassLoader() {

    if (this.classLoader == null) {
      this.classLoader = Thread.currentThread().getContextClassLoader();
    }
    return this.classLoader;
  }

  /**
   * @param classLoader new value of {@link #getClassLoader()}.
   */
  public void setClassLoader(ClassLoader classLoader) {

    this.classLoader = classLoader;
  }

  /**
   * @return the {@link ServiceClientHttpAdapter}.
   */
  protected abstract ServiceClientHttpAdapter getClientAdapter();

}
