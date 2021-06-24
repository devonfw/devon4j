
package com.devonfw.module.service.common.base.client;

import java.lang.reflect.Method;

import com.devonfw.module.service.common.api.client.async.ServiceClientInvocation;
import com.devonfw.module.service.common.api.client.context.ServiceContext;

/**
 * Implementation of {@link ServiceClientInvocation}.
 *
 * @param <S> type of the {@code service API}.
 * @since 2020.08.001
 */

public class SyncServiceClientInvocationImpl<S> implements ServiceClientInvocation<S> {

  private final Method method;

  private final Object[] parameters;

  private final ServiceContext<S> context;

  /**
   * The constructor.
   *
   * @param method - see {@link #getMethod()}.
   * @param parameters - see {@link #getParameters()}.
   * @param context - see {@link #getContext()}.
   */
  public SyncServiceClientInvocationImpl(Method method, Object[] parameters, ServiceContext<S> context) {

    super();
    this.method = method;
    this.parameters = parameters;
    this.context = context;
  }

  @Override
  public Method getMethod() {

    return this.method;
  }

  @Override
  public Object[] getParameters() {

    return this.parameters;
  }

  @Override
  public ServiceContext<S> getContext() {

    return this.context;
  }

}
