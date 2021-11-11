package com.devonfw.module.security.common.api.config;

/**
 * Interface for {@link AbstractWebSecurityConfigurer} as aspect. There can be any number of implementations of this
 * interface as {@link org.springframework.context.annotation.Bean spring-beans} that will automatically be applied
 * sequentially. This way security aspects like CSRF protection or CORS can be provided as spring-boot-starters. Users
 * of devon4j can then enable or disable such features only be adding or removing a dependency.
 *
 * @since 2020.12.001
 */
public interface WebSecurityConfigurerAspect extends AbstractWebSecurityConfigurer {

}
