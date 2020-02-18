package com.devonfw.module.security.common.impl.accesscontrol;

import java.io.File;

/**
 * This is a simple programm to generate (create or update) the XSD for the
 * {@link com.devonfw.module.security.common.api.accesscontrol.AccessControlSchema}.
 */
public class AccessControlSchemaXsdWriter extends AccessControlSchemaXmlMapper {

  /**
   * The constructor.
   */
  public AccessControlSchemaXsdWriter() {

    super();
  }

  /**
   * The main method to launch this program.
   *
   * @param args the command-line arguments (will be ignored).
   */
  public static void main(String[] args) {

    AccessControlSchemaXmlMapper mapper = new AccessControlSchemaXmlMapper();
    mapper.writeXsd(new File("src/main/resources/com/devonfw/module/security/access-control-schema.xsd"));
  }

}
