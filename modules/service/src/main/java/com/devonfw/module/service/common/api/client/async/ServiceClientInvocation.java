package com.devonfw.module.service.common.api.client.async;

import java.lang.reflect.Method;

import com.devonfw.module.service.common.api.client.context.ServiceContext;

/**
 * A {@link ServiceClientInvocation} represents the invocation of a service operation as {@link #getMethod() method}
 * with its {@link #getParameters() parameters}.
 *
 * @param <S> type of the {@code service API}.
 * @since 2020.08.001
 */
public interface ServiceClientInvocation<S> {

  /**
   * @return the Method reflecting the service operation to invoke.
   */
  Method getMethod();

  /**
   * @return the parameters provided when the service operation {@link #getMethod() method} was invoked.
   */
  Object[] getParameters();

  /**
   * @return the {@link ServiceContext}.
   */
  ServiceContext<S> getContext();

}
