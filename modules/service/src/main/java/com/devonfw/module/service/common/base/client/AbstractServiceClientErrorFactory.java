package com.devonfw.module.service.common.base.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.devonfw.module.service.common.api.client.ServiceClientErrorFactory;
import com.devonfw.module.service.common.api.constants.ServiceConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Abstract base implementation of {@link ServiceClientErrorFactory} that converts a REST failure response compliant
 * with
 * <a href="https://github.com/devonfw/devon4j/blob/develop/documentation/guide-rest.asciidoc#error-results">devonfw
 * REST error specification</a>.
 *
 * @since 2020.08.001
 */
public abstract class AbstractServiceClientErrorFactory implements ServiceClientErrorFactory {

  /**
   * The constructor.
   */
  public AbstractServiceClientErrorFactory() {

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
      return create(e, e.getMessage(), e.getClass().getSimpleName(), null, service);
    }
  }

  private RuntimeException createException(Map<String, Object> map, String service) {

    String code = (String) map.get(ServiceConstants.KEY_CODE);
    if (isEmpty(code)) {
      Object status = map.get(ServiceConstants.KEY_STATUS);
      if (status != null) {
        code = status.toString();
      }
    }
    String message = (String) map.get(ServiceConstants.KEY_MESSAGE);
    if (isEmpty(message)) {
      Object error = map.get(ServiceConstants.KEY_ERROR);
      if (error != null) {
        message = error.toString();
      }
    }
    String uuidStr = (String) map.get(ServiceConstants.KEY_UUID);
    UUID uuid = uuidStr != null ? UUID.fromString(uuidStr) : null;
    return create(null, message, code, uuid, service);
  }

  private static boolean isEmpty(String string) {

    if (string == null) {
      return true;
    }
    return string.isBlank();
  }

}
