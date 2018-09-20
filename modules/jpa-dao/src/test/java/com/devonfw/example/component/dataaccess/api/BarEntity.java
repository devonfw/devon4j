package com.devonfw.example.component.dataaccess.api;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.devonfw.example.component.common.api.Bar;
import com.devonfw.example.general.dataaccess.api.TestApplicationPersistenceEntity;

/**
 * Implementation of {@link Bar} as {@link TestApplicationPersistenceEntity persistence entity}.
 */
@Entity
@Table(name = "Bar")
public class BarEntity extends TestApplicationPersistenceEntity implements Bar {
  private static final long serialVersionUID = 1L;

  private String message;

  @Override
  public String getMessage() {

    return this.message;
  }

  @Override
  public void setMessage(String message) {

    this.message = message;
  }

}
