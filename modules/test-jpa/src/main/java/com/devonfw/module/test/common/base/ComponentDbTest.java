package com.devonfw.module.test.common.base;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.devonfw.module.test.common.api.category.TagComponentTest;
import com.devonfw.module.test.common.base.clean.TestCleaner;

/**
 * Combination of {@link DbTest} with {@link ComponentTest}.
 */
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({ TransactionalTestExecutionListener.class, DependencyInjectionTestExecutionListener.class })
@TagComponentTest
public abstract class ComponentDbTest extends DbTest {

}
