package com.devonfw.module.security.csrf.service.impl.rest;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;

import com.devonfw.module.security.csrf.common.api.exception.NoActiveUserException;
import com.devonfw.module.security.csrf.common.api.to.UserProfileTo;
import com.devonfw.module.security.csrf.service.api.rest.CsrfRestService;

/**
 * CsrfRestServiceImpl
 *
 */
public class CsrfRestServiceImpl implements CsrfRestService {
  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(CsrfRestServiceImpl.class);

  /**
   * Use {@link CsrfTokenRepository} for CSRF protection.
   */
  private CsrfTokenRepository csrfTokenRepository;

  @Override
  @PermitAll
  public CsrfToken getCsrfToken(HttpServletRequest request, HttpServletResponse response) {

    CsrfToken token = this.csrfTokenRepository.loadToken(request);
    if (token == null) {
      LOG.error("No CsrfToken could be found - instantiating a new Token");
      token = this.csrfTokenRepository.generateToken(request);
      this.csrfTokenRepository.saveToken(token, request, response);
    }
    return token;
  }

  @Override
  @PermitAll
  public UserProfileTo getCurrentUser() {

    SecurityContext context = SecurityContextHolder.getContext();
    Authentication authentication = null;
    if (context != null) {
      authentication = context.getAuthentication();
    }
    if (authentication == null) {
      throw new NoActiveUserException();
    }
    UserDetails user = (UserDetails) authentication.getPrincipal();
    UserProfileTo profile = new UserProfileTo();
    profile.setLogin(user.getUsername());
    return profile;
  }

  /**
   * @param csrfTokenRepository the csrfTokenRepository to set
   */
  @Inject
  public void setCsrfTokenRepository(CsrfTokenRepository csrfTokenRepository) {

    this.csrfTokenRepository = csrfTokenRepository;
  }
}
