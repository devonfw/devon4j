package com.devonfw.module.service.common.base.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.devonfw.module.basic.common.api.config.ConfigProperties;
import com.devonfw.module.basic.common.api.config.SimpleConfigProperties;
import com.devonfw.module.service.common.api.config.ServiceConfig;

/**
 * Implementation of {@link ServiceConfig} as spring-boot {@link ConfigurationProperties}.<br>
 * Assuming you would have the following in your {@code application.properties}:
 *
 * <pre>
 * service.client.app.foo.url=https://foo.company.com/services/rest
 * service.client.app.bar.url=https://bar.company.com/services/rest
 * service.client.default.url=https://api.company.com/services/rest
 * </pre>
 *
 * Then {@link #asClientConfig()}.{@link ConfigProperties#getChildValue(String)
 * getValue}({@link ServiceConfig#KEY_SEGMENT_APP}, "foo", {@link ServiceConfig#KEY_SEGMENT_URL}) would return
 * "https://foo.company.com/services/rest". This URL would be used by
 * {@link com.devonfw.module.service.common.impl.discovery.ServiceDiscovererImplConfig} for a
 * {@link com.devonfw.module.service.common.api.Service} of the
 * {@link com.devonfw.module.basic.common.api.reflect.Devon4jPackage#getApplication() application} "foo". For a
 * {@link com.devonfw.module.service.common.api.Service} of the
 * {@link com.devonfw.module.basic.common.api.reflect.Devon4jPackage#getApplication() application} "some" the URL
 * "https://api.company.com/services/rest" would be used as default because no property "service.client.app.some.url" is
 * defined.
 *
 * @since 3.0.0
 */
@ConfigurationProperties(prefix = "")
public class ServiceConfigProperties implements ServiceConfig {

  private final Map<String, String> service;

  private ConfigProperties configNode;

  /**
   * The constructor.
   */
  public ServiceConfigProperties() {
    super();
    this.service = new HashMap<>();
  }

  /**
   * @return client configuration {@link Map} from spring-boot {@code application.properties}. Do <b>not</b> modify.
   */
  public Map<String, String> getService() {

    return this.service;
  }

  @Override
  public ConfigProperties asConfig() {

    if (this.configNode == null) {
      this.configNode = SimpleConfigProperties.ofFlatMap("service", this.service);
    }
    return this.configNode;
  }

  @Override
  public ConfigProperties asClientConfig() {

    return asConfig().getChild(KEY_SEGMENT_CLIENT);
  }

}
