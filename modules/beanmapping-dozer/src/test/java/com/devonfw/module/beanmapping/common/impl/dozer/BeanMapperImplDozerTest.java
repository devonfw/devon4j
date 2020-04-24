package com.devonfw.module.beanmapping.common.impl.dozer;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.devonfw.module.basic.common.api.entity.GenericEntity;
import com.devonfw.module.basic.common.api.entity.PersistenceEntity;
import com.devonfw.module.basic.common.api.to.AbstractEto;
import com.devonfw.module.basic.common.api.to.AbstractGenericEto;
import com.devonfw.module.beanmapping.common.api.BeanMapper;
import com.devonfw.module.test.common.base.ModuleTest;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.loader.api.BeanMappingBuilder;
import com.github.dozermapper.core.loader.api.FieldsMappingOptions;

/**
 * Test of {@link BeanMapperImplDozer} .
 *
 */
public class BeanMapperImplDozerTest extends ModuleTest {

  private BeanMapper getBeanMapper() {

    BeanMapperImplDozer mapper = new BeanMapperImplDozer();
    List<String> mappingFiles = Arrays.asList("config/app/common/dozer-mapping.xml");
    BeanMappingBuilder builder = new BeanMappingBuilder() {

      @Override
      protected void configure() {

        mapping(PersistenceEntity.class, AbstractGenericEto.class).fields(this_(),
            field("persistentEntity").accessible(), FieldsMappingOptions.customConverter(IdentityConverter.class));
      }
    };
    Mapper dozer = DozerBeanMapperBuilder.create().withMappingBuilder(builder).withMappingFiles(mappingFiles).build();
    mapper.setDozer(dozer);
    return mapper;
  }

  /**
   * Tests {@link BeanMapper#mapTypesafe(Class, Object, Class)} for a persistence entity to an {@link AbstractEto ETO}
   * and ensures that if the {@link GenericEntity#getModificationCounter() modification counter} gets updated after
   * conversion that the {@link AbstractEto ETO} reflects this change.
   */
  @Test
  public void testMapEntity2Eto() {

    // given
    BeanMapper mapper = getBeanMapper();
    MyBeanEntity entity = new MyBeanEntity();
    Long id = 1L;
    entity.setId(id);
    int version = 1;
    entity.setModificationCounter(version);
    String property = "its magic";
    entity.setProperty(property);

    // when
    MyBeanEto eto = mapper.mapTypesafe(MyBean.class, entity, MyBeanEto.class);

    // then
    assertThat(eto).isNotNull();
    assertThat(eto.getId()).isEqualTo(id);
    assertThat(eto.getModificationCounter()).isEqualTo(version);
    assertThat(eto.getProperty()).isEqualTo(property);
    // sepcial feature: update of modificationCounter is performed when TX is closed what is typically after conversion
    int newVersion = version + 1;
    entity.setModificationCounter(newVersion);
    assertThat(eto.getModificationCounter()).isEqualTo(newVersion);
  }

  /**
   * Interface for {@link MyBeanEntity} and {@link MyBeanEto}.
   */
  public static interface MyBean extends GenericEntity<Long> {

    /**
     * @return property
     */
    String getProperty();

    /**
     * @param property the property to set
     */
    void setProperty(String property);
  }

  /**
   * {@link PersistenceEntity} for testing.
   */
  public static class MyBeanEntity implements MyBean, PersistenceEntity<Long> {

    private static final long serialVersionUID = 1L;

    private Long id;

    private int modificationCounter;

    private String property;

    @Override
    public Long getId() {

      return this.id;
    }

    @Override
    public void setId(Long id) {

      this.id = id;
    }

    @Override
    public int getModificationCounter() {

      return this.modificationCounter;
    }

    @Override
    public void setModificationCounter(int modificationCounter) {

      this.modificationCounter = modificationCounter;
    }

    @Override
    public String getProperty() {

      return this.property;
    }

    @Override
    public void setProperty(String property) {

      this.property = property;
    }

  }

  /**
   * {@link AbstractEto ETO} for testing.
   */
  public static class MyBeanEto extends AbstractEto implements MyBean {

    private static final long serialVersionUID = 1L;

    private String property;

    @Override
    public String getProperty() {

      return this.property;
    }

    @Override
    public void setProperty(String property) {

      this.property = property;
    }
  }

}
