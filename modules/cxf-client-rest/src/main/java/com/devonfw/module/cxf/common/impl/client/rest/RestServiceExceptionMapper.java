package com.devonfw.module.cxf.common.impl.client.rest;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.jaxrs.client.ResponseExceptionMapper;

import com.devonfw.module.service.common.api.client.ServiceClientErrorUnmarshaller;

/**
 * An Implementation of {@link ResponseExceptionMapper} that converts a failure response back to a {@link Throwable}
 * using {@link ServiceClientErrorUnmarshaller}.
 *
 * @since 3.0.0
 */
@Provider
public class RestServiceExceptionMapper implements ResponseExceptionMapper<Throwable> {

  private final ServiceClientErrorUnmarshaller errorUnmarshaller;

  private final String service;

  /**
   * The constructor.
   *
   * @param errorUnmarshaller the {@link ServiceClientErrorUnmarshaller}.
   * @param service the name (e.g. {@link Class#getName() qualified name}) of the
   *        {@link com.devonfw.module.service.common.api.Service} that failed.
   */
  public RestServiceExceptionMapper(ServiceClientErrorUnmarshaller errorUnmarshaller, String service) {

    super();
    this.errorUnmarshaller = errorUnmarshaller;
    this.service = service;
  }

  @Override
  public Throwable fromResponse(Response response) {

    response.bufferEntity();
    if (response.hasEntity()) {
      String data = response.readEntity(String.class);
      if ((data != null) && !data.isEmpty()) {
        MediaType mediaType = response.getMediaType();
        String serviceDetails = this.service;
        URI url = response.getLocation();
        if (url != null) {
          serviceDetails = serviceDetails + "(" + url + ")";
        }
        return this.errorUnmarshaller.unmarshall(data, mediaType.toString(), response.getStatus(), serviceDetails);
      }
    }
    return null;
  }

}
