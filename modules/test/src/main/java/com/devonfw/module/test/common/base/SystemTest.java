package com.devonfw.module.test.common.base;

import org.junit.jupiter.api.Tag;

import com.devonfw.module.test.common.api.category.TagSystemTest;

/**
 * This is the abstract base class for a system test. You are free to create your system tests as you like just by
 * annotating {@link TagSystemTest} using {@link Tag}. However, in most cases it will be convenient just to extend this
 * class.
 *
 * @see TagSystemTest
 */
@TagSystemTest
public abstract class SystemTest extends BaseTest {

}
