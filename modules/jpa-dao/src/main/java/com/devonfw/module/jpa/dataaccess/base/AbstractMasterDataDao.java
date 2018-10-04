package com.devonfw.module.jpa.dataaccess.base;

import java.util.List;

import com.devonfw.module.jpa.dataaccess.api.MasterDataDao;
import com.devonfw.module.jpa.dataaccess.api.PersistenceEntity;

/**
 * This is the abstract base implementation of {@link MasterDataDao}.
 *
 * @param <E> is the generic type of the {@link PersistenceEntity}.
 */
public abstract class AbstractMasterDataDao<E extends PersistenceEntity<Long>> extends AbstractDao<E>
    implements MasterDataDao<E> {

  /**
   * The constructor.
   */
  public AbstractMasterDataDao() {

    super();
  }

  @Override
  public List<E> findAll() {

    return super.findAll();
  }

}
