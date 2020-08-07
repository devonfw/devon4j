package com.devonfw.module.httpclient.common.impl.rest;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * {@link RestParameter}s of a REST method.
 *
 * @since 2020.08.001
 */
public class RestParameters {

  final Method method;

  private Map<String, RestParameter> pathParameters;

  private List<RestParameter> queryParameters;

  private final RestParameter bodyParameter;

  /**
   * The constructor.
   *
   * @param method the JAX-RS {@link Method}.
   */
  public RestParameters(Method method) {

    super();
    this.method = method;
    RestParameter bodyParam = null;
    Parameter[] parameters = method.getParameters();
    for (int i = 0; i < parameters.length; i++) {
      Parameter parameter = parameters[i];
      PathParam pathParam = RestPathMetadata.findAnnotation(parameter, i, PathParam.class, false);
      if (pathParam != null) {
        if (this.pathParameters == null) {
          this.pathParameters = new HashMap<>();
        }
        String name = pathParam.value();
        RestParameter duplicate = this.pathParameters.put(name, new RestParameter(name, i, false));
        if (duplicate != null) {
          throw new IllegalStateException("Duplicate @PathParam " + name + " for method " + method);
        }
      } else {
        QueryParam queryParam = RestPathMetadata.findAnnotation(parameter, i, QueryParam.class, false);
        if (queryParam != null) {
          if (this.queryParameters == null) {
            this.queryParameters = new ArrayList<>();
          }
          this.queryParameters.add(new RestParameter(queryParam.value(), i, true));
        } else {
          RestParameter param = new RestParameter(parameter.getName(), i, false);
          if (bodyParam != null) {
            throw new IllegalStateException("Can not have multiple body parameters " + bodyParam + " and " + param
                + ". Are you missing @PathParam or @QueryParam for method " + method + "?");
          }
          bodyParam = param;
        }
      }
    }
    this.bodyParameter = bodyParam;
  }

  /**
   * @return the {@link RestParameter} for the HTTP body. May be {@code null} for empty body.
   */
  public RestParameter getBodyParameter() {

    return this.bodyParameter;
  }

  int getQueryParameterCount() {

    if (this.queryParameters == null) {
      return 0;
    }
    return this.queryParameters.size();
  }

  RestParameter getQueryParameter(int i) {

    if ((this.queryParameters != null) && (i < this.queryParameters.size())) {
      return this.queryParameters.get(i);
    }
    return null;
  }

  RestParameter getPathParameter(String name) {

    if (this.pathParameters == null) {
      return null;
    }
    return this.pathParameters.get(name);
  }

  RestParameter getRequiredPathParameter(String name) {

    RestParameter parameter = getPathParameter(name);
    if (parameter == null) {
      throw new IllegalArgumentException("Parameter {" + name + "} was not found for method " + this.method);
    }
    return parameter;
  }

}