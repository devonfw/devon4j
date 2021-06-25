
package com.devonfw.module.httpclient.common.impl.rest;

import java.net.http.HttpClient;

import com.devonfw.module.httpclient.common.impl.AbstractSyncServiceHttpClient;
import com.devonfw.module.httpclient.common.impl.ServiceHttpClient;
import com.devonfw.module.service.common.api.client.SyncServiceClient;
import com.devonfw.module.service.common.api.client.async.ServiceClientStub;
import com.devonfw.module.service.common.api.client.context.ServiceContext;

/**
 * Implementation of {@link SyncServiceClient} for REST (JAX-RS) using Java HTTP client.
 *
 * @param <S> type of the {@link #get() service client}.
 * @since 2021.08.003
 */

public class SyncServiceHttpClientRest<S> extends AbstractSyncServiceHttpClient<S, SyncServiceClientFactoryHttpRest> {

  @SuppressWarnings("unused")
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

  /**/
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
