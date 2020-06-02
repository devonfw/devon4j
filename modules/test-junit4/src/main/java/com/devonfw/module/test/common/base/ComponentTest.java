package com.devonfw.module.test.common.base;

import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.devonfw.module.test.common.api.category.CategoryComponentTest;

/**
 * This is the abstract base class for a component test. You are free to create your component tests as you like just by
 * annotating {@link CategoryComponentTest} using {@link Category}. However, in most cases it will be convenient just to
 * extend this class.
 *
 * @see CategoryComponentTest
 */
@RunWith(SpringRunner.class)
@TestExecutionListeners({ TransactionalTestExecutionListener.class, DependencyInjectionTestExecutionListener.class })
@Category(CategoryComponentTest.class)
public abstract class ComponentTest extends BaseTest {

}
