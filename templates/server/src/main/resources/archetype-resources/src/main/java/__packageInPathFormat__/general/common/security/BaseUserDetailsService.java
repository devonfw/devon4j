package ${package}.general.common.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ${package}.general.common.security.accesscontrol.AccessControl;
import ${package}.general.common.security.accesscontrol.AccessControlGrantedAuthority;
import ${package}.general.common.security.accesscontrol.AccessControlProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Custom implementation of {@link UserDetailsService}.<br>
 *
 * @see ${package}.general.common.security.BaseWebSecurityConfig
 */
@ApplicationScoped
@Named
@javax.inject.Named
public class BaseUserDetailsService implements UserDetailsService {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(BaseUserDetailsService.class);

  private AuthenticationManagerBuilder amBuilder;

  private AccessControlProvider accessControlProvider;

  /**
   * The constructor.
   */
  public BaseUserDetailsService() {

    super();
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    try {
      UserDetailsService defaultUserDetailsService = this.amBuilder.getDefaultUserDetailsService();
      UserDetails user = defaultUserDetailsService.loadUserByUsername(username);
      return new User(user.getUsername(), user.getPassword(), getAuthorities(user));
    } catch (Exception e) {
      throw new UsernameNotFoundException("Authentication failed, for user:" + username, e);
    }
  }

  /**
   * @param user the {@link UserDetails} from spring-security.
   * @return the associated {@link GrantedAuthority}s
   * @throws AuthenticationException if no principal is retrievable for the given {@code username}
   */
  protected Set<GrantedAuthority> getAuthorities(UserDetails user) throws AuthenticationException {

    // determine granted authorities for spring-security...
    Set<AccessControl> accessControlSet = new HashSet<>();

    Set<String> undefinedIds = getRoles(user).stream()
        .filter(id -> !this.accessControlProvider.collectAccessControls(id, accessControlSet))
        .collect(Collectors.toUnmodifiableSet());

    undefinedIds.forEach(id -> LOG.warn("Undefined access control {}.", id));

    return accessControlSet.stream().map(accessControl -> new AccessControlGrantedAuthority(accessControl))
        .collect(Collectors.toUnmodifiableSet());
  }

  private Collection<String> getRoles(UserDetails user) {

    return user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toUnmodifiableSet());
  }

  /**
   * @return amBuilder
   */
  public AuthenticationManagerBuilder getAmBuilder() {

    return this.amBuilder;
  }

  /**
   * @param amBuilder new value of {@link #getAmBuilder}.
   */
  @Inject
  @javax.inject.Inject
  public void setAmBuilder(AuthenticationManagerBuilder amBuilder) {

    this.amBuilder = amBuilder;
  }

  /**
   * @return accessControlProvider
   */
  public AccessControlProvider getAccessControlProvider() {

    return this.accessControlProvider;
  }

  /**
   * @param accessControlProvider new value of {@link #getAccessControlProvider}.
   */
  @Inject
  @javax.inject.Inject
  public void setAccessControlProvider(AccessControlProvider accessControlProvider) {

    this.accessControlProvider = accessControlProvider;
  }
}
