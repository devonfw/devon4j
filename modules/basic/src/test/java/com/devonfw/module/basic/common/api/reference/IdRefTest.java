package com.devonfw.module.basic.common.api.reference;

import net.sf.mmm.util.entity.api.MutableGenericEntity;

import org.junit.Test;

import com.devonfw.module.basic.common.api.reference.IdRef;
import com.devonfw.module.basic.common.api.to.AbstractEto;
import com.devonfw.module.test.common.base.ModuleTest;

/**
 * Test of {@link IdRef}.
 */
public class IdRefTest extends ModuleTest {

  /** Test of {@link IdRef#of(net.sf.mmm.util.entity.api.GenericEntity)} */
  @Test
  public void testOfEntity() {

    // given
    long id = 4711L;
    FooEto foo = new FooEto();
    foo.setId(id);

    // when
    IdRef<Foo> fooId = IdRef.<Foo> of(foo); // with Java8 type-inference the additional <Foo> is not required

    // then
    assertThat(fooId).isNotNull();
    assertThat(fooId.getId()).isEqualTo(foo.getId()).isEqualTo(id);
    assertThat(fooId.toString()).isEqualTo(Long.toString(id));
    assertThat(IdRef.of((Foo) null)).isNull();

    Bar bar = new Bar();
    bar.setFooId(fooId); // just a syntax/compilation check.
    // bar.setBarId(fooId); // will produce compiler error what is desired in such case
    IdRef<Bar> barId = IdRef.of(1234L);
    bar.setBarId(barId); // this again will compile
  }

  /** Test of {@link IdRef#of(Long)} */
  @Test
  public void testOfLong() {

    // given
    long id = 4711L;

    // when
    IdRef<Foo> fooId = IdRef.of(id); // not type-safe but required in some cases

    // then
    assertThat(fooId).isNotNull();
    assertThat(fooId.getId()).isEqualTo(id);
    assertThat(fooId.toString()).isEqualTo(Long.toString(id));
    assertThat(IdRef.of(Long.valueOf(id))).isEqualTo(fooId).isNotSameAs(fooId);
    assertThat(IdRef.of((Long) null)).isNull();
  }

  private interface Foo extends MutableGenericEntity<Long> {

  }

  private class FooEto extends AbstractEto implements Foo {

    private static final long serialVersionUID = 1L;

  }

  private class Bar {

    void setFooId(IdRef<Foo> fooId) {

    }

    void setBarId(IdRef<Bar> fooId) {

    }
  }

}
