package com.devonfw.module.httpclient.common.impl;

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
 * Abstract base implementation of {@link AsyncServiceClient} using Java HTTP client.
 *
 * @param <S> type of the {@link #get() service client}.
 * @param <F> type of the owning {@link AsyncServiceClientFactoryHttp factory}.
 * @since 2020.08.001
 */
public abstract class AbstractAsyncServiceHttpClient<S, F extends AsyncServiceClientFactoryHttp>
    extends AbstractAsyncServiceClient<S> {

  /** {@link ServiceHttpClient} to use. */
  protected final ServiceHttpClient client;

  /** The owning {@link AsyncServiceClientFactoryHttp factory} which created this client. */
  protected final F factory;

  /**
   * The constructor.
   *
   * @param proxy the {@link #get() service client}.
   * @param stub the {@link ServiceClientStub}.
   * @param httpClient the {@link ServiceHttpClient} to use.
   * @param factory the owning {@link AsyncServiceClientFactoryHttp factory}.
   */
  public AbstractAsyncServiceHttpClient(S proxy, ServiceClientStub<S> stub, ServiceHttpClient httpClient, F factory) {

    super(proxy, stub);
    this.client = httpClient;
    this.factory = factory;
  }

  @Override
  protected <R> void doCall(ServiceClientInvocation<S> invocation, Consumer<R> resultHandler) {

    long startTime = System.nanoTime();
    HttpRequest request = createRequest(invocation);
    CompletableFuture<HttpResponse<String>> future = this.client.getHttpClient().sendAsync(request,
        BodyHandlers.ofString());
    future.thenAccept(response -> handleResponse(response, startTime, invocation, resultHandler, getErrorHandler()));
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

  @Override
  protected <R> CompletableFuture<R> doCall(ServiceClientInvocation<S> invocation) {

    long startTime = System.nanoTime();
    HttpRequest request = createRequest(invocation);
    CompletableFuture<HttpResponse<String>> future = this.client.getHttpClient().sendAsync(request,
        BodyHandlers.ofString());
    return future.thenApplyAsync(
        response -> handleResponse(response, startTime, invocation, null, ErrorHandlerThrowImmediately.get()));
  }

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
