package com.devonfw.module.httpclient.common.impl.rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.temporal.Temporal;

import javax.ws.rs.core.MediaType;

import com.devonfw.module.httpclient.common.impl.AbstractSyncServiceHttpClient;
import com.devonfw.module.httpclient.common.impl.ServiceHttpClient;
import com.devonfw.module.service.common.api.client.SyncServiceClient;
import com.devonfw.module.service.common.api.client.async.ServiceClientInvocation;
import com.devonfw.module.service.common.api.client.async.ServiceClientStub;
import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.base.client.async.ServiceClientStubImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implementation of {@link SyncServiceClient} for REST (JAX-RS) using Java HTTP client.
 *
 * @param <S> type of the {@link #get() service client}.
 * @since 2021.08.003
 */
public class SyncServiceHttpClientRest<S> extends AbstractSyncServiceHttpClient<S, SyncServiceClientFactoryHttpRest> {

  private final RestServiceMetadata<S> serviceMetadata;

  /**
   * The constructor.
   *
   * @param proxy the {@link #get() service client}.
   * @param stub the {@link ServiceClientStub}.
   * @param client the {@link ServiceHttpClient} to use.
   * @param factory the owning {@link SyncServiceClientFactoryHttpRest}.
   */
  public SyncServiceHttpClientRest(S proxy, ServiceClientStub<S> stub, ServiceHttpClient client,
      SyncServiceClientFactoryHttpRest factory) {

    super(proxy, stub, client, factory);
    this.serviceMetadata = factory.getServiceMetadata(stub.getContext());
  }

  @Override
  protected Object createResult(HttpResponse<?> response, ServiceClientInvocation<S> invocation) {

    Object body = response.body();
    if (body == null) {
      return null;
    }
    Class<?> returnType = invocation.getMethod().getReturnType();
    if ((returnType == void.class) || (returnType == Void.class)) {
      return null;
    }
    if (body instanceof String) {
      ObjectMapper objectMapper = this.factory.getObjectMapper();
      if (CharSequence.class.isAssignableFrom(returnType)) {
        return body;
      }
      try {
        Object result = objectMapper.readValue((String) body, returnType);
        return result;
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    } else {
      return handleUnsupportedBody(body);
    }
  }

  @Override
  protected HttpRequest createRequest(ServiceClientInvocation<S> invocation) {

    RestMethodMetadata method = this.serviceMetadata.getMethod(invocation.getMethod());
    String url = method.getPath().resolve(this.client.getBaseUrl(), invocation);
    String contentType = null;
    BodyPublisher body = null;
    RestParameter bodyParameter = method.getParameters().getBodyParameter();
    if (bodyParameter != null) {
      Object value = invocation.getParameter(bodyParameter.index);
      if (value != null) {
        if ((value instanceof CharSequence) || (value instanceof Number) || (value instanceof Temporal)) {
          body = BodyPublishers.ofString(value.toString());
          // contentType = MediaType.TEXT_PLAIN;
        } else if (value instanceof File) {
          body = createBody(((File) value).toPath());
          contentType = MediaType.APPLICATION_OCTET_STREAM;
        } else if (value instanceof Path) {
          body = createBody((Path) value);
          contentType = MediaType.APPLICATION_OCTET_STREAM;
        } else {
          ObjectMapper objectMapper = this.factory.getObjectMapper();
          try {
            String json = objectMapper.writeValueAsString(value);
            body = BodyPublishers.ofString(json);
            contentType = MediaType.APPLICATION_JSON;
          } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
          }
        }
      }
    }
    Builder requestBuilder = this.client.requestBuilder(url, invocation, method.getHttpMethod(), body);
    if (contentType != null) {
      requestBuilder.header("Content-Type", contentType);
    }
    return requestBuilder.build();
  }

  private BodyPublisher createBody(Path path) {

    try {
      return BodyPublishers.ofFile(path);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * @param <S> type of the {@link #get() service client}.
   * @param context the {@link ServiceContext}.
   * @param client the {@link HttpClient} to use.
   * @param factory the {@link SyncServiceClientFactoryHttpRest}.
   * @return the new {@link SyncServiceHttpClientRest}.
   */
  public static <S> SyncServiceHttpClientRest<S> of(ServiceContext<S> context, ServiceHttpClient client,
      SyncServiceClientFactoryHttpRest factory) {

    ServiceClientStub<S> stub = ServiceClientStubImpl.of(context, factory.getClassLoader());
    return new SyncServiceHttpClientRest<>(stub.getProxy(), stub, client, factory);
  }

}
