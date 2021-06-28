
package com.devonfw.module.httpclient.common.impl.rest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.ws.rs.Path;

import com.devonfw.module.httpclient.common.impl.ServiceHttpClient;
import com.devonfw.module.httpclient.common.impl.SyncServiceClientFactoryHttp;
import com.devonfw.module.json.common.base.ObjectMapperFactory;
import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.api.client.sync.SyncServiceClientFactory;
import com.devonfw.module.service.common.api.constants.ServiceConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implementation of {@link SyncServiceClientFactory} for JAX-RS REST service clients using HTTP.
 *
 * @since 2021.08.003
 */
public class SyncServiceClientFactoryHttpRest extends SyncServiceClientFactoryHttp {

  private final Map<Class<?>, RestServiceMetadata<?>> serviceMetadataMap;

  private ClassLoader classLoader;

  private ObjectMapperFactory objectMapperFactory;

  private ObjectMapper objectMapper;

  /**
   * The constructor.
   */
  public SyncServiceClientFactoryHttpRest() {

    super();
    this.serviceMetadataMap = new ConcurrentHashMap<>();
  }

  /**
   * @return objectMapper
   */
  public ObjectMapper getObjectMapper() {

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
   * @return the {@link ClassLoader} to use.
   */

  public ClassLoader getClassLoader() {

    if (this.classLoader == null) {
      this.classLoader = Thread.currentThread().getContextClassLoader();
    }
    return this.classLoader;
  }

  /**
   * @param classLoader new value of {@link #getClassLoader()}.
   */

  public void setClassLoader(ClassLoader classLoader) {

    this.classLoader = classLoader;
  }

  @Override
  protected String getServiceTypeFolderName() {

    return ServiceConstants.URL_FOLDER_REST;
  }

  @Override
  protected boolean isResponsibleForService(ServiceContext<?> context) {

    return context.getApi().isAnnotationPresent(Path.class);
  }

  /**
   * @param <S> type of the {@link ServiceContext#getApi() service API}.
   * @param context the {@link ServiceContext}.
   * @return the {@link RestServiceMetadata} for the given {@link ServiceContext}.
   */
  @SuppressWarnings("unchecked")
  protected <S> RestServiceMetadata<S> getServiceMetadata(ServiceContext<S> context) {

    Class<S> api = context.getApi();
    RestServiceMetadata<S> serviceMetadata = (RestServiceMetadata<S>) this.serviceMetadataMap.computeIfAbsent(api,
        s -> new RestServiceMetadata<>(context));
    return serviceMetadata;
  }

  @Override
  protected <S> S createService(ServiceContext<S> context, String url) {

    ServiceHttpClient client = new ServiceHttpClient(getHttpClient(), url);
    return SyncServiceHttpClientRest.of(context, client, this).get();
  }

}
