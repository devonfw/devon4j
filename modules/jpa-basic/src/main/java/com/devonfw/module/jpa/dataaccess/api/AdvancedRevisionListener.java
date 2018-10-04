/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package com.devonfw.module.jpa.dataaccess.api;

import org.hibernate.envers.RevisionListener;

import com.devonfw.module.basic.common.api.user.UserSessionAccess;

/**
 * This is the implementation of {@link RevisionListener} that enriches {@link AdvancedRevisionEntity} with additional
 * information.
 *
 * @since 3.0.0
 */
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
    revision.setUserLogin(UserSessionAccess.getUserLogin());
  }

}