package com.devonfw.test.app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.devonfw.module.security.cors.common.impl.CorsAutoConfiguration;

/**
 * Spring-boot app for testing.
 */
@SpringBootApplication
@Import({ CorsAutoConfiguration.class })
public class TestApplication {

}
