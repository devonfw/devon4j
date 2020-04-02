package com.devonfw.module.jpa.dataaccess.api;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.devonfw.example.TestApplication;
import com.devonfw.example.component.common.api.to.FooEto;
import com.devonfw.example.component.dataaccess.api.BarEntity;
import com.devonfw.example.component.dataaccess.api.FooEntity;
import com.devonfw.module.beanmapping.common.api.BeanMapper;
import com.devonfw.module.jpa.dataaccess.api.JpaHelper;
import com.devonfw.module.test.common.base.ComponentTest;

/**
 * Test of {@link JpaHelper}.
 */
@Transactional
@SpringBootTest(classes = { TestApplication.class }, webEnvironment = WebEnvironment.NONE)
public class JpaHelperTest extends ComponentTest {

  @PersistenceContext
  private EntityManager entityManager;

  @Inject
  private BeanMapper beanMapper;

  /**
   * Test of {@link JpaHelper#asEntity(com.devonfw.module.basic.common.api.reference.Ref, Class)} via real production-like
   * scenario.
   */
  @Test
  public void testIdRefAsEntity() {

    // given
    BarEntity barEntity = new BarEntity();
    barEntity.setMessage("Test message");
    FooEntity fooEntity = new FooEntity();
    fooEntity.setName("Test name");
    fooEntity.setBar(barEntity);

    // when
    this.entityManager.persist(fooEntity);
    FooEto fooEto = new FooEto();
    fooEto.setId(fooEntity.getId());
    fooEto.setModificationCounter(fooEntity.getModificationCounter());
    fooEto.setName(fooEntity.getName());
    fooEto.setBarId(fooEntity.getBarId());

    FooEntity fooEntity2 = this.beanMapper.map(fooEto, FooEntity.class);

    // then
    assertThat(fooEntity2.getBar()).isSameAs(barEntity);
  }

}
