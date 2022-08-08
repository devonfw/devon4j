package ${package};

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ${package}.general.common.beanmapping.BeanMapper;
import ${package}.general.common.to.AbstractEto;
import ${package}.general.dataaccess.ApplicationPersistenceEntity;
import jakarta.inject.Inject;

/**
 * This test verifies that {@link ${package}.SpringBootApp} is able to startup.
 */
public class ApplicationTest extends ComponentTest {

  @Inject
  @javax.inject.Named
  @Autowired
  private BeanMapper beanMapper;

  /** Test that {@link ${package}.SpringBootApp} is able to startup. */
  @Test
  public void testContextLoads() {

    // given
    Long id = Long.valueOf(4711);
    MyEntity entity = new MyEntity();
    entity.setId(id);
    // when
    MyEto eto = this.beanMapper.map(entity, MyEto.class);
    // then
    assertThat(eto.getId()).isEqualTo(id);
    assertThat(eto.getModificationCounter()).isEqualTo(0);
    // and when
    entity.setModificationCounter(5);
    // then
    assertThat(eto.getModificationCounter()).isEqualTo(5);
  }

  /** Dummy entity for testing. */
  public static class MyEntity extends ApplicationPersistenceEntity {
  }

  /** Dummy ETO for testing. */
  public static class MyEto extends AbstractEto {
  }
}