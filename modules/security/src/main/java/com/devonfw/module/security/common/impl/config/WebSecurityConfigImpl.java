package com.devonfw.module.security.common.impl.config;

import java.util.Collection;

import javax.inject.Inject;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import com.devonfw.module.security.common.api.config.WebSecurityConfig;
import com.devonfw.module.security.common.api.config.WebSecurityConfigAspect;

/**
 * Implementation of {@link WebSecurityConfig}.
 *
 * @since 2020.12.001
 */
public class WebSecurityConfigImpl implements WebSecurityConfig {

  private Collection<WebSecurityConfigAspect> aspects;

  /**
   * @return the {@link Collection} of {@link WebSecurityConfigAspect}s to configure {@link HttpSecurity}.
   */
  public Collection<WebSecurityConfigAspect> getAspects() {

    return this.aspects;
  }

  /**
   * @param aspects the {@link Collection} of {@link WebSecurityConfigAspect}s to {@link Inject}.
   */
  @Inject
  public void setAspects(Collection<WebSecurityConfigAspect> aspects) {

    this.aspects = aspects;
  }

  @Override
  public HttpSecurity configure(HttpSecurity http) throws Exception {

    for (WebSecurityConfigAspect aspect : this.aspects) {
      http = aspect.configure(http);
    }
    return http;
  }

}
