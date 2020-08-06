package com.devonfw.module.httpclient.common.impl;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import com.devonfw.module.service.common.api.client.AsyncServiceClient;
import com.devonfw.module.service.common.api.client.async.ServiceClientInvocation;
import com.devonfw.module.service.common.api.client.async.ServiceClientStub;
import com.devonfw.module.service.common.base.client.ServiceClientPerformanceLogger;
import com.devonfw.module.service.common.base.client.async.AbstractAsyncServiceClient;

/**
 * Implementation of {@link AsyncServiceClient} using Java HTTP client.
 *
 * @param <S> type of the {@link #get() service client}.
 * @since 2020.08.001
 */
public abstract class AbstractAsyncServiceHttpClient<S> extends AbstractAsyncServiceClient<S> {

  private final HttpClient httpClient;

  /**
   * The constructor.
   *
   * @param proxy the {@link #get() service client}.
   * @param stub the {@link ServiceClientStub}.
   * @param httpClient the {@link HttpClient} to use.
   */
  public AbstractAsyncServiceHttpClient(S proxy, ServiceClientStub<S> stub, HttpClient httpClient) {

    super(proxy, stub);
    this.httpClient = httpClient;
  }

  @Override
  protected <R> void doCall(ServiceClientInvocation<S> invocation, Consumer<R> resultHandler) {

    long startTime = System.nanoTime();
    HttpRequest request = createRequest(invocation);
    // TODO Auto-generated method stub
    CompletableFuture<HttpResponse<String>> future = this.httpClient.sendAsync(request, BodyHandlers.ofString());
    future.thenAccept(response -> handleResponse(response, startTime, invocation, resultHandler));
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private void handleResponse(HttpResponse<?> response, long startTime, ServiceClientInvocation<S> invocation,
      Consumer resultHandler) {

    Throwable error = null;
    try {
      Object result = createResult(response);
      resultHandler.accept(result);
    } catch (Throwable t) {
      this.errorHandler.accept(t);
      error = t;
    } finally {
      Object service = invocation.getContext().getApi().getName();
      String target = invocation.getContext().getUrl();
      ServiceClientPerformanceLogger.log(startTime, service, target, response.statusCode(), error);
    }
  }

  /**
   * @param response the received {@link HttpResponse}.
   * @return the unmarshalled result object.
   */
  protected abstract Object createResult(HttpResponse<?> response);

  /**
   * @param invocation the {@link ServiceClientInvocation}.
   * @return the according {@link HttpResponse} to send.
   */
  protected abstract HttpRequest createRequest(ServiceClientInvocation<S> invocation);

}
