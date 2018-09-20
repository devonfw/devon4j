package com.devonfw.module.beanmapping.common.impl;

import java.util.Arrays;
import java.util.List;

import net.sf.mmm.util.entity.api.PersistenceEntity;
import net.sf.mmm.util.transferobject.api.EntityTo;

import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;

import com.devonfw.module.beanmapping.common.api.BeanMapper;
import com.devonfw.module.beanmapping.common.impl.dozer.BeanMapperImplDozer;
import com.devonfw.module.beanmapping.common.impl.dozer.IdentityConverter;

/**
 * Test of {@link BeanMapperImplDozer} based on {@link AbstractBeanMapperTest}.
 *
 */
public class BeanMapperImplDozerTest extends AbstractBeanMapperTest {

  @Override
  protected BeanMapper getBeanMapper() {

    BeanMapperImplDozer mapper = new BeanMapperImplDozer();
    List<String> mappingFiles = Arrays.asList("config/app/common/dozer-mapping.xml");
    DozerBeanMapper dozer = new DozerBeanMapper(mappingFiles);
    BeanMappingBuilder builder = new BeanMappingBuilder() {

      @Override
      protected void configure() {

        mapping(PersistenceEntity.class, EntityTo.class).fields(this_(), field("persistentEntity").accessible(),
            FieldsMappingOptions.customConverter(IdentityConverter.class));
      }
    };
    dozer.addMapping(builder);
    mapper.setDozer(dozer);
    return mapper;
  }

}
