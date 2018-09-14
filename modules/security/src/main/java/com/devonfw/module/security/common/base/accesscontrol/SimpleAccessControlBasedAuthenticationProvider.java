package com.devonfw.module.security.common.base.accesscontrol;

import java.security.Principal;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.devonfw.module.security.common.api.accesscontrol.AccessControlProvider;
import com.devonfw.module.security.common.api.accesscontrol.PrincipalAccessControlProvider;

/**
 * @deprecated As of bug-fix release 2.1.2 the authentication mechanism changes. It is now based upon custom
 *             implementations of {@link UserDetailsService} in combination with {@link WebSecurityConfigurerAdapter}.
 *             For further information have a look at the sample application. <br/>
 *             This is an implementation of {@link AbstractUserDetailsAuthenticationProvider} based on
 *             {@link PrincipalAccessControlProvider} and {@link AccessControlProvider}. <br/>
 *             <br/>
 *             This is a simple implementation of {@link AbstractAccessControlBasedAuthenticationProvider}.
 *
 */
@Deprecated
public class SimpleAccessControlBasedAuthenticationProvider
    extends AbstractAccessControlBasedAuthenticationProvider<User, Principal> {

  /**
   * The constructor.
   */
  public SimpleAccessControlBasedAuthenticationProvider() {

    super();
  }

  @Override
  protected User createUser(String username, String password, Principal principal, Set<GrantedAuthority> authorities) {

    User user = new User(username, password, authorities);
    return user;
  }

  @Override
  protected Principal retrievePrincipal(String username, UsernamePasswordAuthenticationToken authentication) {

    return authentication;
  }

  /*
   * Leave empty on purpose. Not used in this version.
   */
  @Override
  protected Principal retrievePrincipal(String username) {

    return null;
  }

}
