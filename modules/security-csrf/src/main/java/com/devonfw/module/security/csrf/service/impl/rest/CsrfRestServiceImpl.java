package com.devonfw.module.security.csrf.service.impl.rest;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;

import com.devonfw.module.security.csrf.service.api.rest.CsrfRestService;

/**
 * CsrfRestServiceImpl
 *
 */
@Named
@Transactional
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

  /**
   * @param csrfTokenRepository the csrfTokenRepository to set
   */
  @Inject
  public void setCsrfTokenRepository(CsrfTokenRepository csrfTokenRepository) {

    this.csrfTokenRepository = csrfTokenRepository;
  }

}
