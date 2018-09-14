package com.devonfw.module.test.common.base;

import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.devonfw.module.test.common.api.category.CategorySubsystemTest;
import com.devonfw.module.test.common.base.SubsystemTest;

/**
 * Combination of {@link DbTest} with {@link SubsystemTest}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ TransactionalTestExecutionListener.class, DependencyInjectionTestExecutionListener.class })
@Category(CategorySubsystemTest.class)
public abstract class SubsystemDbTest extends DbTest {

}
