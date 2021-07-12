package com.devonfw.module.httpclient.common.impl.rest;

import com.devonfw.module.httpclient.common.impl.AbstractAsyncServiceHttpClient;
import com.devonfw.module.httpclient.common.impl.ServiceClientHttpAdapter;
import com.devonfw.module.service.common.api.client.AsyncServiceClient;
import com.devonfw.module.service.common.api.client.async.ServiceClientStub;
import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.base.client.async.ServiceClientStubImpl;

/**
 * Implementation of {@link AsyncServiceClient} for REST (JAX-RS) using Java HTTP client.
 *
 * @param <S> type of the {@link #get() service client}.
 * @since 2020.08.001
 */
public class AsyncServiceHttpClientRest<S> extends AbstractAsyncServiceHttpClient<S> {

  /**
   * The constructor.
   *
   * @param proxy the {@link #get() service client}.
   * @param stub the {@link ServiceClientStub}.
   * @param adapter the {@link ServiceClientHttpAdapter} to use.
   * @param baseUrl the discovered and resolved base URL of the service to invoke.
   */
  public AsyncServiceHttpClientRest(S proxy, ServiceClientStub<S> stub, ServiceClientHttpAdapter adapter,
      String baseUrl) {

    super(proxy, stub, adapter, baseUrl);
  }

  /**
   * @param <S> type of the {@link #get() service client}.
   * @param context the {@link ServiceContext}.
   * @param adapter the {@link ServiceClientHttpAdapter} to use.
   * @param baseUrl the discovered and resolved base URL of the service to invoke.
   * @return the new {@link AsyncServiceHttpClientRest}.
   */
  public static <S> AsyncServiceHttpClientRest<S> of(ServiceContext<S> context, ServiceClientHttpAdapter adapter,
      String baseUrl) {

    ServiceClientStub<S> stub = ServiceClientStubImpl.of(context, adapter.getClassLoader());
    return new AsyncServiceHttpClientRest<>(stub.getProxy(), stub, adapter, baseUrl);
  }

}
