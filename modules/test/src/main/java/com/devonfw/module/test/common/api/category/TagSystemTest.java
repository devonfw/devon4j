package com.devonfw.module.test.common.api.category;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Tag;

/**
 * This is the meta Annotation JUnit5 {@link org.junit.jupiter.api.Tag} for a System Test
 *
 */
@Tag("system")
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface TagSystemTest {

}
