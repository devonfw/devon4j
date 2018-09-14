package com.devonfw.module.web.common.base.debug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An {@link HttpServlet} to echo the {@link HttpServletRequest} with headers and payload as HTML output in the
 * {@link HttpServletResponse}. This is very helpful especially if your application is behind a complex infrastructure
 * with reverse-proxies, etc.<br>
 * In order to use this servlet simply add the following to your spring configuration (and adjust "/debug/echo/*" to
 * your needs):
 *
 * <pre>
 * &#64;Bean
 * public ServletRegistrationBean echoServletRegistration() {
 *
 *   HttpEchoServlet echoServlet = new HttpEchoServlet();
 *   ServletRegistrationBean registration = new ServletRegistrationBean(echoServlet);
 *   registration.addUrlMappings("/debug/echo/*");
 *   return registration;
 * }
 * </pre>
 *
 */
public class HttpEchoServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  private static final String SECURED = "****SECURED****";

  private static final String COOKIE_SECURED = "=" + SECURED;

  /**
   * Der Konstruktor.
   */
  public HttpEchoServlet() {
    super();
  }

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String uri = request.getRequestURI();
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.print("<!DOCTYPE html><html><head><title>Debugausgabe von ");
    out.print(uri);
    out.println("</title></head><body><h1>");
    out.print(request.getMethod());
    out.print(' ');
    out.print(uri);
    String query = request.getQueryString();
    if (query != null) {
      out.print('?');
      out.print(query);
    }
    out.println();
    out.println("</h1><h2>Headers:</h2><pre>");
    printHeaders(request, out);
    out.println("</pre><h2>Payload:</h2><pre>");
    BufferedReader in = request.getReader();
    String line = in.readLine();
    if (line == null) {
      out.println("no data");
    }
    while (line != null) {
      out.println(line);
      line = in.readLine();
    }
    out.println("</pre></body></html>");
  }

  private void printHeaders(HttpServletRequest request, PrintWriter out) {

    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String name = headerNames.nextElement();
      Enumeration<String> headerValues = request.getHeaders(name);
      while (headerValues.hasMoreElements()) {
        String value = headerValues.nextElement();
        out.print(name);
        out.print(": ");
        out.print(getSecuredValue(name, value));
        out.println();
      }
    }
  }

  private String getSecuredValue(String originalHeaderName, String value) {

    String nameLowercase = originalHeaderName.toLowerCase(Locale.US);
    if ("authorization".equals(nameLowercase)) {
      return SECURED;
    }
    if ("proxy-authorization".equals(nameLowercase)) {
      return SECURED;
    }
    if ("cookie".equals(nameLowercase)) {
      if (value != null) {
        return value.replaceAll("=[^; ]*", COOKIE_SECURED);
      }
    }
    return value;
  }

}
