package ${package}.general.common.impl.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.beanmapping.common.base.BaseOrikaConfig;

/**
 * Java bean configuration for Orika
 * 
 *  {@link #configureCustomMapping(MapperFactory)} from {@link BaseOrikaConfig} can be overridden as per requirements
 */
@Configuration
@ComponentScan(basePackages = { "com.devonfw.module.beanmapping" })
public class BeansOrikaConfig extends BaseOrikaConfig{

 
}
