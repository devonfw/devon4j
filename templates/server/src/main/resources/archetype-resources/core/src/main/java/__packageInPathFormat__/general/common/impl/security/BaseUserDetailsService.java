package ${package}.general.common.impl.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.devonfw.module.security.common.api.accesscontrol.AccessControl;
import com.devonfw.module.security.common.api.accesscontrol.AccessControlProvider;
import com.devonfw.module.security.common.base.accesscontrol.AccessControlGrantedAuthority;

/**
 * Custom implementation of {@link UserDetailsService}.<br>
 *
 * @see ${package}.general.service.impl.config.BaseWebSecurityConfig
 */
@Named
public class BaseUserDetailsService implements UserDetailsService {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(BaseUserDetailsService.class);

  private AuthenticationManagerBuilder amBuilder;

  private AccessControlProvider accessControlProvider;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Set<GrantedAuthority> authorities = getAuthorities(username);
    UserDetails user;
    try {
      user = getAmBuilder().getDefaultUserDetailsService().loadUserByUsername(username);
      User userData = new User(user.getUsername(), user.getPassword(), authorities);
      return userData;
    } catch (Exception e) {
      e.printStackTrace();
      UsernameNotFoundException exception = new UsernameNotFoundException("Authentication failed.", e);
      LOG.warn("Failed to get user {}.", username, exception);
      throw exception;
    }
  }

  /**
   * @param username the login of the user
   * @return the associated {@link GrantedAuthority}s
   * @throws AuthenticationException if no principal is retrievable for the given {@code username}
   */
  protected Set<GrantedAuthority> getAuthorities(String username) throws AuthenticationException {

    Objects.requireNonNull(username, "username");
    // determine granted authorities for spring-security...
    Set<GrantedAuthority> authorities = new HashSet<>();
    Collection<String> accessControlIds = getRoles(username);
    Set<AccessControl> accessControlSet = new HashSet<>();
    for (String id : accessControlIds) {
      boolean success = this.accessControlProvider.collectAccessControls(id, accessControlSet);
      if (!success) {
        LOG.warn("Undefined access control {}.", id);
      }
    }
    for (AccessControl accessControl : accessControlSet) {
      authorities.add(new AccessControlGrantedAuthority(accessControl));
    }
    return authorities;
  }

  /**
   * Lists roles of AWS from IAM profile of pointed user.
   *
   * @param userName        given userName in AWS
   * @param showErrorOutput flag marking outputing errors to console
   * @return collection with user's roles
   */
  public static Collection<String> obtainRoles(String userName, boolean showErrorOutput) {
    Collection<String> roles = new ArrayList<String>();

    final String baseCommand = "aws iam list-groups-for-user --user-name";
    String command = String.format("%s %s", baseCommand, userName);

    BufferedReader reader = null;
    Process process;
    try {
      process = Runtime.getRuntime().exec(command);
      process.waitFor();
      reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        String theLine = line.trim();
        if (theLine.startsWith("\"GroupName\": ")) {
          String roleName = theLine.substring(14, theLine.length() - 1);
          roles.add(roleName);
        }
      }
    } catch (Exception exc_1) {
      if (showErrorOutput) {
        exc_1.printStackTrace();
      }
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception exc_2) {
          if (showErrorOutput) {
            System.err.println("Couldn't even close the BufferedReader.");
          }
        }
      }
    }

    return roles;
  }

  /**
   * Return list of IAM roles.
   *
   * @param username pointed user's name
   * @return obtained roles
   */
  private Collection<String> getRoles(String username) {

    return this.obtainRoles(username);
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
  public void setAccessControlProvider(AccessControlProvider accessControlProvider) {

    this.accessControlProvider = accessControlProvider;
  }
}
