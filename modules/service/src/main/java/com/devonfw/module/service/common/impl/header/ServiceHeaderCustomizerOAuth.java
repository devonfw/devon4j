package com.devonfw.module.service.common.impl.header;

import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.devonfw.module.service.common.api.config.ServiceConfig;
import com.devonfw.module.service.common.api.header.ServiceHeaderContext;
import com.devonfw.module.service.common.api.header.ServiceHeaderCustomizer;

/**
 * Implementation of {@link ServiceHeaderCustomizer} that passes a potential {@code oauth.token} property from the
 * current {@link Authentication} to subsequent {@link com.devonfw.module.service.common.api.Service} invocations.
 *
 * @since 3.0.0
 */
public class ServiceHeaderCustomizerOAuth implements ServiceHeaderCustomizer {

  private static final String JWT_SEGMENT_REGEX = "[A-Za-z0-9-_=]{4,}";

  private static final Pattern JWT_PATTERN = Pattern
      .compile("^" + JWT_SEGMENT_REGEX + "\\." + JWT_SEGMENT_REGEX + "\\." + JWT_SEGMENT_REGEX + "$");

  /**
   * The constructor.
   */
  public ServiceHeaderCustomizerOAuth() {

    super();
  }

  @Override
  public void addHeaders(ServiceHeaderContext<?> context) {

    String auth = context.getConfig().getChildValue(ServiceConfig.KEY_SEGMENT_AUTH);
    if (!ServiceConfig.VALUE_AUTH_OAUTH.equals(auth)) {
      return;
    }
    Object oauthToken = findToken();
    if (oauthToken == null) {
      return;
    }
    String authorizationHeader = "Bearer " + oauthToken;
    context.setHeader("Authorization", authorizationHeader);
  }

  private Object findToken() {

    SecurityContext securityContext = SecurityContextHolder.getContext();
    if (securityContext == null) {
      return null;
    }
    Authentication authentication = securityContext.getAuthentication();
    if (authentication == null) {
      return null;
    }
    Object credentials = authentication.getCredentials();
    if (credentials instanceof String) {
      // most obvious "API" of spring-security to store JWT (also done this way in devon4j-security-jwt)
      String token = (String) credentials;
      // reduce risk of forwarding regular password due to configuration error to other service...
      if (JWT_PATTERN.matcher(token).matches()) {
        return token;
      }
    }
    Object details = authentication.getDetails();
    if (!(details instanceof Map)) {
      return null;
    }
    Map<?, ?> map = (Map<?, ?>) details;
    return map.get("oauth.token");
  }

}
