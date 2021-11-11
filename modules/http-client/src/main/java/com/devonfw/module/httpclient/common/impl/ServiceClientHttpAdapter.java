package com.devonfw.module.httpclient.common.impl;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;

import com.devonfw.module.service.common.api.client.async.ServiceClientInvocation;

/**
 * Adapter for specific service protocol and implementation such as REST or SOAP.
 *
 * @since 2021.04.003
 */
public interface ServiceClientHttpAdapter {

  /**
   * @return the {@link HttpClient} to use. Will be created lazily.
   */
  HttpClient getHttpClient();

  /**
   * @param invocation the {@link ServiceClientInvocation}.
   * @param baseUrl the {@link com.devonfw.module.service.common.api.client.context.ServiceContext#getUrl() base URL} of
   *        the service to invoke.
   * @return the according {@link HttpResponse} to send.
   */
  HttpRequest createRequest(ServiceClientInvocation<?> invocation, String baseUrl);

  /**
   * @param <R> type of the response data.
   * @param response the {@link HttpResponse} that has been received.
   * @param startTime the start time as {@link System#nanoTime()}.
   * @param invocation the initial {@link ServiceClientInvocation}.
   * @return the result data or {@code null} on error or asynchronous usage.
   */
  default <R> R handleResponse(HttpResponse<?> response, long startTime, ServiceClientInvocation<?> invocation) {

    return handleResponse(response, startTime, invocation, null);
  }

  /**
   * @param <R> type of the response data.
   * @param response the {@link HttpResponse} that has been received.
   * @param startTime the start time as {@link System#nanoTime()}.
   * @param invocation the initial {@link ServiceClientInvocation}.
   * @param resultHandler the {@link Consumer} to handle the result asynchronous or {@code null} get from the returned
   *        result.
   * @return the result data or {@code null} on error or asynchronous usage.
   */
  default <R> R handleResponse(HttpResponse<?> response, long startTime, ServiceClientInvocation<?> invocation,
      Consumer<R> resultHandler) {

    return handleResponse(response, startTime, invocation, resultHandler, ErrorHandlerThrowImmediately.get());
  }

  /**
   * @param <R> type of the response data.
   * @param response the {@link HttpResponse} that has been received.
   * @param startTime the start time as {@link System#nanoTime()}.
   * @param invocation the initial {@link ServiceClientInvocation}.
   * @param resultHandler the {@link Consumer} to handle the result asynchronous or {@code null} get from the returned
   *        result.
   * @param errorHandler the {@link Consumer} to handle a potential error.
   * @return the result data or {@code null} on error or asynchronous usage.
   */
  <R> R handleResponse(HttpResponse<?> response, long startTime, ServiceClientInvocation<?> invocation,
      Consumer<R> resultHandler, Consumer<Throwable> errorHandler);

  /**
   * @return the {@link ClassLoader} to use.
   */
  ClassLoader getClassLoader();

}
