/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package com.devonfw.module.jpa.dataaccess.api;

import java.util.List;

import javax.persistence.PersistenceException;

import com.devonfw.module.basic.common.api.RevisionMetadata;
import com.devonfw.module.basic.common.api.entity.RevisionedEntity;
import com.devonfw.module.basic.common.api.entity.RevisionedPersistenceEntity;

/**
 * This is the interface for a {@link GenericDao} with the ability of revision-control. It organizes a revision-history
 * (journal) of the managed entities.
 *
 * @see RevisionedEntity
 *
 * @param <ID> is the type of the {@link RevisionedEntity#getId() primary key}.
 * @param <ENTITY> is the type of the managed entity.
 *
 */
public interface GenericRevisionedDao<ID, ENTITY extends RevisionedPersistenceEntity<ID>>
    extends GenericDao<ID, ENTITY> {

  /**
   * This method will get the {@link List} of historic {@link RevisionedPersistenceEntity#getRevision() revisions} of
   * the {@link RevisionedPersistenceEntity entity} with the given <code>id</code>.<br>
   * If the <code>entity</code> is NOT revision controlled, an {@link java.util.Collections#emptyList() empty list} is
   * returned.
   *
   * @param id the {@link RevisionedPersistenceEntity#getId() primary key} of the {@link RevisionedPersistenceEntity
   *        entity} to retrieve the history for.
   * @return the {@link List} of historic {@link RevisionedEntity#getRevision() revisions}.
   */
  List<Number> getRevisionHistory(ID id);

  /**
   * This method will get the {@link List} of {@link RevisionMetadata} from the {@link RevisionedEntity#getRevision()
   * revision}-history of the entity with the given <code>id</code>.
   *
   * @param id is the {@link RevisionedEntity#getId() primary key} of the entity for which the history-metadata is
   *        requested.
   * @return the requested {@link List} of {@link RevisionMetadata}.
   */
  List<RevisionMetadata> getRevisionHistoryMetadata(Object id);

  /**
   * This method loads a historic {@link RevisionedEntity#getRevision() revision} of the {@link RevisionedEntity} with
   * the given <code>id</code> from the persistent store. <br>
   * However if the given <code>revision</code> is {@link RevisionedEntity#LATEST_REVISION} the {@link #find(Object)
   * latest revision will be loaded}. <br>
   * <b>ATTENTION:</b><br>
   * You should not make assumptions about the <code>revision</code> numbering of the underlying implementation. Please
   * use {@link #getRevisionHistory(Object)} or {@link #getRevisionHistoryMetadata(Object)} to find revision numbers.
   *
   * @param id is the {@link RevisionedEntity#getId() primary key} of the requested {@link RevisionedEntity entity}.
   * @param revision is the {@link RevisionedEntity#getRevision() revision} of the requested entity or
   *        {@link RevisionedEntity#LATEST_REVISION} to get the {@link #find(Object) latest} revision. A specific
   *        revision has to be greater than <code>0</code>.
   * @return the requested {@link RevisionedEntity entity}.
   * @throws RuntimeException if the requested {@link RevisionedEntity entity} could NOT be found.
   */
  ENTITY load(ID id, Number revision);

  /**
   * {@inheritDoc}
   *
   * The behavior of this method depends on the revision-control strategy of the implementation. <br>
   * <ul>
   * <li>In case of an <em>audit-proof revision-history</em> the deletion of the {@link RevisionedEntity#LATEST_REVISION
   * latest revision} of an entity will only move it to the history while the deletion of a
   * {@link RevisionedEntity#getRevision() historic entity} is NOT permitted and will cause a
   * {@link PersistenceException}.</li>
   * <li>In case of an <em>on-demand revision-history</em> the deletion of the {@link RevisionedEntity#LATEST_REVISION
   * latest revision} of an entity will either move it to the history or</li>
   * </ul>
   * If the given <code>entity</code> is a {@link RevisionedEntity#getRevision() historic entity} the according historic
   */
  @Override
  void delete(ENTITY entity);

}
