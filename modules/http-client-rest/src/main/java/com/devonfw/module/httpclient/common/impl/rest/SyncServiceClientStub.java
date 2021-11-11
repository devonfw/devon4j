package com.devonfw.module.httpclient.common.impl.rest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.devonfw.module.httpclient.common.impl.ServiceClientHttpAdapter;
import com.devonfw.module.service.common.api.client.async.ServiceClientInvocation;
import com.devonfw.module.service.common.api.client.async.ServiceClientStub;
import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.base.client.async.ServiceClientInvocationImpl;

/**
 * Implementation of {@link ServiceClientStub} and {@link InvocationHandler} that acts as dynamic proxy for service API
 * to capture {@link ServiceClientInvocation}s.
 *
 * @param <S> type of the {@link ServiceContext#getApi() service API}.
 * @since 2021.04.003
 */
public class SyncServiceClientStub<S> implements InvocationHandler {

  private final ServiceContext<S> context;

  private final ServiceClientHttpAdapter adapter;

  private final String baseUrl;

  private S proxy;

  /**
   * The constructor.
   *
   * @param context the {@link ServiceContext}.
   * @param adapter the {@link ServiceClientHttpAdapter}.
   * @param baseUrl the discovered and resolved base URL of the service to invoke.
   */
  protected SyncServiceClientStub(ServiceContext<S> context, ServiceClientHttpAdapter adapter, String baseUrl) {

    super();
    this.context = context;
    this.adapter = adapter;
    this.baseUrl = baseUrl;
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
    long startTime = System.nanoTime();
    ServiceClientInvocation<S> invocation = new ServiceClientInvocationImpl<>(method, args, this.context);
    HttpRequest request = this.adapter.createRequest(invocation, this.baseUrl);
    HttpResponse<String> response = this.adapter.getHttpClient().send(request, BodyHandlers.ofString());
    Object result = this.adapter.handleResponse(response, startTime, invocation);
    return result;

  }

  /**
   * @return the dynamic proxy.
   */
  public S get() {

    return this.proxy;
  }

  /**
   * @param <S> type of the {@link ServiceContext#getApi() service API}.
   * @param context the {@link ServiceContext}.
   * @param adapter the {@link ServiceClientHttpAdapter}.
   * @param baseUrl the discovered and resolved base URL of the service to invoke.
   * @return the {@link SyncServiceClientStub}.
   */
  @SuppressWarnings("unchecked")
  public static <S> SyncServiceClientStub<S> of(ServiceContext<S> context, ServiceClientHttpAdapter adapter,
      String baseUrl) {

    SyncServiceClientStub<S> stub = new SyncServiceClientStub<>(context, adapter, baseUrl);
    Class<?>[] interfaces = new Class<?>[] { context.getApi() };
    stub.proxy = (S) Proxy.newProxyInstance(adapter.getClassLoader(), interfaces, stub);
    return stub;
  }

}
