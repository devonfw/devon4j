package com.devonfw.module.service.common.impl.header;

import org.springframework.util.Base64Utils;

import com.devonfw.module.basic.common.api.config.ConfigProperties;
import com.devonfw.module.logging.common.api.LoggingConstants;
import com.devonfw.module.service.common.api.config.ServiceConfig;
import com.devonfw.module.service.common.api.header.ServiceHeaderContext;
import com.devonfw.module.service.common.api.header.ServiceHeaderCustomizer;

/**
 * Implementation of {@link ServiceHeaderCustomizer} that passes the {@link LoggingConstants#CORRELATION_ID} to a
 * subsequent {@link com.devonfw.module.service.common.api.Service} invocation.
 *
 * @since 3.0.0
 */
public class ServiceHeaderCustomizerBasicAuth implements ServiceHeaderCustomizer {

  /**
   * The constructor.
   */
  public ServiceHeaderCustomizerBasicAuth() {

    super();
  }

  @Override
  public void addHeaders(ServiceHeaderContext<?> context) {

    String auth = context.getConfig().getChildValue(ServiceConfig.KEY_SEGMENT_AUTH);
    if (!"basic".equals(auth)) {
      return;
    }
    ConfigProperties userConfig = context.getConfig().getChild(ServiceConfig.KEY_SEGMENT_USER);
    if (userConfig.isEmpty()) {
      return;
    }
    String login = userConfig.getChildValue(ServiceConfig.KEY_SEGMENT_USER_LOGIN);
    if (login == null) {
      return;
    }
    String password = userConfig.getChild(ServiceConfig.KEY_SEGMENT_USER_PASSWORD).getValue(String.class, login);
    String payload = login + ":" + password;
    String authorizationHeader = "Basic " + Base64Utils.encodeToString(payload.getBytes());
    context.setHeader("Authorization", authorizationHeader);
  }

}
