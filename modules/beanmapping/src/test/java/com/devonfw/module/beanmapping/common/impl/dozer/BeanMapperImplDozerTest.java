package com.devonfw.module.beanmapping.common.impl.dozer;

import java.util.Arrays;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;

import com.devonfw.module.basic.common.api.entity.PersistenceEntity;
import com.devonfw.module.basic.common.api.to.AbstractEto;
import com.devonfw.module.beanmapping.common.api.BeanMapper;
import com.devonfw.module.beanmapping.common.impl.AbstractBeanMapperTest;
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

        mapping(PersistenceEntity.class, AbstractEto.class).fields(this_(), field("persistentEntity").accessible(),
            FieldsMappingOptions.customConverter(IdentityConverter.class));
      }
    };
    dozer.addMapping(builder);
    mapper.setDozer(dozer);
    return mapper;
  }

}
