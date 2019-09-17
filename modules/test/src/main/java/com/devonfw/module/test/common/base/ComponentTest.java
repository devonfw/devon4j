package com.devonfw.module.test.common.base;

import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * This is the abstract base class for a component test. You are free to create your component tests as you like just by
 * annotating {@link CategoryComponentTest} using {@link Category}. However, in most cases it will be convenient just to
 * extend this class.
 *
 * @see CategoryComponentTest
 */
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({ TransactionalTestExecutionListener.class, DependencyInjectionTestExecutionListener.class })
@Tag("component")
public abstract class ComponentTest extends BaseTest {

}
