package com.devonfw.module.security.csrf.service.impl.rest;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;

import com.devonfw.module.security.csrf.service.api.rest.CsrfRestService;

/**
 * Implementation of {@link CsrfRestService}.
 *
 * @since 2020.12.001
 */
public class CsrfRestServiceImpl implements CsrfRestService {

  private static final Logger LOG = LoggerFactory.getLogger(CsrfRestServiceImpl.class);

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
   * @param csrfTokenRepository the {@link CsrfTokenRepository} to {@link Inject}.
   */
  @Inject
  public void setCsrfTokenRepository(CsrfTokenRepository csrfTokenRepository) {

    this.csrfTokenRepository = csrfTokenRepository;
  }

}
