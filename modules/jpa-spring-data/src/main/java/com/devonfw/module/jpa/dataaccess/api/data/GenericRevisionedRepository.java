package com.devonfw.module.jpa.dataaccess.api.data;

import java.io.Serializable;
import java.util.List;

import com.devonfw.module.basic.common.api.RevisionMetadata;

/**
 * {@link GenericRepository} with additional support for {@link org.hibernate.envers.Audited}
 *
 * @param <E> generic type of the managed {@link #getEntityClass() entity}. Typically implementing
 *        {@link com.devonfw.module.basic.common.api.entity.RevisionedPersistenceEntity}.
 * @param <ID> generic type of the {@link com.devonfw.module.basic.common.api.entity.RevisionedPersistenceEntity#getId()
 *        primary key} of the entity.
 * @since 3.0.0
 */
public interface GenericRevisionedRepository<E, ID extends Serializable> extends GenericRepository<E, ID> {

  /**
   * @param id the {@link com.devonfw.module.basic.common.api.entity.RevisionedPersistenceEntity#getId() primary key}.
   * @param revision the {@link RevisionMetadata#getRevision() revision} of the requested entity.
   * @return the entity with the given {@code id} and {@code revision}.
   * @see #find(Serializable)
   * @see #getRevisionHistoryMetadata(Serializable, boolean)
   * @see RevisionMetadata#getRevision()
   * @see com.devonfw.module.basic.common.api.entity.RevisionedPersistenceEntity#getRevision()
   */
  E find(ID id, Number revision);

  /**
   * @param id the {@link com.devonfw.module.basic.common.api.entity.RevisionedPersistenceEntity#getId() primary key}.
   * @return the {@link List} of {@link RevisionMetadata} for the historic
   *         {@link com.devonfw.module.basic.common.api.entity.RevisionedPersistenceEntity#getRevision() revisions} of the
   *         entity with the given {@code id}. In case no such history exists, an {@link List#isEmpty() empty}
   *         {@link List} is returned.
   */
  default List<RevisionMetadata> getRevisionHistoryMetadata(ID id) {

    return getRevisionHistoryMetadata(id, false);
  }

  /**
   * @param id the {@link com.devonfw.module.basic.common.api.entity.RevisionedPersistenceEntity#getId() primary key}.
   * @param lazy - {@code true} to load the {@link RevisionMetadata} lazily, {@code false} otherwise (eager loading).
   * @return the {@link List} of {@link RevisionMetadata} for the historic
   *         {@link com.devonfw.module.basic.common.api.entity.RevisionedPersistenceEntity#getRevision() revisions} of the
   *         entity with the given {@code id}. In case no such history exists, an {@link List#isEmpty() empty}
   *         {@link List} is returned.
   */
  List<RevisionMetadata> getRevisionHistoryMetadata(ID id, boolean lazy);

  /**
   * @param id the {@link com.devonfw.module.basic.common.api.entity.RevisionedPersistenceEntity#getId() primary key}.
   * @return the {@link RevisionMetadata} of the last historic
   *         {@link com.devonfw.module.basic.common.api.entity.RevisionedPersistenceEntity#getRevision() revision} of the
   *         entity with the given {@code id}. Will be {@code null} if no such history exists.
   */
  RevisionMetadata getLastRevisionHistoryMetadata(ID id);

}
