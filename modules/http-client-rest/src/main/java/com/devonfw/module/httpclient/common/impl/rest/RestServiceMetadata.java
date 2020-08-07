package com.devonfw.module.httpclient.common.impl.rest;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.devonfw.module.service.common.api.client.context.ServiceContext;

/**
 * Metadata of a REST service for a {@link ServiceContext#getApi() API} {@link Class}.
 *
 * @param <S> type of the {@link ServiceContext#getApi() service API}.
 * @since 2020.08.001
 */
public class RestServiceMetadata<S> extends RestMetadata {

  private final ServiceContext<S> context;

  private final Map<Method, RestMethodMetadata> methodMap;

  private final String path;

  /**
   * The constructor.
   *
   * @param context the {@link ServiceContext} to use.
   */
  public RestServiceMetadata(ServiceContext<S> context) {

    super();
    this.context = context;
    this.methodMap = new ConcurrentHashMap<>();
    Class<S> api = context.getApi();
    Path pathAnnotation = api.getAnnotation(Path.class);
    this.path = pathAnnotation.value();
    Consumes consumesAnnotation = findAnnotation(api, Consumes.class);
    if (consumesAnnotation != null) {
      requireJson(consumesAnnotation.value());
    }
    Produces producesAnnotation = findAnnotation(api, Produces.class);
    if (producesAnnotation != null) {
      requireJson(producesAnnotation.value());
    }
  }

  /**
   * @return the {@link ServiceContext}.
   */
  public ServiceContext<S> getContext() {

    return this.context;
  }

  /**
   * @return the URL path fragment from the {@link Path} annotation.
   */
  public String getPath() {

    return this.path;
  }

  /**
   * @param method the REST {@link Method} to get the metadata for.
   * @return the {@link RestMethodMetadata} for the given {@link Method}.
   */
  public RestMethodMetadata getMethod(Method method) {

    return this.methodMap.computeIfAbsent(method, this::createMethodMetadata);
  }

  private RestMethodMetadata createMethodMetadata(Method method) {

    return new RestMethodMetadata(this, method);
  }

}
