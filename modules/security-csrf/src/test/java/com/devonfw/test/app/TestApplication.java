package com.devonfw.test.app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.devonfw.module.security.csrf.common.impl.CsrfAutoConfiguration;

/**
 * Spring-boot app for testing.
 */
@SpringBootApplication
@Import({ CsrfAutoConfiguration.class })
public class TestApplication {

}
