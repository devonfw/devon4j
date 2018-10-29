package com.devonfw.module.jpa.dataaccess.base;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.query.Query;
import org.springframework.beans.factory.FactoryBean;

/**
 * ProductWriter allows to get named query from resource NamedQueries.xml. It is used for example in job to get sql for
 * JdbcCursorItemReader.
 *
 * @since 3.0.0
 */
public class NamedQueryFactoryBean implements FactoryBean<String> {

  private EntityManager entityManager;

  private String queryName;

  @Override
  public String getObject() throws Exception {

    return this.entityManager.createNamedQuery(this.queryName).unwrap(Query.class).getQueryString();
  }

  @Override
  public Class<?> getObjectType() {

    return String.class;
  }

  @Override
  public boolean isSingleton() {

    return false;
  }

  /**
   * @param entityManager the {@link EntityManager} to set.
   */
  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {

    this.entityManager = entityManager;
  }

  /**
   * @param queryName the name of the query.
   */
  public void setQueryName(String queryName) {

    this.queryName = queryName;
  }

}
