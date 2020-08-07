package com.devonfw.module.httpclient.common.impl.rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.temporal.Temporal;

import com.devonfw.module.httpclient.common.impl.AbstractAsyncServiceHttpClient;
import com.devonfw.module.httpclient.common.impl.ServiceHttpClient;
import com.devonfw.module.service.common.api.client.AsyncServiceClient;
import com.devonfw.module.service.common.api.client.async.ServiceClientInvocation;
import com.devonfw.module.service.common.api.client.async.ServiceClientStub;
import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.base.client.async.ServiceClientStubImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implementation of {@link AsyncServiceClient} for REST (JAX-RS) using Java HTTP client.
 *
 * @param <S> type of the {@link #get() service client}.
 * @since 2020.08.001
 */
public class AsyncServiceHttpClientRest<S>
    extends AbstractAsyncServiceHttpClient<S, AsyncServiceClientFactoryHttpRest> {

  private final RestServiceMetadata<S> serviceMetadata;

  /**
   * The constructor.
   *
   * @param proxy the {@link #get() service client}.
   * @param stub the {@link ServiceClientStub}.
   * @param client the {@link ServiceHttpClient} to use.
   * @param factory the owning {@link AsyncServiceClientFactoryHttpRest}.
   */
  public AsyncServiceHttpClientRest(S proxy, ServiceClientStub<S> stub, ServiceHttpClient client,
      AsyncServiceClientFactoryHttpRest factory) {

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
      throw new UnsupportedOperationException("TODO");
    }
  }

  @Override
  protected HttpRequest createRequest(ServiceClientInvocation<S> invocation) {

    RestMethodMetadata method = this.serviceMetadata.getMethod(invocation.getMethod());
    String url = method.getPath().resolve(this.client.getBaseUrl(), invocation);
    BodyPublisher body = null;
    RestParameter bodyParameter = method.getParameters().getBodyParameter();
    if (bodyParameter != null) {
      Object bodyValue = invocation.getParameter(bodyParameter.index);
      if (bodyValue != null) {
        body = createBody(bodyValue);
      }
    }
    return this.client.requestBuilder(url, invocation, method.getHttpMethod(), body).build();
  }

  private BodyPublisher createBody(Object value) {

    if ((value instanceof CharSequence) || (value instanceof Number) || (value instanceof Temporal)) {
      return BodyPublishers.ofString(value.toString());
    } else if (value instanceof File) {
      return createBody(((File) value).toPath());
    } else if (value instanceof Path) {
      try {
        return BodyPublishers.ofFile((Path) value);
      } catch (FileNotFoundException e) {
        throw new IllegalArgumentException(e);
      }
    } else {
      ObjectMapper objectMapper = this.factory.getObjectMapper();
      try {
        String json = objectMapper.writeValueAsString(value);
        return BodyPublishers.ofString(json);
      } catch (JsonProcessingException e) {
        throw new IllegalStateException(e);
      }
    }
  }

  /**
   * @param <S> type of the {@link #get() service client}.
   * @param context the {@link ServiceContext}.
   * @param client the {@link HttpClient} to use.
   * @param factory the {@link AsyncServiceClientFactoryHttpRest}.
   * @return the new {@link AsyncServiceHttpClientRest}.
   */
  public static <S> AsyncServiceHttpClientRest<S> of(ServiceContext<S> context, ServiceHttpClient client,
      AsyncServiceClientFactoryHttpRest factory) {

    ServiceClientStub<S> stub = ServiceClientStubImpl.of(context, factory.getClassLoader());
    return new AsyncServiceHttpClientRest<>(stub.getProxy(), stub, client, factory);
  }

}
