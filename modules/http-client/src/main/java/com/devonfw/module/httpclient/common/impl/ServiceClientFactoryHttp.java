package com.devonfw.module.httpclient.common.impl;

import java.net.http.HttpClient;

import com.devonfw.module.service.common.api.client.async.AsyncServiceClientFactory;
import com.devonfw.module.service.common.base.client.PartialServiceClientFactory;

/**
 * Abstract base implementation of {@link AsyncServiceClientFactory} for service clients using Java HTTP client.
 *
 * @since 2021.04.003
 */
public abstract class ServiceClientFactoryHttp extends PartialServiceClientFactory {

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
