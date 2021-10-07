package com.devonfw.module.service.common.impl.discovery;

import org.junit.jupiter.api.Test;

import com.devonfw.module.service.common.api.client.discovery.ServiceDiscoveryContext;
import com.devonfw.module.service.common.base.config.ServiceConfigProperties;
import com.devonfw.module.service.common.base.context.ServiceContextImpl;
import com.devonfw.module.test.common.base.ModuleTest;

/**
 * Test of {@link ServiceDiscovererImplConfig}.
 */
public class ServiceDiscovererImplConfigTest extends ModuleTest {

  /**
   * Test if {@link ServiceDiscovererImplConfig} with contextPath being "/".
   */
  @Test
  public void testUrlWithContextPathSlash() {

    // given
    ServiceDiscovererImplConfig discoverer = new ServiceDiscovererImplConfig();
    ServiceConfigProperties config = createConfig();
    discoverer.setContextPath("/");
    discoverer.setConfig(config);
    ServiceDiscoveryContext<?> context = new ServiceContextImpl<>(DemoService.class, config.asClientConfig());
    // when
    discoverer.discover(context);
    // then
    assertThat(context.getUrl()).isEqualTo("http://localhost:8080/services/${type}");
  }

  /**
   * Test if {@link ServiceDiscovererImplConfig} with contextPath being "".
   */
  @Test
  public void testUrlWithContextPathEmpty() {

    // given
    ServiceDiscovererImplConfig discoverer = new ServiceDiscovererImplConfig();
    ServiceConfigProperties config = createConfig();
    discoverer.setContextPath("");
    discoverer.setConfig(config);
    ServiceDiscoveryContext<?> context = new ServiceContextImpl<>(DemoService.class, config.asClientConfig());
    // when
    discoverer.discover(context);
    // then
    assertThat(context.getUrl()).isEqualTo("http://localhost:8080/services/${type}");
  }

  /**
   * Test if {@link ServiceDiscovererImplConfig} with contextPath being "my-app".
   */
  @Test
  public void testUrlWithContextPathMyApp() {

    // given
    ServiceDiscovererImplConfig discoverer = new ServiceDiscovererImplConfig();
    ServiceConfigProperties config = createConfig();
    discoverer.setContextPath("my-app");
    discoverer.setConfig(config);
    ServiceDiscoveryContext<?> context = new ServiceContextImpl<>(DemoService.class, config.asClientConfig());
    // when
    discoverer.discover(context);
    // then
    assertThat(context.getUrl()).isEqualTo("http://localhost:8080/my-app/services/${type}");
  }

  /**
   * Test if {@link ServiceDiscovererImplConfig} with contextPath being "/my-app".
   */
  @Test
  public void testUrlWithContextPathSlashMyApp() {

    // given
    ServiceDiscovererImplConfig discoverer = new ServiceDiscovererImplConfig();
    ServiceConfigProperties config = createConfig();
    discoverer.setContextPath("/my-app");
    discoverer.setConfig(config);
    ServiceDiscoveryContext<?> context = new ServiceContextImpl<>(DemoService.class, config.asClientConfig());
    // when
    discoverer.discover(context);
    // then
    assertThat(context.getUrl()).isEqualTo("http://localhost:8080/my-app/services/${type}");
  }

  /**
   * Test if {@link ServiceDiscovererImplConfig} with contextPath being "my-app/".
   */
  @Test
  public void testUrlWithContextPathMyAppSlash() {

    // given
    ServiceDiscovererImplConfig discoverer = new ServiceDiscovererImplConfig();
    ServiceConfigProperties config = createConfig();
    discoverer.setContextPath("my-app/");
    discoverer.setConfig(config);
    ServiceDiscoveryContext<?> context = new ServiceContextImpl<>(DemoService.class, config.asClientConfig());
    // when
    discoverer.discover(context);
    // then
    assertThat(context.getUrl()).isEqualTo("http://localhost:8080/my-app/services/${type}");
  }

  private ServiceConfigProperties createConfig() {

    ServiceConfigProperties config = new ServiceConfigProperties();
    config.getService().put("client.default.host", "localhost");
    config.getService().put("client.default.port", "8080");
    return config;
  }

}
