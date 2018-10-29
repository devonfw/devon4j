package com.devonfw.example.component.dataaccess.impl;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import com.devonfw.example.component.dataaccess.api.BarEntity;
import com.devonfw.example.component.dataaccess.api.dao.BarDao;
import com.devonfw.module.jpa.dataaccess.api.GenericDao;
import com.devonfw.module.jpa.dataaccess.base.AbstractGenericDaoTest;

/**
 * This type provides methods in a transactional environment for the {@link AbstractGenericDaoTest}. All methods,
 * annotated with the {@link Transactional} annotation, are executed in separate transaction, thus one test case can
 * execute multiple transactions. Unfortunately this does not work when the transactional methods are directly in the
 * top-level class of the test-case itself.
 */
@Named
public class BarDaoTxBean {

  @Inject
  private BarDao genericDao;

  /**
   * Creates a new {@link BarEntity}, persist it and surround everything with a transaction.
   *
   * @return entity the new {@link BarEntity}.
   */
  @Transactional
  public BarEntity create() {

    BarEntity entity = new BarEntity();
    this.genericDao.save(entity);
    return entity;
  }

  /**
   * Loads the {@link BarEntity} with the given {@code id} and
   * {@link GenericDao#forceIncrementModificationCounter(Object) increments the modification counter}.
   *
   * @param id of the {@link BarEntity} to load and increment.
   * @return entity the updated {@link BarEntity}.
   */
  @Transactional
  public BarEntity incrementModificationCounter(long id) {

    BarEntity entity = this.genericDao.find(id);
    this.genericDao.forceIncrementModificationCounter(entity);
    return entity;
  }
}