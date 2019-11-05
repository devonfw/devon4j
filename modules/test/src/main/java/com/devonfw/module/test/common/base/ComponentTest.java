package com.devonfw.module.test.common.base;

import org.junit.jupiter.api.Tag;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.devonfw.module.test.common.api.category.TagComponentTest;

/**
 * This is the abstract base class for a component test. You are free to create your component tests as you like just by
 * annotating {@link TagComponentTest} using {@link Tag}. However, in most cases it will be convenient just to extend
 * this class.
 *
 * @see TagComponentTest
 */
@SpringJUnitConfig
@TestExecutionListeners({ TransactionalTestExecutionListener.class, DependencyInjectionTestExecutionListener.class })
@TagComponentTest
public abstract class ComponentTest extends BaseTest {

}
