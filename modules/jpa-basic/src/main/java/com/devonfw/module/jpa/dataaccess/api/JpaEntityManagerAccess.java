package com.devonfw.module.jpa.dataaccess.api;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Internal access to {@link EntityManager}.
 */
class JpaEntityManagerAccess {

  private static final Logger LOG = LoggerFactory.getLogger(JpaEntityManagerAccess.class);

  private static EntityManager entityManager;

  static void setEntityManager(EntityManager entityManager, boolean check) {

    if ((JpaEntityManagerAccess.entityManager != null) && (JpaEntityManagerAccess.entityManager != entityManager)) {
      if (check) {
        throw new IllegalStateException("EntityManager has already been initialized!");
      } else {
        LOG.debug("EntityManager conflict: {} has been replaced with {}. This may only happen during tests.",
            JpaEntityManagerAccess.entityManager, entityManager);
      }
    }
    JpaEntityManagerAccess.entityManager = entityManager;
  }

  static boolean hasEntityManager() {

    return (entityManager != null);
  }

  static EntityManager getEntityManager() {

    if (entityManager == null) {
      throw new IllegalStateException("EntityManager has not yet been initialized!");
    }
    return entityManager;
  }

}
