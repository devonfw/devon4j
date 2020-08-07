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
   * @param index the index of the requested {@link #getParameters() parameter}.
   * @return the {@link #getParameters() parameter} at the given {@code index}.
   */
  default Object getParameter(int index) {

    Object[] params = getParameters();
    if ((index < 0) || (index >= params.length)) {
      throw new IndexOutOfBoundsException("Parameter index " + index + " is out of range for parameters of length "
          + params.length + " in method " + getMethod());
    }
    return params[index];
  }

  /**
   * @return the {@link ServiceContext}.
   */
  ServiceContext<S> getContext();

  /**
   * @return a {@link String} describing the invoked {@link com.devonfw.module.service.common.api.Service} and
   *         {@link #getMethod() method/operation}.
   */
  default String getServiceDescription() {

    return getContext().getApi().getName() + "#" + getMethod().getName();
  }

  /**
   * @return a {@link String} describing the invoked {@link com.devonfw.module.service.common.api.Service} and
   *         {@link #getMethod() method/operation} including the base URL.
   */
  default String getServiceDescriptionWithUrl() {

    ServiceContext<S> context = getContext();
    return context.getApi().getName() + "#" + getMethod().getName() + "[" + context.getUrl() + "]";
  }

}
