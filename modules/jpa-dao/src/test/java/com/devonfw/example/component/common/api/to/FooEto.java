package com.devonfw.example.component.common.api.to;

import com.devonfw.example.component.common.api.Bar;
import com.devonfw.example.component.common.api.Foo;
import com.devonfw.module.basic.common.api.reference.IdRef;
import com.devonfw.module.basic.common.api.to.AbstractEto;

/**
 * Implementation of {@link Foo} as {@link AbstractEto ETO}.
 */
public class FooEto extends AbstractEto implements Foo {
  private static final long serialVersionUID = 1L;

  private String name;

  private IdRef<Bar> barId;

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public void setName(String name) {

    this.name = name;
  }

  @Override
  public IdRef<Bar> getBarId() {

    return this.barId;
  }

  @Override
  public void setBarId(IdRef<Bar> barId) {

    this.barId = barId;
  }

}
