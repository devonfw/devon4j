package com.devonfw.module.service.common.impl.client;

import com.devonfw.module.service.common.api.client.SyncServiceClient;
import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.api.client.sync.SyncHttpServiceClientFactory;
import com.devonfw.module.service.common.api.client.sync.SyncServiceClientFactory;

/**
 * An empty implementation of {@link SyncServiceClientFactory} to make it optional. Otherwise if no provider
 * implementation is found frameworks like spring will throw an exception (No qualifying bean of type
 * 'java.util.Collection&lt;{@link SyncServiceClientFactory}&gt;' available).
 *
 * @since 2021.08.003
 */
public class EmptySyncHttpServiceClientFactory implements SyncHttpServiceClientFactory {

  @Override
  public <S> SyncServiceClient<S> create(ServiceContext<S> context) {

    return null;
  }

}
