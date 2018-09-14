package com.devonfw.module.security.common.impl.accesscontrol;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devonfw.module.security.common.api.accesscontrol.AccessControlSchema;
import com.devonfw.module.security.common.base.accesscontrol.AccessControlSchemaMapper;

/**
 * This class is a simple wrapper for {@link #read(InputStream) reading} and
 * {@link #write(AccessControlSchema, OutputStream) writing} the {@link AccessControlSchema} from/to XML.
 *
 */
public class AccessControlSchemaXmlMapper implements AccessControlSchemaMapper {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(AccessControlSchemaXmlMapper.class);

  private JAXBContext jaxbContext;

  /**
   * The constructor.
   */
  public AccessControlSchemaXmlMapper() {

    super();
    try {
      this.jaxbContext = JAXBContext.newInstance(AccessControlSchema.class);
    } catch (JAXBException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public void write(AccessControlSchema conf, OutputStream out) {

    try {
      Marshaller marshaller = this.jaxbContext.createMarshaller();
      marshaller.marshal(conf, out);
    } catch (JAXBException e) {
      throw new IllegalStateException("Marshalling XML failed!", e);
    }
  }

  @Override
  public AccessControlSchema read(InputStream in) {

    try {
      Unmarshaller unmarshaller = this.jaxbContext.createUnmarshaller();
      ValidationEventHandler handler = new ValidationEventHandlerImpl();
      unmarshaller.setEventHandler(handler);
      return (AccessControlSchema) unmarshaller.unmarshal(in);
    } catch (JAXBException e) {
      throw new IllegalStateException("Unmarshalling XML failed!", e);
    }
  }

  /**
   * Generates the XSD (XML Schema Definition) to the given {@link File}.
   *
   * @param outFile is the {@link File} to write to.
   */
  public void writeXsd(File outFile) {

    File folder = outFile.getParentFile();
    if (!folder.isDirectory()) {
      boolean success = folder.mkdirs();
      if (!success) {
        throw new IllegalStateException("Failed to create folder " + folder);
      }
    }
    try (FileOutputStream fos = new FileOutputStream(outFile)) {
      writeXsd(fos);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to generate and write the XSD schema to " + outFile + "!", e);
    }
  }

  /**
   * Generates the XSD (XML Schema Definition) to the given {@link OutputStream}.
   *
   * @param out is the {@link OutputStream} to write to.
   */
  public void writeXsd(final OutputStream out) {

    SchemaOutputResolver sor = new SchemaOutputResolver() {

      @Override
      public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {

        StreamResult streamResult = new StreamResult(out);
        streamResult.setSystemId(suggestedFileName);
        return streamResult;
      }

    };
    try {
      this.jaxbContext.generateSchema(sor);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to generate and write the XSD schema!", e);
    }
  }

  /**
   * Custom implementation of {@link ValidationEventHandler}.
   */
  protected static class ValidationEventHandlerImpl implements ValidationEventHandler {

    /**
     * The constructor.
     */
    public ValidationEventHandlerImpl() {

      super();
    }

    @Override
    public boolean handleEvent(ValidationEvent event) {

      if (event != null) {
        switch (event.getSeverity()) {
        case ValidationEvent.ERROR:
        case ValidationEvent.FATAL_ERROR:
          throw new IllegalArgumentException(event.toString());
        case ValidationEvent.WARNING:
          LOG.warn(event.toString());
          break;
        default:
          LOG.debug(event.toString());
        }
      }
      return true;
    }
  }

}
