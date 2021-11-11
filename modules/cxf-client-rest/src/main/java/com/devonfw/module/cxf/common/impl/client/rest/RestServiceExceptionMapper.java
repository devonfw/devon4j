package com.devonfw.module.cxf.common.impl.client.rest;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.jaxrs.client.ResponseExceptionMapper;
import org.apache.cxf.jaxrs.impl.ResponseImpl;
import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.ws.addressing.AttributedURIType;
import org.apache.cxf.ws.addressing.EndpointReferenceType;

import com.devonfw.module.service.common.api.client.ServiceClientErrorFactory;
import com.devonfw.module.service.common.api.client.context.ServiceContext;

/**
 * An Implementation of {@link ResponseExceptionMapper} that converts a failure response back to a {@link Throwable}
 * using {@link ServiceClientErrorFactory}.
 *
 * @since 3.0.0
 */
@Provider
public class RestServiceExceptionMapper implements ResponseExceptionMapper<Throwable> {

  private final ServiceClientErrorFactory errorUnmarshaller;

  private final ServiceContext<?> context;

  /**
   * The constructor.
   *
   * @param errorUnmarshaller the {@link ServiceClientErrorFactory}.
   * @param context the {@link ServiceContext}.
   */
  public RestServiceExceptionMapper(ServiceClientErrorFactory errorUnmarshaller, ServiceContext<?> context) {

    super();
    this.errorUnmarshaller = errorUnmarshaller;
    this.context = context;
  }

  @Override
  public Throwable fromResponse(Response response) {

    response.bufferEntity();
    if (response.hasEntity()) {
      String data = response.readEntity(String.class);
      if ((data != null) && !data.isEmpty()) {
        MediaType mediaType = response.getMediaType();
        String url = getUrl(response);
        String operation = getOperation(response);
        String serviceDetails = this.context.getServiceDescription(operation, url);
        return this.errorUnmarshaller.unmarshall(data, mediaType.toString(), response.getStatus(), serviceDetails);
      }
    }
    return null;
  }

  private String getOperation(Response response) {

    if (response instanceof ResponseImpl) {
      Message message = ((ResponseImpl) response).getOutMessage();
      if (message != null) {
        Object invocationContext = message.get(Message.INVOCATION_CONTEXT);
        Object requestContext = getFromMap(invocationContext, "RequestContext");
        OperationResourceInfo operation = getFromMap(requestContext, OperationResourceInfo.class);
        if (operation != null) {
          Method method = operation.getAnnotatedMethod();
          if (method != null) {
            return method.getName();
          }
        }
      }
    }
    return "";
  }

  @SuppressWarnings("rawtypes")
  private static Object getFromMap(Object map, Object key) {

    if (map instanceof Map) {
      return ((Map) map).get(key);
    }
    return null;
  }

  @SuppressWarnings("rawtypes")
  private static <T> T getFromMap(Object map, Class<T> key) {

    if (map instanceof Map) {
      Object value = ((Map) map).get(key.getName());
      if (value != null) {
        try {
          return key.cast(value);
        } catch (Exception e) {
        }
      }
    }
    return null;
  }

  private String getUrl(Response response) {

    URI url = response.getLocation();
    if (url != null) {
      return url.toString();
    } else if (response instanceof ResponseImpl) {
      Message message = ((ResponseImpl) response).getOutMessage();
      if (message != null) {
        Object uri = message.get(Message.REQUEST_URI);
        if (uri instanceof String) {
          return (String) uri;
        }
        Conduit conduit = message.get(Conduit.class);
        if (conduit != null) {
          EndpointReferenceType target = conduit.getTarget();
          if (target != null) {
            AttributedURIType address = target.getAddress();
            if (address != null) {
              return address.getValue();
            }
          }
        }
      }
    }
    return "";
  }

}
