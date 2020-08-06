package com.devonfw.module.cxf.common.impl.client;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.devonfw.module.service.common.impl.ServiceClientAutoConfiguration;

/**
 * {@link Configuration} for REST (JAX-RS) clients using Apache CXF.
 *
 * @since 3.0.0
 */
@Configuration
@Import(ServiceClientAutoConfiguration.class)
public class CxfClientAutoConfiguration {

}
