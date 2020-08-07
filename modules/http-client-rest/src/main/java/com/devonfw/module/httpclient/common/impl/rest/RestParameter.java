package com.devonfw.module.httpclient.common.impl.rest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Parameter of a REST call.
 *
 * @since 2020.08.001
 */
public class RestParameter {

  private final String name;

  final int index;

  private final boolean query;

  RestParameter(String name, int index, boolean query) {

    super();
    this.name = name;
    this.index = index;
    this.query = query;
  }

  boolean format(Object value, StringBuilder pathBuilder, boolean queryStarted) {

    if (value == null) {
      if (this.query) {
        return queryStarted;
      }
      throw new IllegalArgumentException("@PathParam {" + this.name + "} can not be null!");
    }
    String valueAsString = escape(value.toString());
    if (this.query) {
      if (queryStarted) {
        pathBuilder.append('&');
      } else {
        pathBuilder.append('?');
        queryStarted = true;
      }
      pathBuilder.append(this.name);
      pathBuilder.append('=');
    }
    pathBuilder.append(valueAsString);
    return queryStarted;
  }

  private String escape(String string) {

    return URLEncoder.encode(string, StandardCharsets.UTF_8);
  }

  @Override
  public String toString() {

    return "{" + this.name + "}@" + this.index;
  }
}