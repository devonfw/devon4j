package com.devonfw.module.test.common.base;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devonfw.module.test.common.base.clean.TestCleaner;

/**
 * Extends {@link BaseTest} with the following features:
 * <ul>
 * <li>Automatically register the {@link EntityManager} of the current spring-test. See {@link JpaTestInitializer} for
 * further details.</li>
 * <li>Automatically performs {@link TestCleaner#cleanup() cleanup} to prevent side-effects. Can be configured by
 * overriding {@link #isPerformCleanup()} and {@link #isAllowMultiCleanup()}.</li>
 * </ul>
 */
public abstract class DbTest extends BaseTest {

  private static final Logger LOG = LoggerFactory.getLogger(DbTest.class);

  @PersistenceContext
  private EntityManager entityManager;

  @Inject
  private TestCleaner testUtility;

  @Override
  protected void doSetUp() {

    super.doSetUp();
    if (isInitialSetup()) {
      JpaTestInitializer.setJpaEntityManager(this.entityManager);
    }
    if (isPerformCleanup()) {
      if (isAllowMultiCleanup() || isInitialSetup()) {
        try {
          this.testUtility.cleanup();
        } catch (Exception exception) {
          LOG.error("Exception occurred while performing cleanup", exception);
        }
      }
    }
  }

  /**
   * @return {@code true} if {@link TestCleaner#cleanup()} should be enabled, {@code false} otherwise. Override to
   *         change behavior for your test.
   */
  protected boolean isPerformCleanup() {

    return true;
  }

  /**
   * @return {@code true} to allow that {@link TestCleaner#cleanup()} is invoked for each test-method of your test
   *         (potentially multiple times), {@code false} to ensure that {@link TestCleaner#cleanup()} is never invoked
   *         multiple times per test class (e.g. to speed up read-only tests that will never have side-effects). Will be
   *         ignored if {@link #isPerformCleanup()} returns {@code false}. Override to change behavior for your test.
   */
  protected boolean isAllowMultiCleanup() {

    return true;
  }

  /**
   * @return the instance of {@link TestCleaner}.
   */
  public TestCleaner getTestUtility() {

    return this.testUtility;
  }

}
