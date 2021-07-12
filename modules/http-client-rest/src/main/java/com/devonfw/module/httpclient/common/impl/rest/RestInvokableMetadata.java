package com.devonfw.module.httpclient.common.impl.rest;

import java.lang.reflect.Method;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

/**
 * Base class for metadata of a REST service.
 *
 * @since 2021.04.003
 */
public abstract class RestInvokableMetadata extends RestMetadata {

  private final String consumes;

  private final String produces;

  /**
   * The constructor.
   *
   * @param consumes the {@link #getConsumes() consuming type}.
   * @param produces the {@link #getProduces() producing type}.
   */
  public RestInvokableMetadata(String consumes, String produces) {

    super();
    this.consumes = consumes;
    this.produces = produces;
  }

  /**
   * @return the {@link javax.ws.rs.core.MediaType} that is consumed (content-type of HTTP request).
   */
  public String getConsumes() {

    return this.consumes;
  }

  /**
   * @return the {@link javax.ws.rs.core.MediaType} that is produced (content-type of HTTP response).
   */
  public String getProduces() {

    return this.produces;
  }

  /**
   * @param api the {@link Class} of the REST service API.
   * @return the {@link #getConsumes() consuming type}.
   */
  protected static String findConsumes(Class<?> api) {

    Consumes consumes = findAnnotation(api, Consumes.class);
    return findConsumes(consumes, api.getName());
  }

  /**
   * @param serviceMetadata the {@link RestServiceMetadata}.
   * @param method the Java {@link Method} of the REST service operation.
   * @return the {@link #getConsumes() consuming type}.
   */
  protected static String findConsumes(RestServiceMetadata<?> serviceMetadata, Method method) {

    Consumes consumes = findAnnotation(method, Consumes.class, false);
    if (consumes != null) {
      return findConsumes(consumes, serviceMetadata.getContext().getApi().getName() + "." + method.getName());
    }
    return serviceMetadata.getConsumes();
  }

  /**
   * @param consumes the {@link Consumes} annotation of the REST service API.
   * @param service the name of the service.
   * @return the {@link #getConsumes() consuming type}.
   */
  protected static String findConsumes(Consumes consumes, String service) {

    String[] mediaTypes = null;
    if (consumes != null) {
      mediaTypes = consumes.value();
    }
    return getMediaType(mediaTypes, service);
  }

  /**
   * @param api the {@link Class} of the REST service API.
   * @return the {@link #getProduces() producing type}.
   */
  protected static String findProduces(Class<?> api) {

    Produces produces = findAnnotation(api, Produces.class);
    return findProduces(produces, api.getName());
  }

  /**
   * @param serviceMetadata the {@link RestServiceMetadata}.
   * @param method the Java {@link Method} of the REST service operation.
   * @return the {@link #getConsumes() consuming type}.
   */
  protected static String findProduces(RestServiceMetadata<?> serviceMetadata, Method method) {

    Produces produces = findAnnotation(method, Produces.class, false);
    if (produces != null) {
      return findProduces(produces, serviceMetadata.getContext().getApi().getName() + "." + method.getName());
    }
    return serviceMetadata.getProduces();
  }

  /**
   * @param produces the {@link Produces} annotation of the REST service API.
   * @param service the name of the service.
   * @return the {@link #getConsumes() consuming type}.
   */
  protected static String findProduces(Produces produces, String service) {

    String[] mediaTypes = null;
    if (produces != null) {
      mediaTypes = produces.value();
    }
    return getMediaType(mediaTypes, service);
  }

}
