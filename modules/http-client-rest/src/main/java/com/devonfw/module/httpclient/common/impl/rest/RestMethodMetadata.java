package com.devonfw.module.httpclient.common.impl.rest;

import java.lang.reflect.Method;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;

/**
 *
 * @since 2020.08.001
 */
public class RestMethodMetadata extends RestMetadata {

  private static final String METHOD_GET = "GET";

  private static final String METHOD_POST = "POST";

  private static final String METHOD_PUT = "PUT";

  private static final String METHOD_DELETE = "DELETE";

  private static final String METHOD_HEAD = "HEAD";

  private static final String METHOD_PATCH = "PATCH";

  private static final String METHOD_OPTIONS = "OPTIONS";

  private final RestServiceMetadata<?> serviceMetadata;

  private final Method javaMethod;

  private final String httpMethod;

  private final RestParameters parameters;

  private final RestPathMetadata path;

  /**
   * The constructor.
   *
   * @param serviceMetadata the owning {@link RestServiceMetadata}.
   * @param method the Java {@link Method}.
   */
  public RestMethodMetadata(RestServiceMetadata<?> serviceMetadata, Method method) {

    super();
    this.serviceMetadata = serviceMetadata;
    this.javaMethod = method;
    this.httpMethod = determineHttpMethod(method);
    this.parameters = new RestParameters(method);
    this.path = RestPathMetadata.of(serviceMetadata.getPath(), method, this.parameters);
  }

  private static String determineHttpMethod(Method method) {

    // we do not validate JAR-RS but take the first hit assuming that service API is validated on server-side
    // so in case multiple HTTP methods are annotated currently the first hit will be matched...
    if (method.isAnnotationPresent(GET.class)) {
      return METHOD_GET;
    } else if (method.isAnnotationPresent(POST.class)) {
      return METHOD_POST;
    } else if (method.isAnnotationPresent(DELETE.class)) {
      return METHOD_DELETE;
    } else if (method.isAnnotationPresent(PUT.class)) {
      return METHOD_PUT;
    } else if (method.isAnnotationPresent(HEAD.class)) {
      return METHOD_HEAD;
    } else if (method.isAnnotationPresent(PATCH.class)) {
      return METHOD_PATCH;
    } else if (method.isAnnotationPresent(OPTIONS.class)) {
      return METHOD_OPTIONS;
    }
    throw new IllegalStateException("Unknown or missing HTTP method for " + method);
  }

  /**
   * @return serviceMetadata
   */
  public RestServiceMetadata<?> getServiceMetadata() {

    return this.serviceMetadata;
  }

  /**
   * @return method
   */
  public Method getJavaMethod() {

    return this.javaMethod;
  }

  /**
   * @return the HTTP method (e.g. "GET" or "POST").
   */
  public String getHttpMethod() {

    return this.httpMethod;
  }

  /**
   * @return the {@link RestParameter}s.
   */
  public RestParameters getParameters() {

    return this.parameters;
  }

  /**
   * @return the {@link RestPathMetadata}.
   */
  public RestPathMetadata getPath() {

    return this.path;
  }

}
