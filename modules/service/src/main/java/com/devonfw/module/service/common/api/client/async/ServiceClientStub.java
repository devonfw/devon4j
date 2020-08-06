package com.devonfw.module.service.common.api.client.async;

import com.devonfw.module.service.common.api.client.context.ServiceContext;

/**
 * A {@link ServiceClientStub} is the stub of an {@link com.devonfw.module.service.common.api.client.AsyncServiceClient}
 * that records the {@link #getInvocation() invocation}.
 *
 * @param <S> type of the {@code service API}.
 * @since 2020.08.001
 */
public interface ServiceClientStub<S> {

  /**
   * @return the {@link java.lang.reflect.Proxy dynamic proxy} instance of the {@link ServiceContext#getApi() service
   *         API}.
   */
  S getProxy();

  /**
   * @return the last {@link ServiceClientInvocation} that has been recorded by this stub. This method will reset this
   *         stub and remove the invocation.
   */
  ServiceClientInvocation<S> getInvocation();

}
