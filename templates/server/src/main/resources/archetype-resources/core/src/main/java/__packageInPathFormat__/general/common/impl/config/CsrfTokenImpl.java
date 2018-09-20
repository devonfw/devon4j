package ${package}.general.common.impl.config;

import org.springframework.security.web.csrf.CsrfToken;

/**
 * Implementation of {@link CsrfToken} as Java bean for JSON deserialization.
 */
public class CsrfTokenImpl implements CsrfToken {

  private static final long serialVersionUID = 1L;

  private String headerName;

  private String parameterName;

  private String token;

  @Override
  public String getHeaderName() {

    return this.headerName;
  }

  @Override
  public String getParameterName() {

    return this.parameterName;
  }

  @Override
  public String getToken() {

    return this.token;
  }

  /**
   * @param headerName new value of {@link #getHeaderName()}.
   */
  public void setHeaderName(String headerName) {

    this.headerName = headerName;
  }

  /**
   * @param parameterName new value of {@link #getParameterName()}.
   */
  public void setParameterName(String parameterName) {

    this.parameterName = parameterName;
  }

  /**
   * @param token new value of {@link #getToken()}.
   */
  public void setToken(String token) {

    this.token = token;
  }

}
