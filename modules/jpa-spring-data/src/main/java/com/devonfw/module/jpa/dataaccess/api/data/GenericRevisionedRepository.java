package com.devonfw.module.jpa.dataaccess.api.data;

import java.io.Serializable;
import java.util.List;

import net.sf.mmm.util.entity.api.RevisionedEntity;

import org.hibernate.envers.Audited;

import com.devonfw.module.jpa.dataaccess.api.RevisionMetadata;

/**
 * {@link GenericRepository} with additional support for {@link Audited}
 *
 * @param <E> generic type of the managed {@link #getEntityClass() entity}. Typically implementing
 *        {@link net.sf.mmm.util.entity.api.PersistenceEntity} and
 *        {@link net.sf.mmm.util.entity.api.MutableRevisionedEntity}.
 * @param <ID> generic type of the {@link net.sf.mmm.util.entity.api.PersistenceEntity#getId() primary key} of the
 *        entity.
 * @since 3.0.0
 */
public interface GenericRevisionedRepository<E, ID extends Serializable> extends GenericRepository<E, ID> {

  /**
   * @param id the {@link net.sf.mmm.util.entity.api.PersistenceEntity#getId() primary key}.
   * @param revision the {@link RevisionMetadata#getRevision() revision} of the requested entity.
   * @return the entity with the given {@code id} and {@code revision}.
   * @see #find(Serializable)
   * @see #getRevisionHistoryMetadata(Serializable, boolean)
   * @see RevisionMetadata#getRevision()
   * @see net.sf.mmm.util.entity.api.RevisionedEntity#getRevision()
   */
  E find(ID id, Number revision);

  /**
   * @param id the {@link net.sf.mmm.util.entity.api.PersistenceEntity#getId() primary key}.
   * @return die {@link List}e der {@link RevisionMetadata Metadaten} der historischen
   *         {@link RevisionedEntity#getRevision() Revisionen} zur angegebenen Entität. Falls keine Historie existiert,
   *         wird eine leere {@link List}e zurück geliefert.
   */
  default List<RevisionMetadata> getRevisionHistoryMetadata(ID id) {

    return getRevisionHistoryMetadata(id, false);
  }

  /**
   * @param id the {@link net.sf.mmm.util.entity.api.PersistenceEntity#getId() primary key}.
   * @param lazy - {@code true} to load the {@link RevisionMetadata} lazily, {@code false} otherwise (eager loading).
   * @return the {@link List} of {@link RevisionMetadata} for the historical {@link RevisionedEntity#getRevision()
   *         revisions} of the {@link net.sf.mmm.util.entity.api.PersistenceEntity} zur angegebenen Entität. Falls keine
   *         Historie existiert, wird eine leere {@link List}e zurück geliefert.
   */
  List<RevisionMetadata> getRevisionHistoryMetadata(ID id, boolean lazy);

  /**
   * @param id the {@link net.sf.mmm.util.entity.api.PersistenceEntity#getId() primary key}.
   * @return die {@link RevisionMetadata Metadaten} der letzten historischen {@link RevisionedEntity#getRevision()
   *         Revisionen} zur angegebenen Entität. Falls keine Historie existiert, wird {@code null} zurück geliefert.
   */
  RevisionMetadata getLastRevisionHistoryMetadata(ID id);

}
