
package com.devonfw.module.httpclient.common.impl.rest;

import javax.inject.Inject;

import com.devonfw.module.httpclient.common.impl.ServiceClientHttpAdapter;
import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.api.client.sync.SyncServiceClientFactory;
import com.devonfw.module.service.common.base.client.AbstractSyncServiceClientFactoryRest;

/**
 * Implementation of {@link SyncServiceClientFactory} for JAX-RS REST service clients using HTTP.
 *
 * @since 2021.04.003
 */
public class SyncServiceClientFactoryHttpRest extends AbstractSyncServiceClientFactoryRest {

  private ServiceClientHttpAdapter adapter;

  /**
   * The constructor.
   */
  public SyncServiceClientFactoryHttpRest() {

    super();
  }

  /**
   * @return the {@link ServiceClientHttpAdapter}.
   */
  public ServiceClientHttpAdapter getAdapter() {

    return this.adapter;
  }

  /**
   * @param adapter new value of {@link #getAdapter()}.
   */
  @Inject
  public void setAdapter(ServiceClientHttpAdapter adapter) {

    this.adapter = adapter;
  }

  @Override
  protected <S> S createService(ServiceContext<S> context, String url) {

    return SyncServiceClientStub.of(context, this.adapter, url).get();
  }

}
