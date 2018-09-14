package com.devonfw.module.security.common.impl.accesscontrol;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.devonfw.module.security.common.base.accesscontrol.AbstractAccessControlProvider;
import com.devonfw.module.security.common.base.accesscontrol.AccessControlSchemaProvider;

/**
 * This is the default implementation of {@link com.devonfw.module.security.common.api.accesscontrol.AccessControlProvider}.
 *
 */
public class AccessControlProviderImpl extends AbstractAccessControlProvider {

  private AccessControlSchemaProvider accessControlSchemaProvider;

  /**
   * The constructor.
   */
  public AccessControlProviderImpl() {

    super();
  }

  /**
   * Initializes this class.
   */
  @PostConstruct
  public void initialize() {

    if (this.accessControlSchemaProvider == null) {
      this.accessControlSchemaProvider = new AccessControlSchemaProviderImpl();
    }
    initialize(this.accessControlSchemaProvider.loadSchema());
  }

  /**
   * @return accessControlSchemaProvider
   */
  public AccessControlSchemaProvider getAccessControlSchemaProvider() {

    return this.accessControlSchemaProvider;
  }

  /**
   * @param accessControlSchemaProvider the {@link AccessControlSchemaProvider} to {@link Inject}.
   */
  @Inject
  public void setAccessControlSchemaProvider(AccessControlSchemaProvider accessControlSchemaProvider) {

    this.accessControlSchemaProvider = accessControlSchemaProvider;
  }

}
