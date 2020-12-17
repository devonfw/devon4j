package com.devonfw.module.service.common.api.client;

import java.util.Map;

import com.devonfw.module.service.common.api.Service;

/**
 * This is the interface for a factory used to {@link #create(Class) create} client stubs for a {@link Service}. The
 * following example shows the typical usage in your code:
 *
 * <pre>
 * &#64;{@link javax.inject.Named}
 * public class UcMyUseCaseImpl extends MyUseCaseBase implements UcMyUseCase {
 *   &#64;{@link javax.inject.Inject} private {@link ServiceClientFactory} clientFactory;
 *
 *   &#64;{@link Override} &#64;{@link javax.annotation.security.RolesAllowed}(...)
 *   public Foo doSomething(Bar bar) {
 *     MyExternalServiceApi externalService = this.clientFactory.{@link ServiceClientFactory#create(Class) create}(MyExternalServiceApi.class);
 *     Some result = externalService.doSomething(convert(bar));
 *     return convert(result);
 *   }
 * }
 * </pre>
 *
 * As you can see creating a service client stub is easy and requires only a single line of code. However, internally a
 * lot of things happen such as the following aspects:
 * <ul>
 * <li>{@link com.devonfw.module.service.common.api.client.discovery.ServiceDiscoverer#discover(com.devonfw.module.service.common.api.client.discovery.ServiceDiscoveryContext)
 * service discovery}.</li>
 * <li>{@link com.devonfw.module.service.common.api.header.ServiceHeaderCustomizer#addHeaders(com.devonfw.module.service.common.api.header.ServiceHeaderContext)
 * header customization} (for security, correlation-ID, etc.).</li>
 * <li>performance logging</li>
 * <li>exception mapping (exception facade)</li>
 * </ul>
 * All these aspects can be configured via spring and customized with own implementations.
 *
 * <b>ATTENTION:</b> This {@link ServiceClientFactory} is thread-safe. However, the client objects returned by it are
 * not thread-safe and shall not be reused or shared between threads.
 *
 * @since 3.0.0
 */
public interface ServiceClientFactory {

  /**
   * @param <S> type of the {@link com.devonfw.module.service.common.api.client.context.ServiceContext#getApi() service
   *        API}.
   * @param serviceInterface the {@link Class} reflecting the interface that defines the API of your {@link Service}.
   * @return a new instance of the given {@code serviceInterface} that is a client stub. Invocations to any of the
   *         service methods will trigger a remote call and synchronously return the result.
   */
  <S> S create(Class<S> serviceInterface);

  /**
   * @param <S> type of the {@link com.devonfw.module.service.common.api.client.context.ServiceContext#getApi() service
   *        API}.
   * @param serviceInterface the {@link Class} reflecting the interface that defines the API of your {@link Service}.
   * @param config the {@link Map} with explicit configuration properties. See
   *        {@link com.devonfw.module.service.common.base.config.ServiceConfigProperties} for further details.
   * @return a new instance of the given {@code serviceInterface} that is a client stub. Invocations to any of the
   *         service methods will trigger a remote call and synchronously return the result.
   */
  <S> S create(Class<S> serviceInterface, Map<String, String> config);

  /**
   * @param <S> type of the {@link com.devonfw.module.service.common.api.client.context.ServiceContext#getApi() service
   *        API}.
   * @param serviceInterface the {@link Class} reflecting the interface that defines the API of your {@link Service}.
   * @return an {@link AsyncServiceClient} allowing to call service operations asynchronously.
   */
  <S> AsyncServiceClient<S> createAsync(Class<S> serviceInterface);

  /**
   * @param <S> type of the {@link com.devonfw.module.service.common.api.client.context.ServiceContext#getApi() service
   *        API}.
   * @param serviceInterface the {@link Class} reflecting the interface that defines the API of your {@link Service}.
   * @param config the {@link Map} with explicit configuration properties. See
   *        {@link com.devonfw.module.service.common.base.config.ServiceConfigProperties} for further details.
   * @return an {@link AsyncServiceClient} allowing to call service operations asynchronously.
   */
  <S> AsyncServiceClient<S> createAsync(Class<S> serviceInterface, Map<String, String> config);

}
