package com.devonfw.module.service.common.impl.header;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.devonfw.module.service.common.api.config.ServiceConfig;
import com.devonfw.module.service.common.api.header.ServiceHeaderContext;
import com.devonfw.module.service.common.api.header.ServiceHeaderCustomizer;

/**
 * Implementation of {@link ServiceHeaderCustomizer} that forwards the {@code Authorization} HTTP header from the
 * current request as header to to subsequent {@link com.devonfw.module.service.common.api.Service} invocations.
 *
 * @since 3.0.0
 */
public class ServiceHeaderCustomizerAuthForward implements ServiceHeaderCustomizer {

  private static final String AUTHORIZATION = "Authorization";

  /**
   *
   * The constructor.
   */
  public ServiceHeaderCustomizerAuthForward() {

    super();
  }

  @Override
  public void addHeaders(ServiceHeaderContext<?> context) {

    String auth = context.getConfig().getChildValue(ServiceConfig.KEY_SEGMENT_AUTH);
    if (!ServiceConfig.VALUE_AUTH_FORWARD.equals(auth)) {
      return;
    }
    SecurityContext securityContext = SecurityContextHolder.getContext();
    if (securityContext == null) {
      return;
    }
    String authorizationHeader = context.getConfig().getChildValue(AUTHORIZATION);
    context.setHeader(AUTHORIZATION, authorizationHeader);
  }

}
