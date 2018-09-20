package ${package}.general.service.impl.rest;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;

import ${package}.general.common.api.exception.NoActiveUserException;
import ${package}.general.common.api.to.UserProfileTo;
import ${package}.general.service.api.rest.SecurityRestService;

/**
 * Implementation of {@link SecurityRestService}.
 */
@Named
@Transactional
public class SecurityRestServiceImpl implements SecurityRestService {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(SecurityRestServiceImpl.class);

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
