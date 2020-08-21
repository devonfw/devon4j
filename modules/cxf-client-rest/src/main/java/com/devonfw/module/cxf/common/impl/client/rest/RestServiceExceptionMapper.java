package com.devonfw.module.cxf.common.impl.client.rest;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.jaxrs.client.ResponseExceptionMapper;

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
        URI url = response.getLocation();
        String operation = null;
        String serviceDetails = this.context.getServiceDescription(operation, url.toString());
        return this.errorUnmarshaller.unmarshall(data, mediaType.toString(), response.getStatus(), serviceDetails);
      }
    }
    return null;
  }

}
