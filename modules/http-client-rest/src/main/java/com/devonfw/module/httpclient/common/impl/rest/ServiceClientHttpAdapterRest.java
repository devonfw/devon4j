package com.devonfw.module.httpclient.common.impl.rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.temporal.Temporal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import com.devonfw.module.httpclient.common.impl.AbstractServiceClientHttpAdapter;
import com.devonfw.module.httpclient.common.impl.ServiceClientHttpAdapter;
import com.devonfw.module.json.common.base.ObjectMapperFactory;
import com.devonfw.module.service.common.api.client.async.ServiceClientInvocation;
import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implementation of {@link ServiceClientHttpAdapter} for REST using {@link HttpClient}.
 *
 * @since 2021.04.003
 */
public class ServiceClientHttpAdapterRest extends AbstractServiceClientHttpAdapter {

  private final Map<Class<?>, RestServiceMetadata<?>> serviceMetadataMap;

  private ObjectMapperFactory objectMapperFactory;

  private ObjectMapper objectMapper;

  /**
   * The constructor.
   */
  public ServiceClientHttpAdapterRest() {

    super();
    this.serviceMetadataMap = new ConcurrentHashMap<>();
  }

  /**
   * @return the {@link ObjectMapper} used for JSON mapping.
   */
  protected ObjectMapper getObjectMapper() {

    if (this.objectMapper == null) {
      this.objectMapper = this.objectMapperFactory.createInstance();
    }
    return this.objectMapper;
  }

  /**
   * @return the {@link ObjectMapperFactory}.
   */
  public ObjectMapperFactory getObjectMapperFactory() {

    return this.objectMapperFactory;
  }

  /**
   * @param objectMapperFactory the {@link ObjectMapperFactory} to {@link Inject}.
   */
  @Inject
  public void setObjectMapperFactory(ObjectMapperFactory objectMapperFactory) {

    this.objectMapperFactory = objectMapperFactory;
  }

  /**
   * @param context the {@link ServiceContext}.
   * @return the {@link RestServiceMetadata} for the given {@link ServiceContext}.
   */
  protected RestServiceMetadata<?> getServiceMetadata(ServiceContext<?> context) {

    Class<?> api = context.getApi();
    RestServiceMetadata<?> serviceMetadata = this.serviceMetadataMap.computeIfAbsent(api,
        s -> new RestServiceMetadata<>(context));
    return serviceMetadata;
  }

  @Override
  public HttpRequest createRequest(ServiceClientInvocation<?> invocation, String baseUrl) {

    RestServiceMetadata<?> serviceMetadata = getServiceMetadata(invocation.getContext());
    RestMethodMetadata method = serviceMetadata.getMethod(invocation.getMethod());
    String url = method.getPath().resolve(baseUrl, invocation);
    String contentType = null;
    BodyPublisher body = null;
    RestParameter bodyParameter = method.getParameters().getBodyParameter();
    if (bodyParameter != null) {
      Object value = invocation.getParameter(bodyParameter.index);
      if (value != null) {
        if ((value instanceof CharSequence) || (value instanceof Number) || (value instanceof Temporal)) {
          body = BodyPublishers.ofString(value.toString());
          // contentType = MediaType.TEXT_PLAIN;
        } else if (value instanceof File) {
          body = createBody(((File) value).toPath());
          contentType = MediaType.APPLICATION_OCTET_STREAM;
        } else if (value instanceof Path) {
          body = createBody((Path) value);
          contentType = MediaType.APPLICATION_OCTET_STREAM;
        } else {
          try {
            String json = getObjectMapper().writeValueAsString(value);
            body = BodyPublishers.ofString(json);
            contentType = MediaType.APPLICATION_JSON;
          } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
          }
        }
      }
    }
    Builder requestBuilder = requestBuilder(url, invocation, method.getHttpMethod(), body);
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
  protected Object createResult(HttpResponse<?> response, ServiceClientInvocation<?> invocation) {

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
        Object result = getObjectMapper().readValue((String) body, returnType);
        return result;
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    } else {
      throw createErrorUnsupportedBody(body);
    }
  }
}
