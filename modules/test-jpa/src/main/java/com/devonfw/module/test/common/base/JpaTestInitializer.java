package com.devonfw.module.test.common.base;

import javax.persistence.EntityManager;

import com.devonfw.module.jpa.dataaccess.api.JpaInitializer;

/**
 * Helper class giving access to {@link #setJpaEntityManager(EntityManager) set} the {@link EntityManager} for
 * tests.<br>
 * The {@code spring-test} infrastructure is very powerful and does a lot of magic for you. However, in some edge cases
 * you need to understand what is going on behind the scenes to workaround some problems. On case is the regular
 * {@link com.devonfw.module.jpa.dataaccess.api.JpaInitializer} that initializes the {@link EntityManager} during the
 * bootstrapping of the spring context and makes it internally available to static methods (e.g.
 * {@link com.devonfw.module.jpa.dataaccess.api.JpaHelper#asEntity(com.devonfw.module.basic.common.api.reference.Ref, Class)}).
 * However, {@code spring-test} internally reuses the spring context to boost performance if multiple spring tests are
 * run using the same context. However, if then another spring test runs with a different context then that spring
 * context will be setup overwriting the static instance of {@link EntityManager}. Still everything works as expected.
 * But now if another spring-test is executed using a previous configuration that previous spring context will be
 * magically reused by {@code spring-test}. In such case the static instance of {@link EntityManager} has to be set back
 * to the {@link EntityManager} of the current spring context otherwise you will get strange errors in your tests. In
 * order to archive this goal you need to inject the {@link EntityManager} into each of your spring tests and pass it
 * into the {@link #setJpaEntityManager(EntityManager) method} offered here. To simplify your life you can simply derive
 * from {@link SubsystemDbTest},
 */
public class JpaTestInitializer extends JpaInitializer {

  private static final JpaTestInitializer INSTANCE = new JpaTestInitializer();

  /**
   * @param entityManager the {@link EntityManager} to set.
   */
  public static final void setJpaEntityManager(EntityManager entityManager) {

    INSTANCE.setEntityManager(entityManager, false);
  }

}
