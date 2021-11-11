package com.devonfw.test.app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.devonfw.module.security.jwt.common.impl.JwtAutoConfiguration;
import com.devonfw.module.security.keystore.common.impl.KeyStoreAutoConfiguration;

/**
 * Spring-boot app for testing.
 */
@SpringBootApplication
@Import({ KeyStoreAutoConfiguration.class, JwtAutoConfiguration.class })
public class TestApplication {

}
