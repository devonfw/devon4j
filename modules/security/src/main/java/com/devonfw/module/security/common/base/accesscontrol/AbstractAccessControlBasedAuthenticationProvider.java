package com.devonfw.module.security.common.base.accesscontrol;

import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.devonfw.module.security.common.api.accesscontrol.AccessControl;
import com.devonfw.module.security.common.api.accesscontrol.AccessControlProvider;
import com.devonfw.module.security.common.api.accesscontrol.PrincipalAccessControlProvider;

/**
 * This is an implementation of {@link AbstractUserDetailsAuthenticationProvider} based on
 * {@link PrincipalAccessControlProvider} and {@link AccessControlProvider}.
 * @deprecated As of bug-fix release 2.1.2 the authentication mechanism changes. It is now based upon custom
 *             implementations of {@link UserDetailsService} in combination with {@link WebSecurityConfigurerAdapter}.
 *             For further information have a look at the sample application. <br/>
 *             <br/>
 *
 * @param <U> is the generic type of the {@link UserDetails} implementation used to bridge with spring-security.
 * @param
 *        <P>
 *        is the generic type of the {@link Principal} for internal user representation to bridge with
 *        {@link PrincipalAccessControlProvider}.
 *
 */
@Deprecated
public abstract class AbstractAccessControlBasedAuthenticationProvider<U extends UserDetails, P extends Principal>
    extends AbstractUserDetailsAuthenticationProvider {

  /** The {@link Logger} instance. */
  private static final Logger LOG = LoggerFactory.getLogger(AbstractAccessControlBasedAuthenticationProvider.class);

  private PrincipalAccessControlProvider<P> principalAccessControlProvider;

  private AccessControlProvider accessControlProvider;

  /**
   * The constructor.
   */
  public AbstractAccessControlBasedAuthenticationProvider() {

  }

  /**
   * @param principalAccessControlProvider the {@link PrincipalAccessControlProvider} to {@link Inject}.
   */
  @Inject
  public void setPrincipalAccessControlProvider(PrincipalAccessControlProvider<P> principalAccessControlProvider) {

    this.principalAccessControlProvider = principalAccessControlProvider;
  }

  /**
   * @param accessControlProvider the {@link AccessControlProvider} to {@link Inject}.
   */
  @Inject
  public void setAccessControlProvider(AccessControlProvider accessControlProvider) {

    this.accessControlProvider = accessControlProvider;
  }

  /**
   * Here the actual authentication has to be implemented.<br/>
   * <br/>
   *
   */
  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails,
      UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    // default implementation authentications via servlet API (container managed)
    ServletRequestAttributes currentRequestAttributes =
        (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

    HttpServletRequest request = currentRequestAttributes.getRequest();
    String login = authentication.getName();
    String password = null;
    Object credentials = authentication.getCredentials();
    if (credentials != null) {
      password = credentials.toString();
    }
    try {
      request.login(login, password);
    } catch (ServletException e) {
      LOG.warn("Authentication failed: {}", e.toString());
      throw new BadCredentialsException("Authentication failed.", e);
    }
    authentication.setDetails(userDetails);
  }

  /**
   * Returns the {@link GrantedAuthority}s of the provided user identified by the {@code username}.
   *
   * @param username the name of the user
   * @return the associated {@link GrantedAuthority}s
   * @throws AuthenticationException if not principal is retrievable for the given {@code username}
   */
  protected Set<GrantedAuthority> getAuthorities(String username) throws AuthenticationException {

    P principal = retrievePrincipal(username);
    if (principal == null) {
      LOG.warn("Failed to retrieve user for login {}.", username);
      throw new UsernameNotFoundException(username);
    }

    // determine granted authorities for spring-security...
    Set<GrantedAuthority> authorities = new HashSet<>();
    Collection<String> accessControlIds = this.principalAccessControlProvider.getAccessControlIds(principal);
    Set<AccessControl> accessControlSet = new HashSet<>();
    for (String id : accessControlIds) {
      boolean success = this.accessControlProvider.collectAccessControls(id, accessControlSet);
      if (!success) {
        LOG.warn("Undefined access control {}.", id);
        // authorities.add(new SimpleGrantedAuthority(id));
      }
    }
    for (AccessControl accessControl : accessControlSet) {
      authorities.add(new AccessControlGrantedAuthority(accessControl));
    }
    return authorities;
  }

  /**
   * Creates an instance of {@link UserDetails} that represent the user with the given <code>username</code>.
   *
   * @param username is the login of the user to create.
   * @param password the password of the user.
   * @param principal is the internal {@link Principal} that has been provided by
   *        {@link #retrievePrincipal(String, UsernamePasswordAuthenticationToken)}.
   * @param authorities are the {@link GrantedAuthority granted authorities} or in other words the permissions of the
   *        user.
   * @return the new user object.
   */
  protected abstract U createUser(String username, String password, P principal, Set<GrantedAuthority> authorities);

  /**
   * <b>DEPRECATED</b>
   *
   * Retrieves the internal {@link Principal} object representing the user. This can be any object implementing
   * {@link Principal} and can contain additional user details such as profile data. This object is used to
   * {@link PrincipalAccessControlProvider#getAccessControlIds(Principal) retrieve} the (top-level)
   * {@link AccessControl}s that have been granted to the user.
   *
   * @param username is the login of the user.
   * @param authentication is the {@link UsernamePasswordAuthenticationToken}.
   * @return the {@link Principal}.
   */
  @Deprecated
  protected abstract P retrievePrincipal(String username, UsernamePasswordAuthenticationToken authentication);

  /**
   * Retrieves the internal {@link Principal} object representing the user. This can be any object implementing
   * {@link Principal} and can contain additional user details such as profile data. This object is used to
   * {@link PrincipalAccessControlProvider#getAccessControlIds(Principal) retrieve} the (top-level)
   * {@link AccessControl}s that have been granted to the user.
   *
   * @param username is the login of the user.
   * @return the {@link Principal}.
   */
  protected abstract P retrievePrincipal(String username);

  @Override
  protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
      throws AuthenticationException {

    P principal = retrievePrincipal(username, authentication);
    if (principal == null) {
      LOG.warn("Failed to retrieve user for login {}.", username);
      throw new UsernameNotFoundException(username);
    }

    // determine granted authorities for spring-security...
    Set<GrantedAuthority> authorities = new HashSet<>();
    Collection<String> accessControlIds = this.principalAccessControlProvider.getAccessControlIds(principal);
    Set<AccessControl> accessControlSet = new HashSet<>();
    for (String id : accessControlIds) {
      boolean success = this.accessControlProvider.collectAccessControls(id, accessControlSet);
      if (!success) {
        LOG.warn("Undefined access control {}.", id);
        // authorities.add(new SimpleGrantedAuthority(id));
      }
    }
    for (AccessControl accessControl : accessControlSet) {
      authorities.add(new AccessControlGrantedAuthority(accessControl));
    }

    String password = null;
    Object credentials = authentication.getCredentials();
    if (credentials != null) {
      password = credentials.toString();
    }
    return createUser(username, password, principal, authorities);
  }
}
