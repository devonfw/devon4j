package com.devonfw.module.jpa.dataaccess.api;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Initializer bean for {@link EntityManager}. Required in case you are using {@link JpaHelper} to get access to
 * {@link EntityManager}. When you create a devon4j application it will contain a class called {@code JpaConfig} that
 * will configure this {@link JpaInitializer} as spring bean automatically for you application. See
 * <a href="https://github.com/devonfw/devon4j/blob/master/documentation/guide-jpa-idref.asciidoc">IdRef guide</a> for a
 * detailed documentation.
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
