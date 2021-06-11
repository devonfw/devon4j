package com.devonfw.module.service.common.impl.client;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import com.devonfw.module.basic.common.api.config.ConfigProperties;
import com.devonfw.module.basic.common.api.config.SimpleConfigProperties;
import com.devonfw.module.service.common.api.client.AsyncServiceClient;
import com.devonfw.module.service.common.api.client.ServiceClientFactory;
import com.devonfw.module.service.common.api.client.SyncServiceClient;
import com.devonfw.module.service.common.api.client.async.AsyncServiceClientFactory;
import com.devonfw.module.service.common.api.client.discovery.ServiceDiscoverer;
import com.devonfw.module.service.common.api.client.sync.SyncServiceClientFactory;
import com.devonfw.module.service.common.api.header.ServiceHeaderCustomizer;
import com.devonfw.module.service.common.base.context.ServiceContextImpl;

/**
 * This is the implementation of {@link ServiceClientFactory}.
 *
 * @since 3.0.0
 */
public class ServiceClientFactoryImpl implements ServiceClientFactory {

  private Collection<SyncServiceClientFactory> syncServiceClientFactories;

  private Collection<AsyncServiceClientFactory> asyncServiceClientFactories;

  private Collection<ServiceDiscoverer> serviceDiscoverers;

  private Collection<ServiceHeaderCustomizer> serviceHeaderCustomizers;

  /**
   * The constructor.
   */
  public ServiceClientFactoryImpl() {

    super();
  }

  /**
   * @param syncServiceClientFactories the {@link Collection} of {@link SyncServiceClientFactory factories} to
   *        {@link Inject}.
   */
  @Inject
  public void setSyncServiceClientFactories(Collection<SyncServiceClientFactory> syncServiceClientFactories) {

    this.syncServiceClientFactories = syncServiceClientFactories;
  }

  /**
   * @param asyncServiceClientFactories the {@link Collection} of {@link AsyncServiceClientFactory factories} to
   *        {@link Inject}.
   */
  @Inject
  public void setAsyncServiceClientFactories(Collection<AsyncServiceClientFactory> asyncServiceClientFactories) {

    this.asyncServiceClientFactories = asyncServiceClientFactories;
  }

  /**
   * @param serviceDiscoverers the {@link Collection} of {@link ServiceDiscoverer}s to {@link Inject}.
   */
  @Inject
  public void setServiceDiscoverers(Collection<ServiceDiscoverer> serviceDiscoverers) {

    this.serviceDiscoverers = serviceDiscoverers;
  }

  /**
   * @param serviceHeaderCustomizers the {@link Collection} of {@link ServiceHeaderCustomizer}s to {@link Inject}.
   */
  @Inject
  public void setServiceHeaderCustomizers(Collection<ServiceHeaderCustomizer> serviceHeaderCustomizers) {

    this.serviceHeaderCustomizers = serviceHeaderCustomizers;
  }

  @Override
  public <S> S create(Class<S> serviceInterface) {

    return create(serviceInterface, null);
  }

  @Override
  public <S> S create(Class<S> serviceInterface, Map<String, String> config) {

    ServiceContextImpl<S> context = createContext(serviceInterface, config);
    S serviceClient = createClient(serviceInterface, context);
    return serviceClient;
  }

  private <S> ServiceContextImpl<S> createContext(Class<S> serviceInterface, Map<String, String> config) {

    ConfigProperties configPropreties = ConfigProperties.EMPTY;
    if ((config != null) && !config.isEmpty()) {
      configPropreties = SimpleConfigProperties.ofFlatMap(config);
    }
    ServiceContextImpl<S> context = new ServiceContextImpl<>(serviceInterface, configPropreties);
    discovery(context);
    customizeHeaders(context);
    return context;
  }

  private <S> S createClient(Class<S> serviceInterface, ServiceContextImpl<S> context) {

    S serviceClient = null;
    for (SyncServiceClientFactory factory : this.syncServiceClientFactories) {
      serviceClient = factory.create(context);
      if (serviceClient != null) {
        break;
      }
    }
    if (serviceClient == null) {
      throw new IllegalStateException(
          "Unsuppoerted service type - client could not be created by any factory for " + serviceInterface);
    }
    return serviceClient;
  }

  private <S> void customizeHeaders(ServiceContextImpl<S> context) {

    for (ServiceHeaderCustomizer headerCustomizer : this.serviceHeaderCustomizers) {
      headerCustomizer.addHeaders(context);
    }
  }

  private <S> void discovery(ServiceContextImpl<S> context) {

    if (context.getUrl() != null) {
      return;
    }
    if (this.serviceDiscoverers != null) {
      for (ServiceDiscoverer discoverer : this.serviceDiscoverers) {
        discoverer.discover(context);
        if (context.getUrl() != null) {
          break;
        }
      }
    }
    if (context.getUrl() == null) {
      throw new IllegalStateException("Service discovery failed for " + context.getApi());
    }
  }

  @Override
  public <S> AsyncServiceClient<S> createAsync(Class<S> serviceInterface) {

    return createAsync(serviceInterface, null);
  }

  @Override
  public <S> AsyncServiceClient<S> createAsync(Class<S> serviceInterface, Map<String, String> config) {

    ServiceContextImpl<S> context = createContext(serviceInterface, config);
    AsyncServiceClient<S> serviceClient = createClientAsync(serviceInterface, context);
    return serviceClient;
  }

  private <S> AsyncServiceClient<S> createClientAsync(Class<S> serviceInterface, ServiceContextImpl<S> context) {

    AsyncServiceClient<S> serviceClient = null;
    for (AsyncServiceClientFactory factory : this.asyncServiceClientFactories) {
      serviceClient = factory.create(context);
      if (serviceClient != null) {
        break;
      }
    }
    if (serviceClient == null) {
      throw new IllegalStateException(
          "Unsuppoerted service type - client could not be created by any factory for " + serviceInterface);
    }
    return serviceClient;
  }

  @Override
  public <S> SyncServiceClient<S> createSync(Class<S> serviceInterface, Map<String, String> config) {

    ServiceContextImpl<S> context = createContext(serviceInterface, config);
    SyncServiceClient<S> serviceClient = createClientSync(serviceInterface, context);
    return serviceClient;
  }

  private <S> SyncServiceClient<S> createClientSync(Class<S> serviceInterface, ServiceContextImpl<S> context) {

    SyncServiceClient<S> serviceClient = null;
    for (SyncServiceClientFactory factory : this.syncServiceClientFactories) {
      serviceClient = factory.create(context);
      if (serviceClient != null) {
        break;
      }
    }
    if (serviceClient == null) {
      throw new IllegalStateException(
          "Unsuppoerted service type - client could not be created by any factory for " + serviceInterface);
    }
    return serviceClient;
  }

  @Override
  public <S> SyncServiceClient<S> createSync(Class<S> serviceInterface) {

    return createSync(serviceInterface, null);
  }

}
