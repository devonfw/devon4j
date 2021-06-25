
package com.devonfw.module.httpclient.common.impl;

import java.net.http.HttpResponse;

import com.devonfw.module.service.common.api.client.SyncServiceClient;
import com.devonfw.module.service.common.api.client.async.ServiceClientInvocation;
import com.devonfw.module.service.common.api.client.async.ServiceClientStub;
import com.devonfw.module.service.common.base.client.AbstractSyncServiceClient;

/**
 * Abstract base implementation of {@link SyncServiceClient} using Java HTTP client.
 *
 * @param <S> type of the {@link #get() service client}.
 * @param <F> type of the owning {@link SyncServiceClientFactoryHttp factory}.
 * @since 2021.08.003
 */

public abstract class AbstractSyncServiceHttpClient<S, F extends SyncServiceClientFactoryHttp>
    extends AbstractSyncServiceClient<S> {

  /** {@link ServiceHttpClient} to use. */

  protected final ServiceHttpClient client;

  /** The owning {@link SyncServiceClientFactoryHttp factory} which created this client. */

  protected final F factory;

  /**
   * The constructor.
   *
   * @param proxy the {@link #get() service client}.
   * @param stub the {@link ServiceClientStub}.
   * @param httpClient the {@link ServiceHttpClient} to use.
   * @param factory the owning {@link SyncServiceClientFactoryHttp factory}.
   */

  public AbstractSyncServiceHttpClient(S proxy, ServiceClientStub<S> stub, ServiceHttpClient httpClient, F factory) {

    super(proxy, stub);
    this.client = httpClient;
    this.factory = factory;
  }

  @SuppressWarnings("unused")
  private Throwable createError(HttpResponse<?> response, ServiceClientInvocation<S> invocation, String service) {

    int statusCode = response.statusCode();
    String contentType = response.headers().firstValue("Content-Type").orElse("application/json");
    String data = "";
    Object body = response.body();
    if (body instanceof String) {
      data = (String) body;
    } else {
      handleUnsupportedBody(body);
    }
    return this.factory.getErrorUnmarshaller().unmarshall(data, contentType, statusCode, service);
  }

  /**
   * @param body the body of the HTTP request/response.
   * @return nothing. Will already throw an exception.
   */

  protected Object handleUnsupportedBody(Object body) {

    String bodyType = "null";
    if (body != null) {
      body.getClass().getName(); // avoid OWASP sensitive data exposure and only reveal classname in message
    }
    throw new UnsupportedOperationException(
        "HTTP request/response body of type " + bodyType + " is currently not supported!");
  }

}
