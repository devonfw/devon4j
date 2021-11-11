package com.devonfw.module.httpclient.common.impl.rest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for metadata of a REST service.
 *
 * @since 2020.08.001
 */
public abstract class RestMetadata {

  private static final Logger LOG = LoggerFactory.getLogger(RestMetadata.class);

  static String getMediaType(String[] mediaTypes, String service) {

    if ((mediaTypes == null) || (mediaTypes.length == 0)) {
      return MediaType.APPLICATION_JSON;
    } else {
      if (mediaTypes.length > 1) {
        LOG.warn("Service {} is annotated with multiple media-types. Taking the first one of {}.", service, mediaTypes);
      }
      return mediaTypes[0];
    }

  }

  static void requireJson(String[] mediaTypes) {

    for (String mediaType : mediaTypes) {
      if (MediaType.APPLICATION_JSON.equals(mediaType)) {
        return;
      }
    }
    throw new UnsupportedOperationException("Currently only JSON is supported an not " + Arrays.toString(mediaTypes));
  }

  static <A extends Annotation> A findAnnotation(Class<?> type, Class<A> annotationType, boolean required) {

    A annotation = findAnnotation(type, annotationType);
    if ((annotation == null) && required) {
      throw new IllegalStateException(
          "Missing required annotation " + annotationType.getSimpleName() + " on type " + type.getName());
    }
    return annotation;
  }

  static <A extends Annotation> A findAnnotation(Class<?> type, Class<A> annotationType) {

    A annotation = type.getAnnotation(annotationType);
    if (annotation != null) {
      return annotation;
    }
    for (Class<?> parent : type.getInterfaces()) {
      annotation = findAnnotation(parent, annotationType);
      if (annotation != null) {
        return annotation;
      }
    }
    return null;
  }

  static <A extends Annotation> A findAnnotation(Method method, Class<A> annotationType, boolean required) {

    A annotation = method.getAnnotation(annotationType);
    if (annotation != null) {
      return annotation;
    }
    // inherit via method.getDeclaringClass()?
    if (required) {
      throw new IllegalStateException(
          "Missing required annotation " + annotationType.getSimpleName() + " on method " + method);
    }
    return null;
  }

  static <A extends Annotation> A findAnnotation(Parameter parameter, int parameterIndex, Class<A> annotationType,
      boolean required) {

    A annotation = parameter.getAnnotation(annotationType);
    if (annotation != null) {
      return annotation;
    }
    // inherit via method.getDeclaringClass()?
    if (required) {
      throw new IllegalStateException(
          "Missing required annotation " + annotationType.getSimpleName() + " on parameter at index " + parameterIndex
              + " with type " + parameter.getType().getSimpleName() + " on " + parameter.getDeclaringExecutable());
    }
    return null;
  }

}
