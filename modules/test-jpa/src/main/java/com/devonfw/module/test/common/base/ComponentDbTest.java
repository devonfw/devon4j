package com.devonfw.module.test.common.base;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.devonfw.module.test.common.base.clean.TestCleaner;

/**
 * Combination of {@link DbTest} with {@link ComponentTest}.
 */
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({ TransactionalTestExecutionListener.class, DependencyInjectionTestExecutionListener.class })
@Tag("component")
public abstract class ComponentDbTest extends DbTest {

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
        this.testUtility.cleanup();
      }
    }
  }

  /**
   * @return {@code true} if {@link TestCleaner#cleanup()} should be enabled, {@code false} otherwise. Override to
   *         change behavior for your test.
   */
  @Override
  protected boolean isPerformCleanup() {

    return true;
  }

  /**
   * @return {@code true} to allow that {@link TestCleaner#cleanup()} is invoked for each test-method of your test
   *         (potentially multiple times), {@code false} to ensure that {@link TestCleaner#cleanup()} is never invoked
   *         multiple times per test class (e.g. to speed up read-only tests that will never have side-effects). Will be
   *         ignored if {@link #isPerformCleanup()} returns {@code false}. Override to change behavior for your test.
   */
  @Override
  protected boolean isAllowMultiCleanup() {

    return true;
  }

  /**
   * @return the instance of {@link TestCleaner}.
   */
  @Override
  public TestCleaner getTestUtility() {

    return this.testUtility;
  }

}
