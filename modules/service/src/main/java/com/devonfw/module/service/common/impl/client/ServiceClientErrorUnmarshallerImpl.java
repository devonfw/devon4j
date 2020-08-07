package com.devonfw.module.service.common.impl.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.mmm.util.exception.api.ServiceInvocationFailedException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.devonfw.module.service.common.api.client.ServiceClientErrorUnmarshaller;
import com.devonfw.module.service.common.api.constants.ServiceConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * An Implementation of {@link ServiceClientErrorUnmarshaller} that converts a REST failure response compliant with
 * <a href="https://github.com/devonfw/devon4j/blob/develop/documentation/guide-rest.asciidoc#error-results">devonfw
 * REST error specification</a> to a {@link ServiceInvocationFailedException}.
 *
 * @since 2020.08.001
 */
public class ServiceClientErrorUnmarshallerImpl implements ServiceClientErrorUnmarshaller {

  /**
   * The constructor.
   */
  public ServiceClientErrorUnmarshallerImpl() {

    super();
  }

  @Override
  public RuntimeException unmarshall(String data, String format, int statusCode, String service) {

    try {
      Map<String, Object> map;
      if ("application/json".equals(format)) {
        ObjectMapper objectMapper = new ObjectMapper();
        map = objectMapper.readValue(data, Map.class);
      } else if ("text/xml".equals(format) || "application/xml".equals(format)) {
        InputStream in = new ByteArrayInputStream(data.getBytes());
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
        Element root = document.getDocumentElement();
        map = new HashMap<>();
        NodeList childNodes = root.getChildNodes();
        int length = childNodes.getLength();
        for (int i = 0; i < length; i++) {
          Node node = childNodes.item(i);
          if (node instanceof Element) {
            Element element = (Element) node;
            String key = element.getTagName();
            String value = element.getTextContent();
            map.put(key, value);
          }
        }
      } else {
        throw new IllegalStateException("Unknown format " + format + ": " + data);
      }
      return createException(map, service);
    } catch (Exception e) {
      return new ServiceInvocationFailedTechnicalException(e, e.getMessage(), e.getClass().getSimpleName(), null,
          service);
    }
  }

  private RuntimeException createException(Map<String, Object> map, String service) {

    String code = (String) map.get(ServiceConstants.KEY_CODE);
    String message = (String) map.get(ServiceConstants.KEY_MESSAGE);
    String uuidStr = (String) map.get(ServiceConstants.KEY_UUID);
    UUID uuid = uuidStr != null ? UUID.fromString(uuidStr) : null;
    return createException(code, message, uuid, service);
  }

  private RuntimeException createException(String code, String message, UUID uuid, String service) {

    return new ServiceInvocationFailedException(message, code, uuid, service);
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
