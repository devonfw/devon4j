/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package com.devonfw.module.jpa.dataaccess.base;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;

import com.devonfw.module.basic.common.api.entity.RevisionedPersistenceEntity;
import com.devonfw.module.jpa.dataaccess.api.GenericRevisionedDao;
import com.devonfw.module.jpa.dataaccess.api.RevisionMetadata;
import com.devonfw.module.jpa.dataaccess.impl.LazyRevisionMetadata;

/**
 * This is the abstract base-implementation of a {@link AbstractGenericDao} using to manage the revision-control.
 *
 * @param <ID> is the type of the {@link RevisionedPersistenceEntity#getId() primary key} of the managed
 *        {@link RevisionedPersistenceEntity entity}.
 * @param <ENTITY> is the {@link #getEntityClass() type} of the managed entity.
 *
 * @since 3.0.0
 */
public abstract class AbstractGenericRevisionedDao<ID, ENTITY extends RevisionedPersistenceEntity<ID>>
    extends AbstractGenericDao<ID, ENTITY> implements GenericRevisionedDao<ID, ENTITY> {

  /**
   * The constructor.
   */
  public AbstractGenericRevisionedDao() {

    super();
  }

  /**
   * @return the auditReader
   */
  protected AuditReader getAuditReader() {

    return AuditReaderFactory.get(getEntityManager());
  }

  @Override
  public ENTITY load(ID id, Number revision) {

    if (revision == RevisionedPersistenceEntity.LATEST_REVISION) {
      return find(id);
    } else {
      return loadRevision(id, revision);
    }
  }

  /**
   * This method gets a historic revision of the {@link RevisionedPersistenceEntity entity} with the given {@code id}.
   *
   * @param id is the {@link RevisionedPersistenceEntity#getId() ID} of the requested {@link RevisionedPersistenceEntity
   *        entity}.
   * @param revision is the {@link RevisionedPersistenceEntity#getRevision() revision}
   * @return the requested {@link RevisionedPersistenceEntity entity}.
   */
  protected ENTITY loadRevision(Object id, Number revision) {

    Class<? extends ENTITY> entityClassImplementation = getEntityClass();
    ENTITY entity = getAuditReader().find(entityClassImplementation, id, revision);
    if (entity != null) {
      entity.setRevision(revision);
    }
    return entity;
  }

  @Override
  public List<Number> getRevisionHistory(ID id) {

    return getAuditReader().getRevisions(getEntityClass(), id);
  }

  @Override
  public List<RevisionMetadata> getRevisionHistoryMetadata(Object id) {

    AuditReader auditReader = getAuditReader();
    List<Number> revisionList = auditReader.getRevisions(getEntityClass(), id);
    List<RevisionMetadata> result = new ArrayList<>();
    for (Number revision : revisionList) {
      Long revisionLong = Long.valueOf(revision.longValue());
      result.add(new LazyRevisionMetadata(getEntityManager(), revisionLong));
    }
    return result;
  }
}
