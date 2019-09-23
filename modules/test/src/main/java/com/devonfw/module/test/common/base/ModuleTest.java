package com.devonfw.module.test.common.base;

import org.junit.jupiter.api.Tag;

import com.devonfw.module.test.common.api.category.TagModuleTest;

/**
 * This is the abstract base class for a module test. You are free to create your module tests as you like just by
 * annotating {@link TagModuleTest} using {@link Tag}. However, in most cases it will be convenient just to extend this
 * class.
 *
 * @see ModuleTest
 *
 */
@TagModuleTest
public abstract class ModuleTest extends BaseTest {

}
