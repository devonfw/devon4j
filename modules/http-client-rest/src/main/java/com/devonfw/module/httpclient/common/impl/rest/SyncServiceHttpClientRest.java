
package com.devonfw.module.httpclient.common.impl.rest;

import java.io.FileNotFoundException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.file.Path;

import com.devonfw.module.httpclient.common.impl.AbstractSyncServiceHttpClient;
import com.devonfw.module.httpclient.common.impl.ServiceHttpClient;
import com.devonfw.module.service.common.api.client.SyncServiceClient;
import com.devonfw.module.service.common.api.client.async.ServiceClientInvocation;
import com.devonfw.module.service.common.api.client.async.ServiceClientStub;
import com.devonfw.module.service.common.api.client.context.ServiceContext;
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

    ServiceClientStub<S> stub = SyncServiceClientStubImpl.of(context, factory.getClassLoader(), client,
        factory.getObjectMapper(), factory.getServiceMetadata(context));
    return new SyncServiceHttpClientRest<>(stub.getProxy(), stub, client, factory);
  }

}
