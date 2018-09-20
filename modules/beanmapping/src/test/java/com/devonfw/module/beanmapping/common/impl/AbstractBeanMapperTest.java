package com.devonfw.module.beanmapping.common.impl;

import net.sf.mmm.util.entity.api.PersistenceEntity;
import net.sf.mmm.util.entity.api.RevisionedEntity;
import net.sf.mmm.util.entity.base.AbstractRevisionedEntity;
import net.sf.mmm.util.transferobject.api.EntityTo;

import org.junit.Test;

import com.devonfw.module.beanmapping.common.api.BeanMapper;
import com.devonfw.module.test.common.base.ModuleTest;

/**
 * The abstract test-case for testing {@link BeanMapper} via its interface.
 *
 */
public abstract class AbstractBeanMapperTest extends ModuleTest {

  /**
   * @return the {@link BeanMapper} instance to test.
   */
  protected abstract BeanMapper getBeanMapper();

  /**
   * Tests {@link BeanMapper#mapTypesafe(Class, Object, Class)} for an {@link PersistenceEntity entity} to an
   * {@link EntityTo ETO} and ensures that if the {@link PersistenceEntity#getModificationCounter() modification
   * counter} gets updated after conversion that the {@link EntityTo ETO} reflects this change.
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
    Number revision = 10L;
    entity.setRevision(revision);
    String property = "its magic";
    entity.setProperty(property);

    // when
    MyBeanEto eto = mapper.mapTypesafe(MyBean.class, entity, MyBeanEto.class);

    // then
    assertThat(eto).isNotNull();
    assertThat(eto.getId()).isEqualTo(id);
    assertThat(eto.getModificationCounter()).isEqualTo(version);
    assertThat(eto.getRevision()).isEqualTo(revision);
    assertThat(eto.getProperty()).isEqualTo(property);
    // sepcial feature: update of modificationCounter is performed when TX is closed what is typically after conversion
    int newVersion = version + 1;
    entity.setModificationCounter(newVersion);
    assertThat(eto.getModificationCounter()).isEqualTo(newVersion);
  }

  /**
   * Interface for {@link MyBeanEntity} and {@link MyBeanEto}.
   */
  public static interface MyBean extends RevisionedEntity<Long> {

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
  public static class MyBeanEntity extends AbstractRevisionedEntity<Long> implements PersistenceEntity<Long>, MyBean {

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

  /**
   * {@link EntityTo ETO} for testing.
   */
  public static class MyBeanEto extends EntityTo<Long> implements MyBean {

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
