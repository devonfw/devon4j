
package com.devonfw.module.httpclient.common.impl;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.function.Consumer;

import com.devonfw.module.service.common.api.client.SyncServiceClient;
import com.devonfw.module.service.common.api.client.async.ServiceClientInvocation;
import com.devonfw.module.service.common.api.client.async.ServiceClientStub;
import com.devonfw.module.service.common.base.client.AbstractSyncServiceClient;
import com.devonfw.module.service.common.base.client.ServiceClientPerformanceLogger;

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

  @Override
  protected <R> void doCall(ServiceClientInvocation<S> invocation, Consumer<R> resultHandler)
      throws IOException, InterruptedException {

    long startTime = System.nanoTime();
    HttpRequest request = createRequest(invocation);
    HttpResponse<String> future = this.client.getHttpClient().send(request, BodyHandlers.ofString());
    handleResponse(future, startTime, invocation, resultHandler, getErrorHandler());
    future.body();
  }

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

  /**
   * @param response the received {@link HttpResponse}.
   * @param invocation the {@link ServiceClientInvocation}.
   * @return the unmarshalled result object from the response body or {@code null} if no body was found or return type
   *         is {@code void}.
   * @throws IllegalStateException if the unmarshalling of the result failed.
   * @throws UnsupportedOperationException if the body type is not supported.
   */

  protected abstract Object createResult(HttpResponse<?> response, ServiceClientInvocation<S> invocation);

  /**
   * @param invocation the {@link ServiceClientInvocation}.
   * @return the according {@link HttpResponse} to send.
   */
  protected abstract HttpRequest createRequest(ServiceClientInvocation<S> invocation);

  @SuppressWarnings({ "unchecked" })
  private <R> R handleResponse(HttpResponse<?> response, long startTime, ServiceClientInvocation<S> invocation,
      Consumer<R> resultHandler, Consumer<Throwable> errorHandler) {

    Throwable error = null;
    String service = invocation.getServiceDescription(response.uri().toString());
    try {
      int statusCode = response.statusCode();
      if (statusCode >= 400) {
        error = createError(response, invocation, service);
        errorHandler.accept(error);
      } else {
        R result = (R) createResult(response, invocation);
        if (resultHandler != null) {
          resultHandler.accept(result);
        }
        return result;
      }
    } catch (Throwable t) {
      errorHandler.accept(t);
      error = t;
    } finally {
      ServiceClientPerformanceLogger.log(startTime, service, response.statusCode(), error);
    }
    return null;
  }

}
