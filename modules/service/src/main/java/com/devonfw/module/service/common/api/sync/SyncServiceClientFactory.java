package com.devonfw.module.service.common.api.sync;

import com.devonfw.module.service.common.api.Service;
import com.devonfw.module.service.common.api.client.context.ServiceContext;

/**
 * The interface for a partial implementation of {@link com.devonfw.module.service.common.api.client.ServiceClientFactory} used to
 * {@link #create(ServiceContext) create} client stubs for a {@link Service}.
 *
 * @see com.devonfw.module.service.common.api.client.ServiceClientFactory
 *
 * @since 3.0.0
 */
public interface SyncServiceClientFactory {

  /**
   * @see com.devonfw.module.service.common.api.client.ServiceClientFactory#create(Class)
   *
   * @param <S> the generic type of the {@code serviceInterface}. For flexibility and being not invasive this generic is
   *        not bound to {@link Service} ({@code S extends Service}).
   * @param context the {@link ServiceContext}.
   * @return a new instance of the given {@code serviceInterface} that is a client stub. May be {@code null} if this
   *         implementation does not handle services for the given {@link ServiceContext}.
   */
  <S> S create(ServiceContext<S> context);

}
