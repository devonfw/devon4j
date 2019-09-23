package com.devonfw.module.jpa.dataaccess.base;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.devonfw.example.TestApplication;
import com.devonfw.example.component.dataaccess.api.BarEntity;
import com.devonfw.example.component.dataaccess.impl.BarDaoTxBean;
import com.devonfw.module.jpa.dataaccess.api.GenericDao;
import com.devonfw.module.test.common.base.ComponentTest;

/**
 * Test class to test the {@link GenericDao}.
 */
@SpringBootTest(classes = { TestApplication.class }, webEnvironment = WebEnvironment.NONE)
public class AbstractGenericDaoTest extends ComponentTest {

  @Inject
  private BarDaoTxBean testBean;

  /**
   * Test of {@link GenericDao#forceIncrementModificationCounter(Object)}. Ensures that the modification counter is
   * updated after the call of that method when the transaction is closed.
   */
  @Test
  public void testForceIncrementModificationCounter() {

    // given
    BarEntity entity = this.testBean.create();
    assertThat(entity.getId()).isNotNull();
    assertThat(entity.getModificationCounter()).isEqualTo(0);

    // when
    BarEntity updatedEntity = this.testBean.incrementModificationCounter(entity.getId());

    // then
    assertThat(updatedEntity.getModificationCounter()).isEqualTo(1);
  }

};
