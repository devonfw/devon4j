package com.devonfw.example.component.common.api.to;

import com.devonfw.module.basic.common.api.query.StringSearchConfigTo;

/**
 * {@link SearchCriteriaTo} to find instances of {@link com.devonfw.example.component.common.api.Foo}.
 */
public class FooSearchCriteriaTo extends SearchCriteriaTo {

  private static final long serialVersionUID = 1L;

  private String message;

  private StringSearchConfigTo messageOption;

  /**
   * @return the string to search for {@link com.devonfw.example.component.common.api.Foo#getMessage() message}.
   */
  public String getMessage() {

    return this.message;
  }

  /**
   * @param message new value of {@link #getMessage()}.
   */
  public void setMessage(String message) {

    this.message = message;
  }

  /**
   * @return the {@link StringSearchConfigTo} used to search for {@link #getMessage() message}.
   */
  public StringSearchConfigTo getMessageOption() {

    return this.messageOption;
  }

  /**
   * @param messageOption new value of {@link #getMessageOption()}.
   */
  public void setMessageOption(StringSearchConfigTo messageOption) {

    this.messageOption = messageOption;
  }

}
