package com.devonfw.test.app.myexample.service.api.rest;

import java.time.LocalDate;

import com.devonfw.module.basic.common.api.to.AbstractTo;

/**
 * {@link AbstractTo Transfer-object} for testing.
 */
public class MyExampleTo extends AbstractTo {

  private static final long serialVersionUID = 1L;

  private String name;

  private LocalDate birthday;

  /**
   * @return name
   */
  public String getName() {

    return this.name;
  }

  /**
   * @param name new value of {@link #getName()}.
   */
  public void setName(String name) {

    this.name = name;
  }

  /**
   * @return birthday
   */
  public LocalDate getBirthday() {

    return this.birthday;
  }

  /**
   * @param birthday new value of {@link #getBirthday()}.
   */
  public void setBirthday(LocalDate birthday) {

    this.birthday = birthday;
  }

}
