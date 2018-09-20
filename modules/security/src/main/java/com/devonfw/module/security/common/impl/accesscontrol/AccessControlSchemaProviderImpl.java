package com.devonfw.module.security.common.impl.accesscontrol;

import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.devonfw.module.security.common.api.accesscontrol.AccessControlSchema;
import com.devonfw.module.security.common.base.accesscontrol.AccessControlSchemaMapper;
import com.devonfw.module.security.common.base.accesscontrol.AccessControlSchemaProvider;

/**
 * This is the default implementation of {@link AccessControlSchemaProvider}.
 *
 */
public class AccessControlSchemaProviderImpl implements AccessControlSchemaProvider {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(AccessControlSchemaProviderImpl.class);

  private Resource accessControlSchema;

  private AccessControlSchemaMapper accessControlSchemaMapper;

  private boolean initialized;

  /**
   * The constructor.
   */
  public AccessControlSchemaProviderImpl() {

    super();
    this.initialized = false;
  }

  /**
   * Initializes this class.
   */
  @PostConstruct
  public void initialize() {

    if (this.initialized) {
      return;
    }
    LOG.debug("Initializing.");
    if (this.accessControlSchemaMapper == null) {
      this.accessControlSchemaMapper = new AccessControlSchemaXmlMapper();
    }
    if (this.accessControlSchema == null) {

      this.accessControlSchema = new ClassPathResource("config/app/security/access-control-schema.xml");
    }
    this.initialized = true;
  }

  @Override
  public AccessControlSchema loadSchema() {

    initialize();
    LOG.debug("Reading access control schema from {}", this.accessControlSchema);
    try (InputStream inputStream = this.accessControlSchema.getInputStream()) {
      AccessControlSchema schema = this.accessControlSchemaMapper.read(inputStream);
      LOG.debug("Reading access control schema completed successfully.");
      return schema;
    } catch (Exception e) {
      throw new IllegalStateException("Failed to load access control schema from " + this.accessControlSchema, e);
    }
  }

  /**
   * @return the {@link AccessControlSchemaMapper}.
   */
  public AccessControlSchemaMapper getAccessControlSchemaMapper() {

    return this.accessControlSchemaMapper;
  }

  /**
   * @param accessControlSchemaMapper the {@link AccessControlSchemaMapper} to use.
   */
  public void setAccessControlSchemaMapper(AccessControlSchemaMapper accessControlSchemaMapper) {

    this.accessControlSchemaMapper = accessControlSchemaMapper;
  }

  /**
   * @return accessControlSchema
   */
  public Resource getAccessControlSchema() {

    return this.accessControlSchema;
  }

  /**
   * @param accessControlSchema the {@link Resource} pointing to the XML configuration of the access control schema.
   */
  public void setAccessControlSchema(Resource accessControlSchema) {

    this.accessControlSchema = accessControlSchema;
  }

}
