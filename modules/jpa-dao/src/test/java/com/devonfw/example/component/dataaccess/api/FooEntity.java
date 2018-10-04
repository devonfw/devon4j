package com.devonfw.example.component.dataaccess.api;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.devonfw.example.component.common.api.Bar;
import com.devonfw.example.component.common.api.Foo;
import com.devonfw.example.general.dataaccess.api.TestApplicationPersistenceEntity;
import com.devonfw.module.basic.common.api.reference.IdRef;
import com.devonfw.module.jpa.dataaccess.api.JpaHelper;

/**
 * Implementation of {@link Foo} as {@link TestApplicationPersistenceEntity persistence entity}.
 */
@Entity
@Table(name = "Foo")
public class FooEntity extends TestApplicationPersistenceEntity implements Foo {
  private static final long serialVersionUID = 1L;

  private String name;

  private BarEntity bar;

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public void setName(String name) {

    this.name = name;
  }

  /**
   * @return the {@link BarEntity}.
   */
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "bar")
  public BarEntity getBar() {

    return this.bar;
  }

  /**
   * @param bar new value of {@link #getBar()}.
   */
  public void setBar(BarEntity bar) {

    this.bar = bar;
  }

  @Override
  @Transient
  public IdRef<Bar> getBarId() {

    return IdRef.of(this.bar);
  }

  @Override
  public void setBarId(IdRef<Bar> barId) {

    this.bar = JpaHelper.asEntity(barId, BarEntity.class);
  }

}
