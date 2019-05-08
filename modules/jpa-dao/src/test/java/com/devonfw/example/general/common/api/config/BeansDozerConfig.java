package com.devonfw.example.general.common.api.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.github.dozermapper.core.DozerBeanMapper;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

/**
 * Java bean configuration for Dozer
 */
@Configuration
@ComponentScan(basePackages = { "com.devonfw.module.beanmapping" })
public class BeansDozerConfig {

  private static final String DOZER_MAPPING_XML = "config/app/common/dozer-mapping.xml";

  /**
   * @return the {@link DozerBeanMapper}.
   */
  @Bean
  public Mapper getDozer() {

    List<String> beanMappings = new ArrayList<>();
    beanMappings.add(DOZER_MAPPING_XML);

    Mapper mapper = DozerBeanMapperBuilder.create().withMappingFiles(beanMappings).build();
    return mapper;

  }
}
