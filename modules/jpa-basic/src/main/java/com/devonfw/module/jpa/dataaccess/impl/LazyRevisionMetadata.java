/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package com.devonfw.module.jpa.dataaccess.impl;

import java.util.Date;

import javax.persistence.EntityManager;

import com.devonfw.module.basic.common.api.RevisionMetadata;
import com.devonfw.module.jpa.dataaccess.api.AdvancedRevisionEntity;

/**
 * This is a lazy implementation of the {@link RevisionMetadata} interface.
 *
 */
public class LazyRevisionMetadata implements RevisionMetadata {

  /** The {@link EntityManager} used to read the metadata. */
  private final EntityManager entityManager;

  /** @see #getRevision() */
  private final Long revision;

  /** @see #getRevisionEntity() */
  private AdvancedRevisionEntity revisionEntity;

  /**
   * The constructor.
   *
   * @param entityManager is the {@link EntityManager} used to fetch metadata.
   * @param revision is the {@link #getRevision() revision}.
   */
  public LazyRevisionMetadata(EntityManager entityManager, Long revision) {

    super();
    this.entityManager = entityManager;
    this.revision = revision;
  }

  /**
   * @return the revisionEntity
   */
  public AdvancedRevisionEntity getRevisionEntity() {

    if (this.revisionEntity == null) {
      this.revisionEntity = this.entityManager.find(AdvancedRevisionEntity.class, this.revision);
      assert (this.revisionEntity != null);
    }
    return this.revisionEntity;
  }

  @Override
  public String getAuthor() {

    return getRevisionEntity().getUserLogin();
  }

  @Override
  public Date getDate() {

    return getRevisionEntity().getDate();
  }

  @Override
  public Number getRevision() {

    return this.revision;
  }

}
