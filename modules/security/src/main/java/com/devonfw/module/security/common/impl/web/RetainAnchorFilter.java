package com.devonfw.module.security.common.impl.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.web.filter.GenericFilterBean;

/**
 * Spring Security filter that preserves the URL anchor if the authentication process contains redirects (e.g. if the
 * login is performed via CAS or form login).
 *
 * With standard redirects (default Spring Security behavior), Internet Explorer (6.0 and 8.0) discard the anchor part
 * of the URL such that e.g. bookmarking does not work properly. Firefox re-appends the anchor part.
 *
 * This filter replaces redirects to URLs that match a certain pattern (<code>storeUrlPattern</code>) with a Javascript
 * page that stores the URL anchor in a cookie, and replaces redirects to URLs that match another pattern (
 * <code>restoreUrlPattern</code>) with a Javascript page that restores the URL anchor from that cookie. The cookie name
 * can be set via the attribute <code>cookieName</code>.
 *
 * @see <a href=
 *      "http://forum.spring.io/forum/spring-projects/security/94197-preserving-url-anchor-when-redirecting?p=550750#post550750"
 *      >Forum post of guidow08</a>
 * @see <a href=
 *      "http://forum.spring.io/forum/spring-projects/security/94197-preserving-url-anchor-when-redirecting?p=603929#post603929"
 *      >Forum post of mpickell</a>
 */
public class RetainAnchorFilter extends GenericFilterBean {

  private String storeUrlPattern;

  private String restoreUrlPattern;

  private String cookieName;

  /**
   * Sets the url pattern for storing anchors.
   *
   * @param storeUrlPattern url regular expression
   */
  public void setStoreUrlPattern(String storeUrlPattern) {

    this.storeUrlPattern = storeUrlPattern;
  }

  /**
   * Sets the url pattern for restoring anchors.
   *
   * @param restoreUrlPattern url regular expression
   */
  public void setRestoreUrlPattern(String restoreUrlPattern) {

    this.restoreUrlPattern = restoreUrlPattern;
  }

  /**
   * Sets the cookie name in which the anchor data should be saved.
   *
   * @param cookieName name of the cookie
   */
  public void setCookieName(String cookieName) {

    this.cookieName = cookieName;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
      ServletException {

    ServletResponse wrappedResponse = response;
    if (response instanceof HttpServletResponse) {
      wrappedResponse = new RedirectResponseWrapper((HttpServletResponse) response);
    }

    chain.doFilter(request, wrappedResponse);
  }

  /**
   * HttpServletResponseWrapper that replaces the redirect by appropriate Javascript code.
   */
  private class RedirectResponseWrapper extends HttpServletResponseWrapper {

    public RedirectResponseWrapper(HttpServletResponse response) {

      super(response);
    }

    @Override
    public void sendRedirect(String location) throws IOException {

      HttpServletResponse response = (HttpServletResponse) getResponse();
      String redirectPageHtml = "";
      if (location.matches(RetainAnchorFilter.this.storeUrlPattern)) {
        redirectPageHtml = generateStoreAnchorRedirectPageHtml(location);
      } else if (location.matches(RetainAnchorFilter.this.restoreUrlPattern)) {
        redirectPageHtml = generateRestoreAnchorRedirectPageHtml(location);
      } else {
        super.sendRedirect(location);
        return;
      }
      response.setContentType("text/html;charset=UTF-8");
      response.setContentLength(redirectPageHtml.length());
      response.getWriter().write(redirectPageHtml);
    }

    private String generateStoreAnchorRedirectPageHtml(String location) {

      StringBuilder sb = new StringBuilder();

      sb.append("<html><head><title>Redirect Page</title>\n");
      sb.append("<script type=\"text/javascript\">\n");

      // store anchor
      sb.append("document.cookie = '" + RetainAnchorFilter.this.cookieName
          + "=' + window.location.hash + '; path=/';\n");

      // redirect
      sb.append("window.location = '" + location + "' + window.location.hash;\n");
      sb.append("</script>\n</head>\n");
      sb.append("<body><h1>Redirect Page (Store Anchor)</h1>\n");
      sb.append("Should redirect to " + location + "\n");
      sb.append("</body></html>\n");

      return sb.toString();
    }

    /**
         * @see <a
     *      href="http://forum.spring.io/forum/spring-projects/security/94197-preserving-url-anchor-when-redirecting?p=603929#post603929">Forum
     *      post</a>
     */
    private String generateRestoreAnchorRedirectPageHtml(String location) {

      StringBuilder sb = new StringBuilder();

      // open html
      sb.append("<html><head><title>Redirect Page</title>");
      sb.append("<script type='text/javascript'>");

      // Create stored regex
      // //stored regex lookup is a faster than indexOf (see http://jsperf.com/regexp-indexof-perf/30)
      // //This expression matches the token we're looking for, and the 1st group is the value.
      sb.append("var cookieParser = /" + RetainAnchorFilter.this.cookieName + "=([^;]*)(?:;|$)/;");

      // generic Javascript function to get cookie value via regular expression
      sb.append("var getCookie = function() {");
      sb.append("  var m = cookieParser.exec(document.cookie);");
      sb.append("  if (m != null && m.length == 2) {");
      // // m[0] == full match, m[1] == the value for this token
      sb.append("    return unescape(m[1]);");
      sb.append("  } else {");
      sb.append("    return false;");
      sb.append("  }");
      sb.append("};");

      // get anchor from cookie
      sb.append("var targetAnchor = getCookie();");

      // append to URL and redirect
      sb.append("if (targetAnchor) {");
      sb.append("  window.location = '" + location + "' + targetAnchor;");
      sb.append("} else {");
      sb.append("  window.location = '" + location + "';");
      sb.append("}");

      // Remove cookie
      sb.append("document.cookie = '" + RetainAnchorFilter.this.cookieName
          + "=; expires=Thu, 01-Jan-70 00:00:01 GMT; path=/';");

      // close html
      sb.append("</script>");
      sb.append("</head>");
      sb.append("<body>");
      sb.append("<h1>Redirect Page (Restore Anchor)</h1>");
      sb.append("<p>Should redirect to " + location + "</p>");
      sb.append("</body>");
      sb.append("</html>");

      return sb.toString();
    }

  }
}
