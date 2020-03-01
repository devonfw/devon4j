package com.devonfw.module.security.jwt.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.devonfw.module.security.jwt.util.JwtAccessTokenConverterImpl;

/**
 * This Filter validates the token and sets {@link Authentication} object in {@link SecurityContext}
 *
 * @since 3.3.0
 *
 */
public class JwtAuthenticationFilter extends GenericFilterBean {

  @Inject
  private JwtAccessTokenConverterImpl jwtAccessTokenConverter;

  private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {

    Authentication authentication = this.jwtAccessTokenConverter.getAuthentication((HttpServletRequest) request);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
  }

}
