package ${package}.general.common.base.test;

import com.devonfw.module.test.common.base.ModuleTest;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Class for XML Validation Tests.
 *
 */
public class AccessControlSchemaXmlValidationTest extends ModuleTest {

  /**
   * Tests if the access-control-schema.xml is valid.
   *
   * @throws ParserConfigurationException If a DocumentBuilder cannot be created which satisfies the configuration
   *         requested.
   * @throws IOException If any IO errors occur.
   * @throws SAXException If an error occurs during the validation.
   */
  @Test
  public void validateAccessControllSchema() throws ParserConfigurationException, SAXException, IOException {

    // parse an XML document into a DOM tree
    DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    String xmlPath = getClass().getResource("/config/app/security/access-control-schema.xml").getPath();
    Document document = parser.parse(new File(xmlPath));

    // create a SchemaFactory capable of understanding WXS schemas
    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    // load a WXS schema, represented by a Schema instance
    URL schemaPath = getClass().getResource("/com/devonfw/module/security/access-control-schema.xsd");
    Schema schema = factory.newSchema(schemaPath);

    // create a Validator instance, which can be used to validate an instance document
    Validator validator = schema.newValidator();

    // validate the DOM tree
    validator.validate(new DOMSource(document));
  }
}
