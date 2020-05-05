package com.devonfw.module.security.common.impl.accesscontrol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import com.devonfw.module.security.common.api.accesscontrol.AccessControl;
import com.devonfw.module.security.common.api.accesscontrol.AccessControlGroup;
import com.devonfw.module.security.common.api.accesscontrol.AccessControlPermission;
import com.devonfw.module.security.common.api.accesscontrol.AccessControlProvider;
import com.devonfw.module.security.common.api.accesscontrol.AccessControlSchema;
import com.devonfw.module.test.common.base.ModuleTest;

/**
 * This is the test-case for {@link AccessControlSchema} and {@link AccessControlSchemaXmlMapper}.
 *
 */
public class AccessControlSchemaTest extends ModuleTest {

  /** The location of the reference configuration for regression tests. */
  private static final String SCHEMA_XML = "config/app/security/access-control-schema.xml";

  /** The location of the reference configuration with group type declaration */
  private static final String SCHEMA_XML_GROUP_TYPES = "config/app/security/access-control-schema_groupTypes.xml";

  /** The location of the configuration with a cyclic dependency. */
  private static final String SCHEMA_XML_CYCLIC = "config/app/security/access-control-schema_cyclic.xml";

  /** The location of the configuration that is syntactically corrupted (invalid group reference). */
  private static final String SCHEMA_XML_CORRUPTED = "config/app/security/access-control-schema_corrupted.xml";

  /** The location of the configuration that is syntactically corrupted (invalid group reference). */
  private static final String SCHEMA_XML_ILLEGAL = "config/app/security/access-control-schema_illegal.xml";

  /**
   * The constructor.
   */
  public AccessControlSchemaTest() {

    super();
    // Assure error message, we are testing for are in English.
    Locale.setDefault(Locale.ENGLISH);
  }

  /**
   * Regression test for {@link AccessControlSchemaXmlMapper#write(AccessControlSchema, java.io.OutputStream)}.
   *
   * @throws Exception if something goes wrong.
   */
  @Test
  public void testWriteXml() throws Exception {

    // given
    AccessControlSchema conf = createSecurityConfiguration();
    String expectedXml = readSecurityConfigurationXmlFile();
    // when
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    new AccessControlSchemaXmlMapper().write(conf, baos);
    String actualXml = baos.toString();
    // then
    assertThat(expectedXml.replaceAll("\\r *|\\n *", "")).isEqualTo(actualXml);
  }

  /**
   * Regression test for {@link AccessControlSchemaXmlMapper#read(InputStream)}.
   *
   * @throws Exception if something goes wrong.
   */
  @Test
  public void testReadXml() throws Exception {

    // given
    AccessControlSchema expectedConf = createSecurityConfiguration();
    // when
    ClassPathResource resource = new ClassPathResource(SCHEMA_XML);
    AccessControlSchema actualConf;
    try (InputStream in = resource.getInputStream()) {
      actualConf = new AccessControlSchemaXmlMapper().read(in);
    }
    // then
    assertThat(expectedConf).isEqualTo(actualConf);
  }

  /**
   * Tests that {@link AccessControlProviderImpl} properly detects cyclic inheritance of {@link AccessControlGroup}s.
   */
  @Test
  public void testProviderValid() {

    createProvider(SCHEMA_XML);
  }

  /**
   * Tests that {@link AccessControlProviderImpl} properly detects cyclic inheritance of {@link AccessControlGroup}s.
   */
  @Test
  public void testProviderCyclic() {

    try {
      createProvider(SCHEMA_XML_CYCLIC);
      fail("Exception expected!");
    } catch (Exception e) {
      assertThat(e).hasMessageContaining("User3-->Admin-->User2");
    }
  }

  /** Tests that {@link AccessControlProviderImpl} with corrupted XML (not well-formed). */
  @Test
  public void testProviderCorrupted() {

    try {
      createProvider(SCHEMA_XML_CORRUPTED);
      fail("Exception expected!");
    } catch (IllegalStateException e) {
      String message = e.getMessage();
      assertThat(message).contains(SCHEMA_XML_CORRUPTED.toString());
      String causeMessage = e.getCause().getMessage();
      assertThat("Unmarshalling XML failed!").isEqualToIgnoringCase(causeMessage);
    }
  }

  /** Tests that {@link AccessControlProviderImpl} with illegal XML (undefined group reference). */
  @Test
  public void testProviderIllegal() {

    try {
      createProvider(SCHEMA_XML_ILLEGAL);
      fail("Exception expected!");
    } catch (IllegalStateException e) {
      String message = e.getMessage();
      assertThat(message).contains(SCHEMA_XML_ILLEGAL.toString());
      String causeMessage = e.getCause().getMessage();
      assertThat(causeMessage).contains("Undefined ID \"User1\"");
    }
  }

  /**
   * Tests that {@link AccessControlProviderImpl} properly detects cyclic inheritance of {@link AccessControlGroup}s.
   */
  public void testProvider() {

    AccessControlProvider provider = createProvider(SCHEMA_XML);
    Set<AccessControl> permissions = new HashSet<>();
    boolean success;
    success = provider.collectAccessControls("", permissions);
    assertThat(success).isFalse();
    assertThat(permissions.size()).isEqualTo(0);
    success = provider.collectAccessControls("Admin", permissions);
    assertThat(success).isTrue();
    assertThat(permissions).contains(provider.getAccessControl("Customer_ReadCustomer"));
    assertThat(permissions).contains(provider.getAccessControl("Customer_CreateCustomer"));
    assertThat(permissions).contains(provider.getAccessControl("Customer_DeleteCustomer"));
    assertThat(permissions.size()).isEqualTo(24);
    success = provider.collectAccessControls("ReadOnly", permissions);
    assertThat(success).isTrue();
    assertThat(permissions).contains(provider.getAccessControl("Contract_ReadContractAsset"));
    assertThat(permissions).contains(provider.getAccessControl("Contract_UpdateContractAsset"));
    assertThat(permissions).doesNotContain(provider.getAccessControl("System_DeleteUser"));
    assertThat(permissions.size()).isEqualTo(5);

  }

  /** Tests the correct extraction of group types */
  @Test
  public void testGroupTypes() {

    ClassPathResource resource = new ClassPathResource(SCHEMA_XML_GROUP_TYPES);
    AccessControlSchemaProviderImpl accessControlSchemaProvider = new AccessControlSchemaProviderImpl();
    accessControlSchemaProvider.setAccessControlSchema(resource);
    AccessControlSchema accessControlSchema = accessControlSchemaProvider.loadSchema();
    List<AccessControlGroup> groups = accessControlSchema.getGroups();

    assertThat(groups).isNotNull();
    assertThat(groups).hasSize(3);

    for (AccessControlGroup group : groups) {
      if (group.getId().equals("Admin")) {
        assertThat(group.getType()).isEqualTo("role");
      } else if (group.getId().equals("ReadOnly") || group.getId().equals("ReadWrite")) {
        assertThat(group.getType()).isEqualTo("group");
      }
    }
  }

  private AccessControlProvider createProvider(String location) {

    ClassPathResource resource = new ClassPathResource(location);
    AccessControlProviderImpl accessControlProvider = new AccessControlProviderImpl();
    AccessControlSchemaProviderImpl accessControlSchemaProvider = new AccessControlSchemaProviderImpl();
    accessControlSchemaProvider.setAccessControlSchema(resource);
    accessControlProvider.setAccessControlSchemaProvider(accessControlSchemaProvider);
    accessControlProvider.initialize();
    return accessControlProvider;
  }

  private String readSecurityConfigurationXmlFile() throws IOException, UnsupportedEncodingException {

    ClassPathResource resource = new ClassPathResource(SCHEMA_XML);
    byte[] data = Files.readAllBytes(Paths.get(resource.getURI()));
    String expectedXml = new String(data, "UTF-8");
    return expectedXml;
  }

  private AccessControlSchema createSecurityConfiguration() {

    AccessControlSchema conf = new AccessControlSchema();
    AccessControlGroup readOnly = new AccessControlGroup("ReadOnly");
    readOnly.getPermissions().add(new AccessControlPermission("Customer_ReadCustomer"));
    readOnly.getPermissions().add(new AccessControlPermission("Customer_ReadProfile"));
    readOnly.getPermissions().add(new AccessControlPermission("Customer_ReadAddress"));
    readOnly.getPermissions().add(new AccessControlPermission("Contract_ReadContract"));
    readOnly.getPermissions().add(new AccessControlPermission("Contract_ReadContractAsset"));
    AccessControlGroup readWrite = new AccessControlGroup("ReadWrite");
    readWrite.getInherits().add(readOnly);
    readWrite.getPermissions().add(new AccessControlPermission("Customer_CreateCustomer"));
    readWrite.getPermissions().add(new AccessControlPermission("Customer_CreateProfile"));
    readWrite.getPermissions().add(new AccessControlPermission("Customer_CreateAddress"));
    readWrite.getPermissions().add(new AccessControlPermission("Contract_CreateContract"));
    readWrite.getPermissions().add(new AccessControlPermission("Contract_CreateContractAsset"));
    readWrite.getPermissions().add(new AccessControlPermission("Customer_UpdateCustomer"));
    readWrite.getPermissions().add(new AccessControlPermission("Customer_UpdateProfile"));
    readWrite.getPermissions().add(new AccessControlPermission("Customer_UpdateAddress"));
    readWrite.getPermissions().add(new AccessControlPermission("Contract_UpdateContract"));
    readWrite.getPermissions().add(new AccessControlPermission("Contract_UpdateContractAsset"));
    AccessControlGroup customerAdmin = new AccessControlGroup("CustomerAdmin");
    customerAdmin.getInherits().add(readWrite);
    customerAdmin.getPermissions().add(new AccessControlPermission("Customer_DeleteCustomer"));
    customerAdmin.getPermissions().add(new AccessControlPermission("Customer_DeleteProfile"));
    customerAdmin.getPermissions().add(new AccessControlPermission("Customer_DeleteAddress"));
    AccessControlGroup contractAdmin = new AccessControlGroup("ContractAdmin");
    contractAdmin.getInherits().add(readWrite);
    contractAdmin.getPermissions().add(new AccessControlPermission("Contract_DeleteContract"));
    contractAdmin.getPermissions().add(new AccessControlPermission("Contract_DeleteContractAsset"));
    AccessControlGroup systemAdmin = new AccessControlGroup("SystemAdmin");
    systemAdmin.getInherits().add(readWrite);
    systemAdmin.getPermissions().add(new AccessControlPermission("System_ReadUser"));
    systemAdmin.getPermissions().add(new AccessControlPermission("System_CreateUser"));
    systemAdmin.getPermissions().add(new AccessControlPermission("System_UpdateUser"));
    systemAdmin.getPermissions().add(new AccessControlPermission("System_DeleteUser"));
    AccessControlGroup admin = new AccessControlGroup("Admin");
    admin.getInherits().add(customerAdmin);
    admin.getInherits().add(contractAdmin);
    admin.getInherits().add(systemAdmin);
    admin.getPermissions();
    conf.getGroups().add(readOnly);
    conf.getGroups().add(readWrite);
    conf.getGroups().add(customerAdmin);
    conf.getGroups().add(contractAdmin);
    conf.getGroups().add(systemAdmin);
    conf.getGroups().add(admin);
    return conf;
  }

}
