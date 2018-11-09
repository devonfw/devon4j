package com.devonfw.module.jpa.dataaccess.api;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Initializer bean for {@link EntityManager}. Will be auto configured via {@code devon4j-starter-jpa}.
 *
 * @since 3.0.0
 */
public class JpaInitializer {

  /**
   * @param entityManager the {@link EntityManager} to inject.
   */
  @PersistenceContext
  protected void setEntityManager(EntityManager entityManager) {

    JpaEntityManagerAccess.setEntityManager(entityManager, true);
  }

  /**
   * @param entityManager the {@link EntityManager} to set.
   * @param check - {@code true} to check that the {@link EntityManager} does not change on-the-fly (desired in
   *        productive code), {@code false} otherwise (may be desired in test-code).
   */
  protected void setEntityManager(EntityManager entityManager, boolean check) {

    JpaEntityManagerAccess.setEntityManager(entityManager, check);
  }
}
