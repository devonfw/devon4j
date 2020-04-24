package com.devonfw.module.security.jwt.common.base;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.devonfw.module.security.jwt.common.api.JwtAuthenticator;

/**
 * Filter that validates the token and sets {@link Authentication} object in {@link SecurityContext}
 *
 * @since 2020.04.001
 */
@Component
@Order(SecurityProperties.BASIC_AUTH_ORDER - 1)
public class JwtAuthenticationFilter extends GenericFilterBean {

  @Inject
  private JwtAuthenticator jwtAuthenticator;

  /**
   * The constructor.
   */
  public JwtAuthenticationFilter() {

    super();
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {

    // org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor would be helpful here
    // but the entire dependency seems overkill for 3 lines of code...
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String authorizationHeader = httpRequest.getHeader(JwtConstants.HEADER_AUTHORIZATION);

    if (authorizationHeader != null) {
      if (authorizationHeader.startsWith(JwtConstants.TOKEN_PREFIX)) {
        String token = authorizationHeader.substring(JwtConstants.TOKEN_PREFIX.length()).trim();
        Authentication authentication = this.jwtAuthenticator.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }
    filterChain.doFilter(request, response);
  }

}
