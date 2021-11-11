package com.devonfw.module.httpclient.common.impl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devonfw.module.service.common.api.client.ServiceClientErrorFactory;
import com.devonfw.module.service.common.api.client.async.ServiceClientInvocation;
import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.base.client.ServiceClientPerformanceLogger;

/**
 * Abstract base implementation of {@link ServiceClientHttpAdapter}.
 *
 * @since 2021.04.003
 */
public abstract class AbstractServiceClientHttpAdapter implements ServiceClientHttpAdapter {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractServiceClientHttpAdapter.class);

  private HttpClient httpClient;

  private ClassLoader classLoader;

  private ServiceClientErrorFactory errorFactory;

  @Override
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

  @Override
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
   * @return the {@link ServiceClientErrorFactory}.
   */
  public ServiceClientErrorFactory getErrorFactory() {

    return this.errorFactory;
  }

  /**
   * @param errorFactory new value of {@link #getErrorFactory() errorFactory} to {@link Inject}.
   */
  @Inject
  public void setErrorFactory(ServiceClientErrorFactory errorFactory) {

    this.errorFactory = errorFactory;
  }

  /**
   * @param url the final URL to invoke.
   * @param invocation the {@link ServiceClientInvocation}.
   * @param httpMethod the HTTP method to call ("GET", "POST", "DELETE", etc.).
   * @param body the payload to send as body of the {@link HttpRequest} (e.g. JSON data for POST method).
   * @return the {@link java.net.http.HttpClient.Builder} for the {@link HttpRequest}.
   */
  public Builder requestBuilder(String url, ServiceClientInvocation<?> invocation, String httpMethod, String body) {

    BodyPublisher bodyPublisher = null;
    if (body != null) {
      bodyPublisher = BodyPublishers.ofString(body);
    }
    return requestBuilder(url, invocation, httpMethod, bodyPublisher);
  }

  /**
   * @param url the final URL to invoke.
   * @param invocation the {@link ServiceClientInvocation}.
   * @param httpMethod the HTTP method to call ("GET", "POST", "DELETE", etc.).
   * @param body the {@link BodyPublisher} to send as body of the {@link HttpRequest}. May be {@code null} for empty
   *        body.
   * @return the {@link java.net.http.HttpClient.Builder} for the {@link HttpRequest}.
   */
  public Builder requestBuilder(String url, ServiceClientInvocation<?> invocation, String httpMethod,
      BodyPublisher body) {

    LOG.debug("Preparing HTTP {}-request for URL {}", httpMethod, url);
    Builder builder = HttpRequest.newBuilder(URI.create(url));
    ServiceContext<?> context = invocation.getContext();
    for (String header : context.getHeaderNames()) {
      builder = builder.header(header, context.getHeader(header));
    }
    if (body == null) {
      body = BodyPublishers.noBody();
    }
    builder = builder.method(httpMethod, body);
    return builder;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <R> R handleResponse(HttpResponse<?> response, long startTime, ServiceClientInvocation<?> invocation,
      Consumer<R> resultHandler, Consumer<Throwable> errorHandler) {

    Throwable error = null;
    String serviceDescription = invocation.getServiceDescription(response.uri().toString());
    try {
      int statusCode = response.statusCode();
      if (statusCode >= 400) {
        error = createError(response, invocation, serviceDescription);
        errorHandler.accept(error);
      } else {
        LOG.debug("Received successful HTTP response for requrest {}", serviceDescription);
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
      ServiceClientPerformanceLogger.log(startTime, serviceDescription, response.statusCode(), error);
    }
    return null;
  }

  /**
   * @param response the received {@link HttpResponse}.
   * @param invocation the {@link ServiceClientInvocation}.
   * @return the unmarshalled result object from the response body or {@code null} if no body was found or return type
   *         is {@code void}.
   * @throws IllegalStateException if the unmarshalling of the result failed.
   * @throws UnsupportedOperationException if the body type is not supported.
   */
  protected abstract Object createResult(HttpResponse<?> response, ServiceClientInvocation<?> invocation);

  /**
   * @param response the {@link HttpResponse}.
   * @param invocation the {@link ServiceClientInvocation} that failed.
   * @param serviceDescription the description (URI, etc.) of the service that failed.
   * @return the error as {@link Throwable} unmarshalled from the {@link HttpResponse}.
   */
  protected Throwable createError(HttpResponse<?> response, ServiceClientInvocation<?> invocation,
      String serviceDescription) {

    int statusCode = response.statusCode();
    LOG.debug("Received HTTP failure {} for requrest {}", statusCode, serviceDescription);
    String contentType = response.headers().firstValue("Content-Type").orElse("application/json");
    String data = "";
    Object body = response.body();
    if (body instanceof String) {
      data = (String) body;
    } else {
      return createErrorUnsupportedBody(body);
    }
    return getErrorFactory().unmarshall(data, contentType, statusCode, serviceDescription);
  }

  /**
   * @param body the body of the HTTP request/response.
   * @return the {@link RuntimeException}.
   */
  protected RuntimeException createErrorUnsupportedBody(Object body) {

    String bodyType = "null";
    if (body != null) {
      body.getClass().getName(); // avoid OWASP sensitive data exposure and only reveal classname in message
    }
    return new UnsupportedOperationException(
        "HTTP request/response body of type " + bodyType + " is currently not supported!");
  }

  /**
   * @param expected the expected content-type (mime-type).
   * @param actual the actual content-type (mime-type).
   */
  protected void checkContentType(String expected, String actual) {

    if (!Objects.equals(actual, actual)) {
      LOG.warn(
          "Content-type {} is not matching the expected content-type {}. We will invoke the service but if that fails this seems to be the problem.",
          actual, expected);
    }
  }

}
