package com.devonfw.module.jpa.dataaccess.base;

import com.devonfw.module.basic.common.api.entity.PersistenceEntity;
import com.devonfw.module.jpa.dataaccess.api.Dao;

/**
 * Abstract base implementation of {@link Dao} interface.
 *
 * @param <E> is the generic type of the {@link PersistenceEntity}.
 */
public abstract class AbstractDao<E extends PersistenceEntity<Long>> extends AbstractGenericDao<Long, E>
    implements Dao<E> {

  /**
   * The constructor.
   */
  public AbstractDao() {

    super();
  }

}