package com.devonfw.example.component.dataaccess.impl.dao;

import javax.inject.Named;

import com.devonfw.example.component.dataaccess.api.BarEntity;
import com.devonfw.example.component.dataaccess.api.dao.BarDao;
import com.devonfw.module.jpa.dataaccess.base.AbstractDao;

/**
 * Implementation of {@link BarDao}.
 */
@Named
public class BarDaoImpl extends AbstractDao<BarEntity> implements BarDao {

  @Override
  protected Class<BarEntity> getEntityClass() {

    return BarEntity.class;
  }

}
