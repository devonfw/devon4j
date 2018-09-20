package com.devonfw.module.jpa.dataaccess.api;

import java.util.List;

import net.sf.mmm.util.entity.api.PersistenceEntity;

/**
 * This is the interface for a {@link Dao} responsible for a {@link PersistenceEntity} that represents master-data. In
 * that case you typically have a limited number of entities in your persistent store and need operations like
 * {@link #findAll()}.<br>
 * <b>ATTENTION:</b><br>
 * Such operations are not part of {@link GenericDao} or {@link Dao} as invoking them (accidently) could cause that an
 * extraordinary large number of entities are loaded into main memory and could cause serious performance and stability
 * disasters. So only extend this interface in case you are aware of what you are doing.
 *
 * @param <E> is the generic type of the {@link PersistenceEntity}.
 *
 */
public interface MasterDataDao<E extends PersistenceEntity<Long>> extends Dao<E> {

  /**
   * @return an {@link Iterable} with ALL managed entities from the persistent store. Not exposed to API by default as
   *         this might not make sense for all kind of entities.
   */
  List<E> findAll();

}
