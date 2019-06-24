package com.devonfw.module.beanmapping.common.impl.dozer;

import java.util.Arrays;
import java.util.List;

import com.devonfw.module.basic.common.api.entity.PersistenceEntity;
import com.devonfw.module.basic.common.api.to.AbstractEto;
import com.devonfw.module.beanmapping.common.api.BeanMapper;
import com.devonfw.module.beanmapping.common.impl.AbstractBeanMapperTest;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.loader.api.BeanMappingBuilder;
import com.github.dozermapper.core.loader.api.FieldsMappingOptions;

/**
 * Test of {@link BeanMapperImplDozer} based on {@link AbstractBeanMapperTest}.
 *
 */
public class BeanMapperImplDozerTest extends AbstractBeanMapperTest {

  @Override
  protected BeanMapper getBeanMapper() {

    BeanMapperImplDozer mapper = new BeanMapperImplDozer();
    List<String> mappingFiles = Arrays.asList("config/app/common/dozer-mapping.xml");
    BeanMappingBuilder builder = new BeanMappingBuilder() {

      @Override
      protected void configure() {

        mapping(PersistenceEntity.class, AbstractEto.class).fields(this_(), field("persistentEntity").accessible(),
            FieldsMappingOptions.customConverter(IdentityConverter.class));
      }
    };
    Mapper dozer = DozerBeanMapperBuilder.create().withMappingBuilder(builder).withMappingFiles(mappingFiles).build();
    mapper.setDozer(dozer);
    return mapper;
  }

}
