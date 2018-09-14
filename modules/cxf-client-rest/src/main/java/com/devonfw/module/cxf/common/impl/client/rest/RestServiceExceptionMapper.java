package com.devonfw.module.cxf.common.impl.client.rest;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import net.sf.mmm.util.exception.api.ServiceInvocationFailedException;

import org.apache.cxf.jaxrs.client.ResponseExceptionMapper;

import com.devonfw.module.service.common.api.constants.ServiceConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * An Implementation of {@link ResponseExceptionMapper} that converts a REST failure {@link Response} compliant with
 * <a href="https://github.com/oasp-forge/oasp4j-wiki/wiki/guide-rest#error-results">OASP REST error specification</a>
 * to a {@link ServiceInvocationFailedException}.
 *
 * @since 3.0.0
 */
@Provider
public class RestServiceExceptionMapper implements ResponseExceptionMapper<Throwable> {

  private String serviceName;

  /**
   * The constructor.
   *
   * @param service the name (e.g. {@link Class#getName() qualified name}) of the
   *        {@link com.devonfw.module.service.common.api.Service} that failed.
   */
  public RestServiceExceptionMapper(String service) {

    super();
    this.serviceName = service;
  }

  @Override
  public Throwable fromResponse(Response response) {

    response.bufferEntity();
    if (response.hasEntity()) {
      String json = response.readEntity(String.class);
      if ((json != null) && !json.isEmpty()) {
        try {
          ObjectMapper objectMapper = new ObjectMapper();
          Map<String, Object> jsonMap = objectMapper.readValue(json, Map.class);
          return createException(jsonMap);
        } catch (IOException e) {
          return new ServiceInvocationFailedTechnicalException(e, e.getMessage(), e.getClass().getSimpleName(), null,
              this.serviceName);
        }
      }
    }
    return null;
  }

  private Throwable createException(Map<String, Object> jsonMap) {

    String code = (String) jsonMap.get(ServiceConstants.KEY_CODE);
    String message = (String) jsonMap.get(ServiceConstants.KEY_MESSAGE);
    String uuid = (String) jsonMap.get(ServiceConstants.KEY_UUID);

    return createException(code, message, UUID.fromString(uuid));
  }

  private Throwable createException(String code, String message, UUID uuid) {

    return new ServiceInvocationFailedException(message, code, uuid, this.serviceName);
  }

  /**
   * Extends {@link ServiceInvocationFailedException} as {@link #isTechnical() technical} exception.
   */
  private static final class ServiceInvocationFailedTechnicalException extends ServiceInvocationFailedException {

    private static final long serialVersionUID = 1L;

    /**
     * The constructor.
     *
     * @param cause the {@link #getCause() cause} of this exception.
     * @param message the {@link #getMessage() message}.
     * @param code the {@link #getCode() code}.
     * @param uuid {@link UUID} the {@link #getUuid() UUID}.
     * @param service the name (e.g. {@link Class#getName() qualified name}) of the service that failed.
     */
    private ServiceInvocationFailedTechnicalException(Throwable cause, String message, String code, UUID uuid,
        String service) {

      super(cause, message, code, uuid, service);
    }

    @Override
    public boolean isForUser() {

      return false;
    }
  }

}
