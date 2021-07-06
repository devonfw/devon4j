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
import com.devonfw.module.service.common.base.client.async.AbstractAsyncServiceClient;

/**
 * Abstract base implementation of {@link AsyncServiceClient} using Java HTTP client.
 *
 * @param <S> type of the {@link #get() service client}.
 * @since 2020.08.001
 */
public abstract class AbstractAsyncServiceHttpClient<S> extends AbstractAsyncServiceClient<S> {

  /** {@link ServiceClientHttpAdapter} to use. */
  protected final ServiceClientHttpAdapter adapter;

  /** The discovered and resolved base URL of the service to invoke. */
  protected final String baseUrl;

  /**
   * The constructor.
   *
   * @param proxy the {@link #get() service client}.
   * @param stub the {@link ServiceClientStub}.
   * @param adapter the {@link ServiceClientHttpAdapter} to use.
   * @param baseUrl the discovered and resolved base URL of the service to invoke.
   */
  public AbstractAsyncServiceHttpClient(S proxy, ServiceClientStub<S> stub, ServiceClientHttpAdapter adapter,
      String baseUrl) {

    super(proxy, stub);
    this.adapter = adapter;
    this.baseUrl = baseUrl;
  }

  @Override
  protected <R> void doCall(ServiceClientInvocation<S> invocation, Consumer<R> resultHandler) {

    long startTime = System.nanoTime();
    HttpRequest request = this.adapter.createRequest(invocation, this.baseUrl);
    HttpClient httpClient = this.adapter.getHttpClient();
    CompletableFuture<HttpResponse<String>> future = httpClient.sendAsync(request, BodyHandlers.ofString());
    future.thenAccept(
        response -> this.adapter.handleResponse(response, startTime, invocation, resultHandler, getErrorHandler()));
  }

  @Override
  protected <R> CompletableFuture<R> doCall(ServiceClientInvocation<S> invocation) {

    long startTime = System.nanoTime();
    HttpRequest request = this.adapter.createRequest(invocation, this.baseUrl);
    HttpClient httpClient = this.adapter.getHttpClient();
    CompletableFuture<HttpResponse<String>> future = httpClient.sendAsync(request, BodyHandlers.ofString());
    return future.thenApplyAsync(response -> this.adapter.handleResponse(response, startTime, invocation));
  }

}
