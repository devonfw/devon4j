package com.devonfw.module.jpa.dataaccess.base;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Query;
import org.springframework.beans.factory.FactoryBean;

/**
 * ProductWriter allows to get named query from resource NamedQueries.xml.
 * It is used for example in job to get sql for JdbcCursorItemReader.
 *
 */
public class NamedQueryFactoryBean implements FactoryBean<String> {

  private EntityManager entityManager;

  private String queryName;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getObject() throws Exception {

    return this.entityManager.createNamedQuery(this.queryName).unwrap(Query.class).getQueryString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getObjectType() {

    return String.class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSingleton() {

    return false;
  }

  /**
   * @param entityManager the entityManager to set
   */
  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {

    this.entityManager = entityManager;
  }

  /**
   * @param queryName the queryName to set
   */
  public void setQueryName(String queryName) {

    this.queryName = queryName;
  }

}
