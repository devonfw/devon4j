package com.devonfw.module.httpclient.common.impl.rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Path;
import java.time.temporal.Temporal;
import java.util.function.Consumer;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devonfw.module.httpclient.common.impl.ServiceHttpClient;
import com.devonfw.module.service.common.api.client.async.ServiceClientInvocation;
import com.devonfw.module.service.common.api.client.async.ServiceClientStub;
import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.base.client.ServiceClientPerformanceLogger;
import com.devonfw.module.service.common.base.client.SyncServiceClientInvocationImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implementation of {@link ServiceClientStub} and {@link InvocationHandler} that acts as dynamic proxy for service API
 * to capture {@link ServiceClientInvocation}s.
 *
 * @param <S> type of the {@link ServiceContext#getApi() service API}.
 * @since 2021.08.003
 */
public class SyncServiceClientStubImpl<S> implements InvocationHandler {
  private static final Logger LOG = LoggerFactory.getLogger(SyncServiceClientStubImpl.class);

  private final ServiceContext<S> context;

  private S proxy;

  /** {@link ServiceHttpClient} to use. */
  protected final ServiceHttpClient client;

  private final ObjectMapper objectMapper;

  private final RestServiceMetadata<S> serviceMetadata;

  /** The most recent invocation. */
  private ServiceClientInvocation<S> invocation;

  private ServiceClientStub<S> stub;

  /**
   * The constructor.
   *
   * @param context the {@link ServiceContext}.
   * @param client {@link ServiceHttpClient}
   * @param objectMapper {@link ObjectMapper}
   * @param serviceMetadata {@link RestServiceMetadata}
   */
  protected SyncServiceClientStubImpl(ServiceContext<S> context, ServiceHttpClient client, ObjectMapper objectMapper,
      RestServiceMetadata<S> serviceMetadata) {

    super();
    this.context = context;
    this.client = client;
    this.objectMapper = objectMapper;
    this.serviceMetadata = serviceMetadata;
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
    ServiceClientInvocation<S> invocation;
    invocation = new SyncServiceClientInvocationImpl<>(method, args, this.context);
    HttpRequest request = createRequest(invocation);
    HttpResponse<String> response = this.client.getHttpClient().send(request, BodyHandlers.ofString());
    long startTime = System.nanoTime();
    handleResponse(response, startTime, invocation, null, ErrorHandlerThrowImmediately.get());
    Object result = createResult(response, invocation);
    return result;

  }

  private Throwable createError(HttpResponse<?> response, ServiceClientInvocation<S> invocation, String service) {

    int statusCode = response.statusCode();
    String contentType = response.headers().firstValue("Content-Type").orElse("application/json");
    String data = "";
    Object body = response.body();
    if (body instanceof String) {
      data = (String) body;
    } else {
      handleUnsupportedBody(body);
    }
    return this.factory.getErrorUnmarshaller().unmarshall(data, contentType, statusCode, service);
  }

  @SuppressWarnings({ "unchecked" })
  private <R> R handleResponse(HttpResponse<?> response, long startTime, ServiceClientInvocation<S> invocation,
      Consumer<R> resultHandler, Consumer<Throwable> errorHandler) {

    Throwable error = null;
    String service = invocation.getServiceDescription(response.uri().toString());
    try {
      int statusCode = response.statusCode();
      if (statusCode >= 400) {
        error = createError(response, invocation, service);
        errorHandler.accept(error);
      } else {
        R result = (R) createResult(response, invocation);
        if (resultHandler != null) {
          resultHandler.accept(result);
        }
        return result;
      }
    } catch (Throwable t) {
      errorHandler.accept(t);
      error = t;
    } finally {
      ServiceClientPerformanceLogger.log(startTime, service, response.statusCode(), error);
    }
    return null;
  }

  /**
   * @return errorHandler
   */
  public Consumer<Throwable> getErrorHandler() {

    return this.errorHandler;
  }

  /**
   * @param response {@link HttpResponse}
   * @param invocation {@link ServiceClientInvocation}
   * @return handleUnsupportedBody
   */
  protected Object createResult(HttpResponse<?> response, ServiceClientInvocation<S> invocation) {

    Object body = response.body();
    if (body == null) {
      return null;
    }
    Class<?> returnType = invocation.getMethod().getReturnType();
    if ((returnType == void.class) || (returnType == Void.class)) {
      return null;
    }
    if (body instanceof String) {
      if (CharSequence.class.isAssignableFrom(returnType)) {
        return body;
      }
      try {
        Object result = this.objectMapper.readValue((String) body, returnType);
        return result;
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    } else {
      return handleUnsupportedBody(body);
    }
  }

  /**
   * @param body the body of the HTTP request/response.
   * @return nothing. Will already throw an exception.
   */
  protected Object handleUnsupportedBody(Object body) {

    String bodyType = "null";
    if (body != null) {
      body.getClass().getName(); // avoid OWASP sensitive data exposure and only reveal classname in message
    }
    throw new UnsupportedOperationException(
        "HTTP request/response body of type " + bodyType + " is currently not supported!");
  }

  /**
   * @param invocation {@link HttpResponse}
   * @return requestBuilder of build().
   */
  protected HttpRequest createRequest(ServiceClientInvocation<S> invocation) {

    RestMethodMetadata method = this.serviceMetadata.getMethod(invocation.getMethod());
    String url = method.getPath().resolve(this.client.getBaseUrl(), invocation);
    String contentType = null;
    BodyPublisher body = null;
    RestParameter bodyParameter = method.getParameters().getBodyParameter();
    if (bodyParameter != null) {
      Object value = invocation.getParameter(bodyParameter.index);
      if (value != null) {
        if ((value instanceof CharSequence) || (value instanceof Number) || (value instanceof Temporal)) {
          body = BodyPublishers.ofString(value.toString());
        } else if (value instanceof File) {
          body = createBody(((File) value).toPath());
          contentType = MediaType.APPLICATION_OCTET_STREAM;
        } else if (value instanceof Path) {
          body = createBody((Path) value);
          contentType = MediaType.APPLICATION_OCTET_STREAM;
        } else {
          try {
            String json = this.objectMapper.writeValueAsString(value);
            body = BodyPublishers.ofString(json);
            contentType = MediaType.APPLICATION_JSON;
          } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
          }
        }
      }
    }
    Builder requestBuilder = this.client.requestBuilder(url, invocation, method.getHttpMethod(), body);
    if (contentType != null) {
      requestBuilder.header("Content-Type", contentType);
    }
    return requestBuilder.build();
  }

  private BodyPublisher createBody(Path path) {

    try {
      return BodyPublishers.ofFile(path);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public ServiceContext<S> getContext() {

    return this.context;
  }

  /**
   * @param <S> type of the {@link ServiceContext#getApi() service API}.
   * @param context the {@link ServiceContext}.
   * @param loader the {@link ClassLoader} to use.
   * @param client {@link ServiceHttpClient}
   * @param objectMapper {@link ObjectMapper}
   * @param serviceMetadata {@link RestServiceMetadata}
   * @return the {@link SyncServiceClientStubImpl}.
   */
  @SuppressWarnings("unchecked")
  public static <S> ServiceClientStub<S> of(ServiceContext<S> context, ClassLoader loader, ServiceHttpClient client,
      ObjectMapper objectMapper, RestServiceMetadata<S> serviceMetadata) {

    SyncServiceClientStubImpl<S> stub = new SyncServiceClientStubImpl<>(context, client, objectMapper, serviceMetadata);
    Class<?>[] interfaces = new Class<?>[] { context.getApi() };
    stub.proxy = (S) Proxy.newProxyInstance(loader, interfaces, stub);
    return stub;
  }

  private void logError(Throwable error) {

    ServiceContext<S> context = this.stub.getContext();
    String methodName = "undefined";
    if (this.invocation != null) {
      methodName = this.invocation.getMethod().getName();
    }
    LOG.error("Failed to invoke service {}#{} at {}", context.getApi().getName(), methodName, context.getUrl(), error);
  }

}
