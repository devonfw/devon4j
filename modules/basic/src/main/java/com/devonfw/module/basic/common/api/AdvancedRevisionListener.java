package com.devonfw.module.basic.common.api;

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
