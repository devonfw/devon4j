package com.devonfw.module.security.common.impl.config;

import java.util.Collection;

import javax.inject.Inject;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import com.devonfw.module.security.common.api.config.WebSecurityConfigurer;
import com.devonfw.module.security.common.api.config.WebSecurityConfigurerAspect;

/**
 * Implementation of {@link WebSecurityConfigurer}.
 *
 * @since 2020.12.001
 */
public class WebSecurityConfigurerImpl implements WebSecurityConfigurer {

  private Collection<WebSecurityConfigurerAspect> aspects;

  /**
   * @return the {@link Collection} of {@link WebSecurityConfigurerAspect}s to configure {@link HttpSecurity}.
   */
  public Collection<WebSecurityConfigurerAspect> getAspects() {

    return this.aspects;
  }

  /**
   * @param aspects the {@link Collection} of {@link WebSecurityConfigurerAspect}s to {@link Inject}.
   */
  @Inject
  public void setAspects(Collection<WebSecurityConfigurerAspect> aspects) {

    this.aspects = aspects;
  }

  @Override
  public HttpSecurity configure(HttpSecurity http) throws Exception {

    for (WebSecurityConfigurerAspect aspect : this.aspects) {
      http = aspect.configure(http);
    }
    return http;
  }

}
