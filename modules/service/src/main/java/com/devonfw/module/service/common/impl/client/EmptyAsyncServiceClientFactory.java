package com.devonfw.module.service.common.impl.client;

import com.devonfw.module.service.common.api.client.AsyncServiceClient;
import com.devonfw.module.service.common.api.client.async.AsyncServiceClientFactory;
import com.devonfw.module.service.common.api.client.context.ServiceContext;

/**
 * An empty implementation of {@link AsyncServiceClientFactory} to make it optional. Otherwise if no provider
 * implementation is found frameworks like spring will throw an exception (No qualifying bean of type
 * 'java.util.Collection&lt;{@link AsyncServiceClientFactory}&gt;' available).
 *
 * @since 2020.08.001
 */
public class EmptyAsyncServiceClientFactory implements AsyncServiceClientFactory {

  @Override
  public <S> AsyncServiceClient<S> create(ServiceContext<S> context) {

    return null;
  }

}
