package com.devonfw.module.security.common.impl.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * Processes authentication where credentials are sent as a JSON object.
 * <p/>
 * <p>
 * The JSON object must contain two properties: a username and a password. The default properties' names to use are
 * contained in the static fields {@link UsernamePasswordAuthenticationFilter#SPRING_SECURITY_FORM_USERNAME_KEY} and
 * {@link UsernamePasswordAuthenticationFilter#SPRING_SECURITY_FORM_PASSWORD_KEY}. The JSON object properties' names can
 * also be changed by setting the {@code usernameParameter} and {@code passwordParameter} properties. Assuming the
 * default properties' names were not changed, if the credentials <code>user</code>/<code>pass</code> are to be sent,
 * the following JSON object is expected:
 *
 * <pre>
 * <code>
 *     {
 *        "j_username": "user",
 *        "j_password": "pass",
 *    }
 * </code>
 * </pre>
 * <p/>
 * <p>
 * The URL this filter responds to is passed as a constructor parameter.
 * <p/>
 * <p>
 * This authentication filter is intended for One Page Applications which handle a login page/dialog/pop-up on their
 * own. This filter combined with:
 * <ul>
 * <li>{@link AuthenticationSuccessHandlerSendingOkHttpStatusCode}</li>
 * <li>{@link org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler} created using the
 * default constructor (thus leaving the {@code defaultFailureUrl} unset)</li>
 * <li>{@link LogoutSuccessHandlerReturningOkHttpStatusCode}</li>
 * </ul>
 * makes the login/logout API fully RESTful.
 * </p>
 *
 */
public class JsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
  private String usernameParameter = UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

  private String passwordParameter = UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;

  private boolean postOnly = true;

  // REVIEW may-bee (hohwille) We have a centralized and custom-configured object mapper as spring bean. IMHO we should
  // inject that instance here.
  private ObjectMapper objectMapper = new ObjectMapper();

  /**
   * The constructor.
   *
   * @param requiresAuthenticationRequestMatcher the {@link RequestMatcher} used to determine if authentication is
   *        required. Cannot be null.
   */
  public JsonUsernamePasswordAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {

    super(requiresAuthenticationRequestMatcher);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException, ServletException {

    if (this.postOnly && !request.getMethod().equals("POST")) {
      throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
    }

    final UsernameAndPasswordParser usernameAndPasswordParser = new UsernameAndPasswordParser(request);
    usernameAndPasswordParser.parse();
    UsernamePasswordAuthenticationToken authRequest =
        new UsernamePasswordAuthenticationToken(usernameAndPasswordParser.getTrimmedUsername(),
            usernameAndPasswordParser.getPassword());
    // authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    return getAuthenticationManager().authenticate(authRequest);
  }

  /**
   * @return Value of usernameParameter
   */
  public String getUsernameParameter() {

    return this.usernameParameter;
  }

  /**
   * @param usernameParameter new value for usernameParameter
   */
  public void setUsernameParameter(String usernameParameter) {

    this.usernameParameter = usernameParameter;
  }

  /**
   * @return Value of passwordParameter
   */
  public String getPasswordParameter() {

    return this.passwordParameter;
  }

  /**
   * @param passwordParameter new value for passwordParameter
   */
  public void setPasswordParameter(String passwordParameter) {

    this.passwordParameter = passwordParameter;
  }

  /**
   * @return value of postOnly
   */
  public boolean isPostOnly() {

    return this.postOnly;
  }

  /**
   * @param postOnly new value for postOnly
   */
  public void setPostOnly(boolean postOnly) {

    this.postOnly = postOnly;
  }

  private class UsernameAndPasswordParser {
    private String username;

    private String password;

    private final HttpServletRequest request;

    private JsonNode credentialsNode;

    private UsernameAndPasswordParser(HttpServletRequest request) {

      this.request = request;
    }

    public void parse() {

      parseJsonFromRequestBody();
      if (jsonParsedSuccessfully()) {
        extractUsername();
        extractPassword();
      }
    }

    private void extractPassword() {

      this.password = extractValueByName(JsonUsernamePasswordAuthenticationFilter.this.passwordParameter);
    }

    private void extractUsername() {

      this.username = extractValueByName(JsonUsernamePasswordAuthenticationFilter.this.usernameParameter);
    }

    private String extractValueByName(String name) {

      String value = null;
      if (this.credentialsNode.has(name)) {
        JsonNode node = this.credentialsNode.get(name);
        if (node != null) {
          value = node.asText();
        }
      }
      return value;
    }

    private boolean jsonParsedSuccessfully() {

      return this.credentialsNode != null;
    }

    private void parseJsonFromRequestBody() {

      try {
        final ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest(this.request);
        this.credentialsNode =
            JsonUsernamePasswordAuthenticationFilter.this.objectMapper.readTree(servletServerHttpRequest.getBody());
      } catch (IOException e) {
        // ignoring
      }
    }

    private String getTrimmedUsername() {

      return this.username == null ? "" : this.username.trim();
    }

    private String getPassword() {

      return this.password == null ? "" : this.password;
    }

  }
}
