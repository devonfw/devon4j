package ${package}.general.service.impl.rest;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


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


}