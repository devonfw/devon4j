package com.devonfw.module.service.common.base.client.async;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.devonfw.module.service.common.api.client.async.ServiceClientInvocation;
import com.devonfw.module.service.common.api.client.async.ServiceClientStub;
import com.devonfw.module.service.common.api.client.context.ServiceContext;

/**
 * Implementation of {@link ServiceClientStub} and {@link InvocationHandler} that acts as dynamic proxy for service API
 * to capture {@link ServiceClientInvocation}s.
 *
 * @param <S> type of the {@link ServiceContext#getApi() service API}.
 * @since 2020.08.001
 */
public class ServiceClientStubImpl<S> implements ServiceClientStub<S>, InvocationHandler {

  private final ServiceContext<S> context;

  private S proxy;

  private ServiceClientInvocation<S> invocation;

  /**
   * The constructor.
   *
   * @param context the {@link ServiceContext}.
   */
  protected ServiceClientStubImpl(ServiceContext<S> context) {

    super();
    this.context = context;
  }

  @Override
  public S getProxy() {

    assert (this.proxy != null);
    return this.proxy;
  }

  @Override
  public ServiceClientInvocation<S> getInvocation() {

    ServiceClientInvocation<S> result = this.invocation;
    this.invocation = null;
    assert (result != null);
    return result;
  }

  @Override
  public Object invoke(Object instance, Method method, Object[] args) throws Throwable {

    Class<?> declaringClass = method.getDeclaringClass();
    if (declaringClass == Object.class) {
      switch (method.getName()) {
        case "toString":
          return "ServiceClient for " + this.context.getApi().getName();
        case "getClass":
          return this.context.getApi();
        case "hashCode":
          return Integer.valueOf(hashCode());
        case "equals":
          if ((args != null) && (args.length == 1) && (args[0] == instance)) {
            return Boolean.TRUE;
          }
          return Boolean.FALSE;
      }
      return null;
    }
    if (this.invocation != null) {
      throw new IllegalStateException("Previous invocation has not been send!");
    }
    this.invocation = new ServiceClientInvocationImpl<>(method, args, this.context);
    return returnDummy(method.getReturnType());
  }

  private Object returnDummy(Class<?> returnType) {

    if (returnType.isPrimitive()) {
      if (returnType == int.class) {
        return Integer.valueOf(0);
      } else if (returnType == long.class) {
        return Long.valueOf(0);
      } else if (returnType == boolean.class) {
        return Boolean.FALSE;
      } else if (returnType == double.class) {
        return Double.valueOf(0);
      } else if (returnType == float.class) {
        return Float.valueOf(0);
      } else if (returnType == short.class) {
        return Short.valueOf((short) 0);
      } else if (returnType == byte.class) {
        return Byte.valueOf((byte) 0);
      } else if (returnType == char.class) {
        return Character.valueOf('\0');
      }
    }
    return null;
  }

  /**
   * @param <S> type of the {@link ServiceContext#getApi() service API}.
   * @param context the {@link ServiceContext}.
   * @return the {@link ServiceClientStub}.
   */
  public static <S> ServiceClientStub<S> of(ServiceContext<S> context) {

    return of(context, Thread.currentThread().getContextClassLoader());
  }

  /**
   * @param <S> type of the {@link ServiceContext#getApi() service API}.
   * @param context the {@link ServiceContext}.
   * @param loader the {@link ClassLoader} to use.
   * @return the {@link ServiceClientStub}.
   */
  @SuppressWarnings("unchecked")
  public static <S> ServiceClientStub<S> of(ServiceContext<S> context, ClassLoader loader) {

    ServiceClientStubImpl<S> stub = new ServiceClientStubImpl<>(context);
    Class<?>[] interfaces = new Class<?>[] { context.getApi() };
    stub.proxy = (S) Proxy.newProxyInstance(loader, interfaces, stub);
    return stub;
  }

}
