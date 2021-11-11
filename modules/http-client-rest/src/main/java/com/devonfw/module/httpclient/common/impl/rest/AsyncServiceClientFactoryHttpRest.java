package com.devonfw.module.httpclient.common.impl.rest;

import javax.inject.Inject;

import com.devonfw.module.service.common.api.client.AsyncServiceClient;
import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.api.client.sync.SyncServiceClientFactory;
import com.devonfw.module.service.common.base.client.AbstractAsyncServiceClientFactoryRest;

/**
 * Implementation of {@link SyncServiceClientFactory} for JAX-RS REST service clients using Apache CXF.
 *
 * @since 2020.08.001
 */
public class AsyncServiceClientFactoryHttpRest extends AbstractAsyncServiceClientFactoryRest {

  private ServiceClientHttpAdapterRest adapter;

  /**
   * The constructor.
   */
  public AsyncServiceClientFactoryHttpRest() {

    super();
  }

  /**
   * @return the {@link ServiceClientHttpAdapterRest}.
   */
  public ServiceClientHttpAdapterRest getAdapter() {

    return this.adapter;
  }

  /**
   * @param adapter new value of {@link #getAdapter()}.
   */
  @Inject
  public void setAdapter(ServiceClientHttpAdapterRest adapter) {

    this.adapter = adapter;
  }

  @Override
  protected <S> AsyncServiceClient<S> createService(ServiceContext<S> context, String baseUrl) {

    return AsyncServiceHttpClientRest.of(context, this.adapter, baseUrl);
  }

}
