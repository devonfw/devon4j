package com.devonfw.module.service.common.api.client.discovery;

/**
 * This interface abstracts the aspect of the {@link #discover(ServiceDiscoveryContext) discovery} of a
 * {@link com.devonfw.module.service.common.api.Service}. You may choose an exiting implementation or write your own to
 * customize the {@link #discover(ServiceDiscoveryContext) discovery} of your
 * {@link com.devonfw.module.service.common.api.Service}s.
 *
 * @since 3.0.0
 */
public interface ServiceDiscoverer {

  /**
   * @param context the {@link ServiceDiscoveryContext} where to
   *        {@link ServiceDiscoveryContext#setConfig(com.devonfw.module.basic.common.api.config.ConfigProperties)
   *        set the discovered configuration}. At least the {@link ServiceDiscoveryContext#getUrl() URL} has to be
   *        discovered.<br>
   *        It is possible to have multiple implementations of this interface as spring beans in your context. An
   *        implementation may decide that it is not responsible for the given {@link ServiceDiscoveryContext#getApi()
   *        service API} (e.g. only responsible for REST services). In that case it can return without doing any
   *        modifications to the given {@link ServiceDiscoveryContext}. Until the
   *        {@link ServiceDiscoveryContext#getUrl() URL} has not been discovered further implementations of this
   *        interface will be {@link #discover(ServiceDiscoveryContext) invoked}. If all implementations have been
   *        invoked without discovering the {@link ServiceDiscoveryContext#getUrl() URL}, discovery will fail with a
   *        runtime exception causing the {@link com.devonfw.module.service.common.api.client.ServiceClientFactory} to fail.
   */
  void discover(ServiceDiscoveryContext<?> context);

}
