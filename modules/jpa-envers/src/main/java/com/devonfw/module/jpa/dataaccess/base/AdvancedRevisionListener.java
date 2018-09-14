/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package com.devonfw.module.jpa.dataaccess.base;

import net.sf.mmm.util.session.api.UserSessionAccess;

import org.hibernate.envers.RevisionListener;

/**
 * This is the implementation of {@link RevisionListener} that enriches {@link AdvancedRevisionEntity} with additional
 * information.
 *
 * @deprecated If you want to have the backward compatibility with your existing code , please use this class else if
 *             you are starting the development of application from scratch, please use
 *             {@link com.devonfw.module.jpa.dataaccess.api.AdvancedRevisionListener}
 */
@Deprecated
public class AdvancedRevisionListener implements RevisionListener {

  /**
   * The constructor.
   */
  public AdvancedRevisionListener() {

    super();
  }

  @Override
  public void newRevision(Object revisionEntity) {

    AdvancedRevisionEntity revision = (AdvancedRevisionEntity) revisionEntity;
    revision.setUser(UserSessionAccess.getUserLogin());
  }

}