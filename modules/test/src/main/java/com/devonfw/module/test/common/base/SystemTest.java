package com.devonfw.module.test.common.base;

import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Tag;

/**
 * This is the abstract base class for a system test. You are free to create your system tests as you like just by
 * annotating {@link CategorySystemTest} using {@link Category}. However, in most cases it will be convenient just to
 * extend this class.
 *
 * @see CategorySystemTest
 */
@Tag("system")
public abstract class SystemTest extends BaseTest {

}
