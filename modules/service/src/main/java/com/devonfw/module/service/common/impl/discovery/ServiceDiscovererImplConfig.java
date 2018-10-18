package com.devonfw.module.service.common.impl.discovery;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;

import com.devonfw.module.basic.common.api.config.ConfigProperties;
import com.devonfw.module.basic.common.api.config.MutableConfigProperties;
import com.devonfw.module.basic.common.api.reflect.Devon4jPackage;
import com.devonfw.module.service.common.api.client.discovery.ServiceDiscoverer;
import com.devonfw.module.service.common.api.client.discovery.ServiceDiscoveryContext;
import com.devonfw.module.service.common.api.config.ServiceConfig;
import com.devonfw.module.service.common.api.constants.ServiceConstants;

/**
 * A simple implementation of {@link ServiceDiscoverer} that is using {@link ServiceConfig}. The Service URL can be
 * configured per application providing the service as well as statically for all services (e.g. in case of a central
 * API gateway or a local proxy like <a href="https://lyft.github.io/envoy/">Envoy</a>
 * <a href="https://dzone.com/articles/microservices-patterns-with-envoy-sidecar-proxy-pa">Sidecar Proxy</a>).
 *
 * @see com.devonfw.module.service.common.base.config.ServiceConfigProperties
 *
 * @since 3.0.0
 */
public class ServiceDiscovererImplConfig implements ServiceDiscoverer, ApplicationListener<WebServerInitializedEvent> {

  // @Value("${local.server.port}")
  private int localServerPort;

  @Value("${server.servlet.context-path:}")
  private String contextPath;

  private ServiceConfig config;

  /**
   * @param config the {@link ServiceConfig} to {@link Inject}.
   */
  @Inject
  public void setConfig(ServiceConfig config) {

    this.config = config;
  }

  @Override
  public void onApplicationEvent(WebServerInitializedEvent event) {

    this.localServerPort = event.getWebServer().getPort();
  }

  @Override
  public void discover(ServiceDiscoveryContext<?> context) {

    Class<?> api = context.getApi();
    Devon4jPackage devon4jPackage = Devon4jPackage.of(api);
    String application = devon4jPackage.getApplication();
    if (application == null) {
      application = api.getName();
    }
    ConfigProperties clientNode = this.config.asClientConfig();
    ConfigProperties appNode = clientNode.getChild(ServiceConfig.KEY_SEGMENT_APP, application);
    ConfigProperties defaultNode = clientNode.getChild(ServiceConfig.KEY_SEGMENT_DEFAULT);
    MutableConfigProperties configNode = appNode.inherit(defaultNode);
    configNode = context.getConfig().inherit(configNode);
    String url = configNode.getChildValue(ServiceConfig.KEY_SEGMENT_URL);
    if (url == null) {
      String host = configNode.getChildValue(ServiceConfig.KEY_SEGMENT_HOST);
      if (host == null) {
        return;
      }
      String port = clientNode.getChild(ServiceConfig.KEY_SEGMENT_PORT).getValue();
      String protocol = clientNode.getChild(ServiceConfig.KEY_SEGMENT_PROTOCOL).getValue();
      if (protocol == null) {
        if ("443".equals(port)) {
          protocol = "https";
        }
        protocol = "http";
      }
      if ((port == null) && (isLocalhost(host))) {
        port = Integer.toString(this.localServerPort);
      }
      StringBuilder buffer = new StringBuilder();
      buffer.append(protocol);
      buffer.append("://");
      buffer.append(host);
      if (port != null) {
        buffer.append(':');
        buffer.append(port);
      }
      if (!this.contextPath.isEmpty()) {
        buffer.append(this.contextPath);
        buffer.append('/');
      }
      buffer.append(ServiceConstants.URL_PATH_SERVICES);
      buffer.append('/');
      buffer.append(ServiceConstants.VARIABLE_TYPE);
      url = buffer.toString();
    }
    url = resolveVariables(url, application);
    configNode.setChildValue(ServiceConfig.KEY_SEGMENT_URL, url);
    context.setConfig(configNode);
  }

  private String resolveVariables(String url2, String application) {

    String resolvedUrl = url2;
    resolvedUrl = resolvedUrl.replace(ServiceConstants.VARIABLE_APP, application);
    resolvedUrl = resolvedUrl.replace(ServiceConstants.VARIABLE_LOCAL_SERVER_PORT,
        Integer.toString(this.localServerPort));
    return resolvedUrl;
  }

  private boolean isLocalhost(String host) {

    if ("localhost".equalsIgnoreCase(host)) {
      return true;
    }
    if ("127.0.0.1".equals(host)) {
      return true;
    }
    if ("0:0:0:0:0:0:0:1".equals(host)) {
      return true;
    }
    if ("::1".equals(host)) {
      return true;
    }
    return false;
  }

}
