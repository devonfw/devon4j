package com.devonfw.module.security.common.impl.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Sends the OK status code upon successful authentication.
 *
 * @see JsonUsernamePasswordAuthenticationFilter
 */
public class AuthenticationSuccessHandlerSendingOkHttpStatusCode implements AuthenticationSuccessHandler {
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    if (response.isCommitted()) {
      return;
    }
    clearAuthenticationAttributes(request);
    response.setStatus(HttpServletResponse.SC_OK);
  }

  private void clearAuthenticationAttributes(HttpServletRequest request) {

    HttpSession session = request.getSession(false);
    if (session == null) {
      return;
    }
    session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
  }
}
