package com.devonfw.module.test.common.base;

import org.junit.experimental.categories.Category;

import com.devonfw.module.test.common.api.category.CategorySystemTest;
import com.devonfw.module.test.common.base.SystemTest;

/**
 * Combination of {@link DbTest} with {@link SystemTest}.
 */
@Category(CategorySystemTest.class)
public class SystemDbTest extends DbTest {

}
