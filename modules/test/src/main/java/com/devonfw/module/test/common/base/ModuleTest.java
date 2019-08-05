package com.devonfw.module.test.common.base;

import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Tag;

/**
 * This is the abstract base class for a module test. You are free to create your module tests as you like just by
 * annotating {@link CategoryModuleTest} using {@link Category}. However, in most cases it will be convenient just to
 * extend this class.
 *
 * @see CategoryModuleTest
 *
 */
@Tag("module")
public abstract class ModuleTest extends BaseTest {

}
