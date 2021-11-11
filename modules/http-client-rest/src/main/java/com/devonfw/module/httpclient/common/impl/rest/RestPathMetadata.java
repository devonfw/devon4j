package com.devonfw.module.httpclient.common.impl.rest;

import java.lang.reflect.Method;

import javax.ws.rs.Path;

import com.devonfw.module.service.common.api.client.async.ServiceClientInvocation;

/**
 * Metadata for a {@link javax.ws.rs.Path REST path}.
 *
 * @since 2020.08.001
 */
public abstract class RestPathMetadata extends RestMetadata {

  private RestPathMetadata next;

  /**
   * @param baseUrl the base URL.
   * @param invocation the {@link ServiceClientInvocation} to build the URL path.
   * @return the final URL to invoke.
   */
  public String resolve(String baseUrl, ServiceClientInvocation<?> invocation) {

    StringBuilder pathBuilder = new StringBuilder(baseUrl);
    resolve(invocation, pathBuilder, false);
    return pathBuilder.toString();
  }

  private void resolve(ServiceClientInvocation<?> invocation, StringBuilder pathBuilder, boolean queryStarted) {

    queryStarted = doResolve(invocation, pathBuilder, queryStarted);
    if (this.next != null) {
      this.next.resolve(invocation, pathBuilder, queryStarted);
    }
  }

  /**
   * @param invocation the {@link ServiceClientInvocation} to build the URL path.
   * @param pathBuilder the {@link StringBuilder} where to append that URL path.
   * @param queryStarted - {@code true} if the URL query part has been started ('?' was written), {@code false}
   *        otherwise.
   * @return the {@code true} if the URL query part has been started ('?' was written), {@code false} otherwise.
   */
  protected abstract boolean doResolve(ServiceClientInvocation<?> invocation, StringBuilder pathBuilder,
      boolean queryStarted);

  /**
   * @return the last {@link RestPathMetadata} in this chain.
   */
  protected RestPathMetadata last() {

    RestPathMetadata last = this;
    while (last.next != null) {
      last = last.next;
    }
    return last;
  }

  /**
   * @param basePath the URL base path from the service interface {@link Path} annotation.
   * @param method the JAX-RS {@link Method} to introspect.
   * @param restParameters the {@link RestParameters}.
   * @return the parsed {@link RestPathMetadata}.
   */
  public static RestPathMetadata of(String basePath, Method method, RestParameters restParameters) {

    String path = buildPath(basePath, method);

    // parse URL path
    RestPathMetadata pathMetadata = null;
    int varStart = path.indexOf('{');
    if (varStart < 0) {
      pathMetadata = new RestPathMetadataStatic(path);
    } else {
      pathMetadata = createPathMetadata(path, varStart, restParameters);
    }
    int queryParameterCount = restParameters.getQueryParameterCount();
    if (queryParameterCount > 0) {
      RestPathMetadata last = pathMetadata.last();
      for (int i = 0; i < queryParameterCount; i++) {
        last.next = new RestPathMetadataParameter("", restParameters.getQueryParameter(i));
        last = last.next;
      }
    }
    return pathMetadata;
  }

  private static RestPathMetadata createPathMetadata(String path, int varStart, RestParameters restParameters) {

    RestPathMetadata pathMetadata = null;
    int start = 0;
    while (varStart >= 0) {
      String prefix = path.substring(start, varStart);
      varStart++;
      int varEnd = path.indexOf('}', varStart);
      if (varEnd <= varStart) {
        throw new IllegalStateException("Invalid REST path " + path + ": variable at index " + varStart
            + " empty or not closed at method " + restParameters.method);
      }
      String parameterName = path.substring(varStart, varEnd);
      RestParameter parameter = restParameters.getRequiredPathParameter(parameterName);
      pathMetadata = new RestPathMetadataParameter(prefix, parameter);

      start = varEnd + 1;
      varStart = path.indexOf('{', start);
    }
    return pathMetadata;
  }

  private static String buildPath(String basePath, Method method) {

    String path = basePath;
    boolean required = false; // @Path is not required on method according to JAX-RS
    Path pathAnnotation = findAnnotation(method, Path.class, required);
    if (pathAnnotation != null) {
      String value = pathAnnotation.value();
      if (path.endsWith("/") || value.startsWith("/")) {
        path = path + value;
      } else {
        path = path + "/" + value;
      }
    }
    return path;
  }

  private static class RestPathMetadataParameter extends RestPathMetadata {

    private final String prefix;

    private final RestParameter parameter;

    private RestPathMetadataParameter(String prefix, RestParameter parameter) {

      super();
      this.prefix = prefix;
      this.parameter = parameter;
    }

    @Override
    public boolean doResolve(ServiceClientInvocation<?> invocation, StringBuilder pathBuilder, boolean queryStarted) {

      pathBuilder.append(this.prefix);
      Object value = invocation.getParameter(this.parameter.index);
      return this.parameter.format(value, pathBuilder, queryStarted);
    }

  }

  private static class RestPathMetadataStatic extends RestPathMetadata {

    private final String path;

    private RestPathMetadataStatic(String path) {

      super();
      this.path = path;
    }

    @Override
    public boolean doResolve(ServiceClientInvocation<?> invocation, StringBuilder pathBuilder, boolean queryStarted) {

      pathBuilder.append(this.path);
      return queryStarted;
    }
  }

}
