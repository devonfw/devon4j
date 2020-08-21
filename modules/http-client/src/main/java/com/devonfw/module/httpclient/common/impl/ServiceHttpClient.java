package com.devonfw.module.httpclient.common.impl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;

import com.devonfw.module.service.common.api.client.async.ServiceClientInvocation;
import com.devonfw.module.service.common.api.client.context.ServiceContext;

/**
 * Wrapper of {@link HttpClient} to perform {@link ServiceClientInvocation}s.
 *
 * @since 2020.08.001
 */
public final class ServiceHttpClient {

  private final HttpClient httpClient;

  private final String baseUrl;

  /**
   * The constructor.
   *
   * @param httpClient the {@link #getHttpClient() HTTP client} to use.
   * @param baseUrl the {@link #getBaseUrl() base URL}.
   */
  public ServiceHttpClient(HttpClient httpClient, String baseUrl) {

    super();
    this.httpClient = httpClient;
    this.baseUrl = baseUrl;
  }

  /**
   * @return the base URL of the service to invoke (e.g. "https://api.customer.com/my-app/services/rest").
   */
  public String getBaseUrl() {

    return this.baseUrl;
  }

  /**
   * @return the {@link HttpClient}.
   */
  public HttpClient getHttpClient() {

    return this.httpClient;
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

}
